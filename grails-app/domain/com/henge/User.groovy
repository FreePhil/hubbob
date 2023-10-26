package com.henge

class User {

    String loginId
    String password
    String homepage
    Date dateCreated

    static constraints = {
        loginId size: 3..20, unique: true, nullable: false, matches: /[0-9]{7}[A-Za-z]*/
        password size: 6..20, nullable: false, validator: { password, user -> user.loginId != password }
        homepage url: true, nulllable: true
    }
}
