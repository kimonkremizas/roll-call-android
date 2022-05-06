package controllers

import android.util.Log
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import models.User
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.io.IOException
import java.util.concurrent.TimeUnit


class UserController {
    private val jwcUrl: String = "https://rollcallsystem-kea.azurewebsites.net/api/JWTTokens"
    private val userUrl: String = "https://rollcallsystem-kea.azurewebsites.net/api/Users/Self"

    suspend fun LogIn(email:String, password:String):User{
        var user: User = User()
        var token: String = ""

        @Serializable
        data class LoginData(val email:String, val password:String)

        val loginData:LoginData = LoginData(email, password)
        val jsonData:String = Json.encodeToString(loginData)

        val requestBody:RequestBody =
                RequestBody.create(MediaType.parse("application/json"), jsonData)
        var request = Request.Builder().url(jwcUrl).post(requestBody).build()
        val client = OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();
        try {
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")
                response.body()?.let { token = it.string() }
            }
        }
        catch (ex: IOException)
        {
            user.FirstName = ex.message.toString()
            return user
        }

        var userData:UserData

        request = Request.Builder().url(userUrl).addHeader("Authorization", "Bearer $token").build()

        try {
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")
                response.body()?.let {
                    userData = Json.decodeFromString(it.string())
                    user = User(userData.id, userData.email, userData.firstName, userData.lastName, token)
                    return user
                }
            }
        }
        catch (ex:IOException) {
            user.FirstName = ex.message.toString()
            return user
        }

        return user
    }

    @Serializable
    data class UserData(val id: Int, val email: String, val firstName: String,
                        val lastName: String, val roleId: Int)
}