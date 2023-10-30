package com.henge


import grails.testing.gorm.DataTest
import grails.testing.services.ServiceUnitTest
import org.grails.testing.GrailsUnitTest
import spock.lang.Specification

class PostServiceSpec extends Specification implements ServiceUnitTest<PostService>, DataTest {

    def setup() {
        mockDomain User
    }

    def cleanup() {
    }

    void "Valid posts get saved and added to the user"() {
        given:
        new User(loginId: "12abc", password: "password", homepage: "http://www.hle.com").save(failOnError: true)

        when:
        def newPost = service.createPost("12abc", "first post")

        then:
        newPost.content == "first post"
        User.findByLoginId("12abc").posts.size() == 1
    }
}