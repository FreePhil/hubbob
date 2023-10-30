package com.henge

import grails.testing.gorm.DataTest
import grails.testing.web.controllers.ControllerUnitTest
import org.grails.testing.GrailsUnitTest
import spock.lang.Specification

class PostControllerSpec extends Specification implements ControllerUnitTest<PostController>, DataTest {

    def setup() {
    }

    def cleanup() {
    }

    void "Adding a valid new post to the timeline"() {
        given: "Mock a post service"
        def service = Mock(PostService)
        1 * service.createPost(_, _) >>
                new Post(content: "Mock Post")
        controller.postService = service

        when: "Controller is invoked"
        def result = controller.addPost("123cool", "Posting up a storm")

        then: "redirected to timeline"
        flash.message ==~ /Added new post.*/
        response.redirectedUrl == '/post/timeline/123cool'
    }


}