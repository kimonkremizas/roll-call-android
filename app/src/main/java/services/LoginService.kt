package services

import android.util.Log
import models.Event
import models.User

class LoginService {
    companion object {
        @JvmStatic val onLoginSuccessful = Event<User>()
    }

    fun LoginUser() {
        var loggedInUser:User = User()
        Log.d("kek", ">:((((((((")
        onLoginSuccessful.invoke(loggedInUser)
    }
}