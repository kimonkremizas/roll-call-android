package services

import android.util.Log
import controllers.UserController
import models.Event
import models.User

class LoginService {
    companion object {
        @JvmStatic val onLoginSuccessful = Event<User>()
    }

    suspend fun LoginUser(email:String, password:String) {
        var loggedInUser:User = User()
        var loginSuccessful: Boolean = false

        val userController:UserController = UserController()
        userController.LogIn(email, password)

        if(loginSuccessful) {
            onLoginSuccessful.invoke(loggedInUser)
        }
    }
}