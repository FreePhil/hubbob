package com.henge

import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration
import spock.lang.Specification

@Integration
@Rollback
class PostIntegrationSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    def "Add posts to user links post to user"() {
        given: "A brand new user"
        def joe = new User(loginId: '1234567joe', password: 'secret', homepage: 'http://www.grails.com')
        joe.save(failOnError: true)

        when: "Serveral posts are added to the user"
        joe.addToPosts(new Post(content: "First post..."))
        joe.addToPosts(new Post(content: "Second post..."))
        joe.addToPosts(new Post(content: "Third post..."))

        then: "The user has three of posts"
        3 == User.get(joe.id).posts.size()
    }

    def "Ensure post linked to a suer can be retrieved"() {
        given: "A user with several posts"
        def user = new User(loginId: '1234567joe', password: 'secret', homepage: 'http://www.grails.com')
        user.addToPosts(new Post(content: "First"))
        user.addToPosts(new Post(content: "Second"))
        user.addToPosts(new Post(content: "Third"))
        user.save(failOnError: true)

        when: "The user is retrieved by their id"
        def foundUser = User.get(user.id)
        def sortedPostContent = foundUser.posts.collect {
            it.content
        }

        then: "The posts appear on the retrieved user"
        sortedPostContent == ["First", "Second", "Third"]
    }

    def "Exercise tagging several posts with various tag"() {
        given: "A user with a set of tags"
        def user = new User(loginId: '1234567joe', password: 'secret', homepage: 'http://www.grails.com')
        def tagGroovy = new Tag(name: "Groovy")
        def tagGrails = new Tag(name: "Grails")
        user.addToTags(tagGroovy)
        user.addToTags(tagGrails)
        user.save(failOnError: true)

        when: "The user tags two fresh posts"
        def groovyPost = new Post(content: "A groovy post")
        user.addToPosts(groovyPost)
        groovyPost.addToTags(tagGroovy)

        def bothPost = new Post(content: "A groovy and grails post")
        user.addToPosts(bothPost)
        bothPost.addToTags(tagGroovy)
        bothPost.addToTags(tagGrails)

        def savedUser = User.get(user.id)

        then:
        savedUser.tags*.name.sort() == ["Grails", "Groovy"]
        1 == groovyPost.tags.size()
        2 == bothPost.tags.size()
    }
}
