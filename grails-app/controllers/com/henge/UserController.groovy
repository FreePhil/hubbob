package com.henge

class UserController {

    static scaffold = User

    def handleCommand(UserRegistrationCommand command) {
        if (command.hasErrors())
        {}
    }

    def register() {
        if (request.method == "POST") {
            def user = new User(params)
            if (user.validate()) {
                user.save()
                flash.message = "Successfully created user"
                redirect(uri: "/")
            } else {
                flash.message = "Error registering user"
                return [user: user]
            }
        }
    }
}

class UserRegistrationCommand {
    String loginId
    String password
    String passwordRepeat
    byte[] photo
    String fullName
    String bio
    String homepage
    String email
    String timezone
    String country
    String jabberAddress

    static constraints = {
        importFrom User
        importFrom Profile
        password size: 6..20, blank: false, validator: { password, user -> return password != user.loginId }
        passwordRepeat nullable: false, validator: { password2, user -> return password2 == user.password }
    }
}
