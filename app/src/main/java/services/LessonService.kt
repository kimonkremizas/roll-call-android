package services

import controllers.LessonController
import models.Event
import models.Lesson
import models.User

class LessonService {
    companion object {
        @JvmStatic val onCurrentLessonChanged = Event<Lesson>()
    }
    val lessonController:LessonController = LessonController()

    suspend fun GetCurrentLesson(user: User):Boolean {
        var lesson:Lesson = Lesson()

        lesson = lessonController.GetCurrentLesson(user)

        if(lesson.Id != 0) {
            onCurrentLessonChanged.invoke(lesson)
            return true
        }

        return false
    }

    suspend fun CheckIntoLesson(user: User, lesson: Lesson, code: Int):String {
        if(code != lesson.Code) {
            return "Incorrect"
        }

        val result: Boolean = lessonController.CheckIntoLesson(user, lesson, code)

        if(result == true) {
            return "Success"
        }
        else {
            return "Fail"
        }
    }
}