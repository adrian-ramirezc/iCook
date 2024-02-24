package com.example.icook.network

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.icook.data.models.Post
import com.example.icook.data.models.SimpleMessage
import com.example.icook.data.models.User
import com.example.icook.data.models.UserToUpdate
import com.example.icook.utils.LocalDateTimeDeserializer
import com.google.gson.GsonBuilder
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE

import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import java.time.LocalDateTime

private const val BASE_URL = "http://192.168.27.168:5000"
//private const val BASE_URL = "http://10.0.2.2:5000"
/**
 * when debugging/testing:
 *  - from emulator: "http://10.0.2.2:PORT_NUMBER"
 *  - from android phone (via usb connection): "http://192.168.27.168:PORT_NUMBER"
 *      Tip: Turn on mobile hotspot and connect computer to that network
 *      In computer: $ ifconfig
 *      And look for you public address, usually: 192.168.XX.XX
 *      Make backend host: 0.0.0.0
 * **/

val gson = GsonBuilder()
    .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeDeserializer())
    .create()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create(gson))
    .baseUrl(BASE_URL)
    .build()

interface ICookApiService {
    @POST("users/create")
    suspend fun createUser(@Body requestBody: User): Response<SimpleMessage>

    @PUT("users/update")
    suspend fun updateUser(@Body requestBody: UserToUpdate): Response<SimpleMessage>

    @GET("users/{username}")
    suspend fun getUser(@Path("username") username: String): Response<User>

    @POST("posts/create")
    suspend fun createPost(@Body requestBody: Post): Response<SimpleMessage>

    @GET("posts/{username}")
    suspend fun getUserPosts(@Path("username") username: String): Response<List<Post>>

    @GET("posts/feed/{username}")
    suspend fun getFeedPosts(@Path("username") username: String): Response<List<Post>>

    @DELETE("posts/delete/{id}")
    suspend fun deletePost(@Path("id") id: Int): Response<SimpleMessage>
}


/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */
object ICookApi {
    val retrofitService: ICookApiService by lazy {
        retrofit.create(ICookApiService::class.java)
    }
}