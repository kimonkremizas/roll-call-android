package appLogic

import android.util.Log
import models.Lesson
import models.User
import services.LessonService
import services.LoginService

object AppState {
    enum class State {
        LoggedOut,
        LoggedIn
    }

    var CurrentState: State = State.LoggedOut
        private set
    var CurrentUser: User = User()
        private set
    var CurrentLesson : Lesson = Lesson()

    init {
        LoginService.onLoginSuccessful += { user -> SetCurrentUser(user) }
        LessonService.onCurrentLessonChanged += { lesson -> SetCurrentLesson(lesson) }
    }

    private fun SetCurrentUser(user: User) {
        CurrentUser = user
        CurrentState = State.LoggedIn
    }

    private fun SetCurrentLesson(lesson: Lesson) {
        CurrentLesson = lesson
    }
}