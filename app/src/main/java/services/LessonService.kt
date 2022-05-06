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
}