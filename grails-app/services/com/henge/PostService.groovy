package com.henge

import grails.gorm.transactions.Transactional

class PostException extends RuntimeException {
    String message
}

@Transactional
class PostService {

    double version = 3.1

    def createPost(String loginId, String content) {
        def user = User.findByLoginId(loginId)
        if (user) {
            def post = new Post(content: content)
            user.addToPosts(post)
            if (post.validate()) {
                if (user.save()) {
                    return post
                }
            }
            throw new PostException(message: "Invalid or empty post", post: post)
        }

        throw new PostException(message: "Invalid User Id ${loginId}")
    }
}
