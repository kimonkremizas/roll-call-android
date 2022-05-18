package com.example.rollcall.appLogic

import com.example.rollcall.models.Lesson
import com.example.rollcall.models.User
import com.example.rollcall.services.LessonService
import com.example.rollcall.services.LoginService

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