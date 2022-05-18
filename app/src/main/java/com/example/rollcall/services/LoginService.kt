package com.example.rollcall.services

import com.example.rollcall.controllers.UserController
import com.example.rollcall.models.Event
import com.example.rollcall.models.User

class LoginService {
    companion object {
        @JvmStatic val onLoginSuccessful = Event<User>()
        @JvmStatic val onLoginUnsuccessful = Event<String>()
    }

    suspend fun LoginUser(email:String, password:String):Boolean {
        var loggedInUser:User = User()

        val userController:UserController = UserController()
        loggedInUser = userController.LogIn(email, password)

        if(loggedInUser.Id != 0)
        {
            onLoginSuccessful.invoke(loggedInUser)
            return true
        }

        //I figured this would be the easiest way
        onLoginUnsuccessful.invoke(loggedInUser.FirstName)
        return false
    }
}