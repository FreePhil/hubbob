package com.henge

class UserController {

    static scaffold = User

    def timeline(String id) {
        render "${id} rendered"
    }
}
