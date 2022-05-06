package appLogic

import android.util.Log
import models.User
import services.LoginService

object AppState {
    enum class State {
        LoggedOut,
        LoggedIn
    }

    var CurrentState: State = State.LoggedOut
        private set
    var CurrentUser: User? = null
        private set

    init {
        LoginService.onLoginSuccessful += { user -> SetCurrentUser(user) }
    }

    private fun SetCurrentUser(user: User) {
        CurrentUser = user
        CurrentState = State.LoggedIn
    }
}