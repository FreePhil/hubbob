package com.henge

class User {

    String loginId
    String password
    String homepage
    Date dateCreated

    static hasMany = [posts: Post, tags: Tag, following: User]
    static hasOne = [profile: Profile]
    static constraints = {
        loginId size: 3..20, unique: true, nullable: false, matches: /.*/
        password size: 6..20, nullable: false, validator: { password, user -> user.loginId != password }
        homepage nulllable: true
        profile nullable: true
        tags()
        posts()
    }
}
