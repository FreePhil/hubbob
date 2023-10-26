package com.henge

import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration
import spock.lang.Specification

@Integration
@Rollback
class UserIntegrationSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    def "Save our first user to the database"() {
        given: "A brand new user"
            def joe = new User(loginId: '1234567joe', password: 'secret', homepage: 'http://www.grails.com')

        when: "The user is saved"
            joe.save()

        then: "It shoud save successfully and can be retrieved from database"
            joe.errors.errorCount == 0
            joe.id != null
            User.get(joe.id).loginId == joe.loginId

    }

    def "Update a user's properties"() {
        given: "An existing user"
            def existingUser = new User(loginId: '1234567joe', password: 'secret', homepage: 'http://www.redhat.com')
            existingUser.save(failOnError: true)

        when: "A property is changed"
            def foundUser = User.get(existingUser.id)
            foundUser.password = 'sesame'
            foundUser.save(failOnError: true)

        then: "The change is reflected in the database"
            User.get(existingUser.id).password == 'sesame'
    }

    def "Delete a user"() {
        given: "An existing user"
            def user = new User(loginId: '1234567joe', password: 'secret', homepage: 'http://www.redhat.com')
            user.save(failOnError: true)

        when: "Delete the user"
            def foundUser = User.get(user.id)
            foundUser.delete(flush: true)

        then: "The user is removed from the database"
            !User.exists(user.id)
    }

    def "Saving a user with invalid properties causes an error"() {
        given: "A user which fails several field validations"
            def user = new User(loginId: 'joe', password: 'tiny', homepage: 'not-a-url')

        when: "The user is validated"
            user.validate()

        then: "There must be errors"
            user.hasErrors()

            "size.toosmall" == user.errors.getFieldError("password").code
            "tiny" == user.errors.getFieldError('password').rejectedValue
            'url.invalid' == user.errors.getFieldError('homepage').code
            'not-a-url' == user.errors.getFieldError('homepage').rejectedValue
            "matches.invalid" == user.errors.getFieldError('loginId').code
            'joe' == user.errors.getFieldError('loginId').rejectedValue
    }

    def "Saving a user with loginId as the same as password causes an error"() {
        given: "A user which has the same value for loginId an password"
        def user = new User(loginId: '1234567joe', password: '1234567joe', homepage: 'not-a-url')

        when:
        user.validate()

        then:
        user.hasErrors()

        "validator.invalid" == user.errors.getFieldError("password").code
        "1234567joe" == user.errors.getFieldError('password').rejectedValue
    }
}
