package controllers

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import models.Lesson
import models.User
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.io.IOException
import java.util.concurrent.TimeUnit

class LessonController {
    private val lessonUrl: String = "https://rollcallsystem-kea.azurewebsites.net/api/Lessons"
    private val client = OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build()

    suspend fun GetCurrentLesson(user: User): Lesson {
        var lesson: Lesson = Lesson()

        var lessonData: LessonData

        val request = Request.Builder().url("$lessonUrl/Current")
                .addHeader("Authorization", "Bearer ${user.Token}").build()

        try {
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")
                response.body()?.let {
                    lessonData = Json.decodeFromString(it.string())
                    lesson = Lesson(lessonData.id, lessonData.subjectName, lessonData.startTime,
                            lessonData.code, lessonData.codeTime, lessonData.campusName, lessonData.teacherName)
                    //lesson = Lesson(lessonData.id, lessonData.subjectName, lessonData.code, lessonData.campusName, lessonData.teacherName)
                    return lesson
                }
            }
        }
        catch (ex: IOException) {
            lesson.SubjectName = ex.message.toString()
            return lesson
        }

        return lesson
    }

    suspend fun CheckIntoLesson(user: User, lesson: Lesson, code: Int): Boolean
    {
        val requestBody:RequestBody =
            RequestBody.create(MediaType.parse("application/json"), "")

        val request = Request.Builder().url("$lessonUrl/CheckIn/${lesson.Id}?code=$code")
            .addHeader("Authorization", "Bearer ${user.Token}").post(requestBody).build()

        try {
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")
                response.body()?.let {
                    val bool:Boolean = Json.decodeFromString(it.string())
                    return bool
                }
            }
        }
        catch (ex: IOException) {
            return false
        }

        return false
    }

    suspend fun CheckIfCheckedIn(user: User, lesson: Lesson): Boolean {
        val request = Request.Builder().url("$lessonUrl/CheckedIn/${lesson.Id}")
            .addHeader("Authorization", "Bearer ${user.Token}").build()

        try {
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")
                response.body()?.let {
                    return true
                }
            }
        }
        catch (ex: IOException) {
            return false
        }

        return false
    }

    @Serializable
    data class LessonData(val id: Int, val subjectId: Int, val subjectName: String,
                          val startTime: LocalDateTime, val code: Int?, val codeTime: LocalDateTime?,
                            val campusId: Int, val campusName: String, val teacherName: String)
}