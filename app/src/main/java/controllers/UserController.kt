package controllers

import android.util.Log
import kotlinx.serialization.Serializable
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
    private val userUrl: String = "https://rollcallsystem-kea.azurewebsites.net/api/JWTTokens"

    suspend fun LogIn(email:String, password:String):User{
        var user: User = User()

        @Serializable
        data class LoginData(val email:String, val password:String)

        val loginData:LoginData = LoginData(email, password)

        val jsonAnon:String = Json.encodeToString(loginData)

        val requestBody:RequestBody =
                RequestBody.create(MediaType.parse("application/json"), jsonAnon)
        var request = Request.Builder().url(jwcUrl).post(requestBody).build()
        val client = OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")
            response.body()?.let { Log.d("kek", it.string()) }
        }

        //request = Request.Builder().url()

        return user


    }
}