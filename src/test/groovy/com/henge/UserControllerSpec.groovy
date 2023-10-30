package com.henge

import grails.testing.gorm.DataTest
import grails.testing.web.controllers.ControllerUnitTest
import org.grails.testing.GrailsUnitTest
import spock.lang.Specification

import javax.swing.JPasswordField

class UserControllerSpec extends Specification implements ControllerUnitTest<UserController>, DataTest {

    def setup() {
        mockDomains User, Profile
    }

    def cleanup() {
    }

    void "Registering a user with known good parameters"() {
        given: "A set of user parameter"
        params.with {
            loginId = "glen_a_smith"
            password = "winning"
            homepage = "http://blog.bytecode.com/glen"
        }

        and:
        params['profile.fullName'] = "Glen Smith"
        params['profile.email'] = "glen@gmail.com"
        params['profile.homepage'] = "http://www.gmail.com"
        params['profile.bio'] = "some text"

        when: "The user is registered"
        request.method = "POST"
        controller.register()

        then: "The user is created and browser redirect"
        response.redirectedUrl == "/"
        User.count() == 1
        Profile.count() == 1
    }

    void "Registration command object or loginId"() {

        given: "A mocked command object"
        def urc = new UserRegistrationCommand()
        urc.loginId = loginId
        urc.password = password
        urc.passwordRepeat = passwordRepeat
        urc.fullName = "Your name here"
        urc.email = "someone@gmail.com"

        when: "The validator is invoked"
        controller.handleCommand(urc)

        then: "The appropriate fields are flagged as errors"
        isValidRegistration == anticipatedValid
        urc.errors.getFieldError(fieldInError)?.code == errorCode

        where:
        loginId     | password      | passwordRepeat    | anticipatedValid  | fieldInError      | errorCode
        "glen"      | "password"    | "nomatch"         | false             | "passwordRepeat"  | "validator.invalid"
        "peter"     | "password"    | "password"        | true              | null              | null
        "a"         | "password"    | "password"        | false             | "loginId"         | "size.toosmall"
    }
}