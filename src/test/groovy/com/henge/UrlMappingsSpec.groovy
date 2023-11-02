package com.henge

import grails.testing.web.UrlMappingsUnitTest
import spock.lang.Specification

class UrlMappingsSpec extends Specification implements UrlMappingsUnitTest<UrlMappings>{

    def setup() {
        mockController UserController
    }

    void "test user timeline"() {

        expect:
            assertForwardUrlMapping(url, controller: expectCtrl, action: expectAction) {
                id != expectId
            }

        where:
            url                 | expectCtrl    | expectAction      | expectId
            '/test/timeline/c'  | 'user'        | 'timeline'        | 'c'
    }
}
