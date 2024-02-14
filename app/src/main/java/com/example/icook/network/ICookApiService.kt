package com.example.icook.network

import com.example.icook.data.User
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body

import retrofit2.http.GET
import retrofit2.http.POST

private const val BASE_URL = "http://192.168.27.168:5000"
//private const val BASE_URL = "http://10.0.2.2:5000"
/**
 * when debugging/testing:
 *  - from emulator: "http://10.0.2.2:PORT_NUMBER"
 *  - from android phone (via usb connection): "http://192.168.27.168:PORT_NUMBER"
 *      Tip: Turn on mobile hotspot and connect computer to that network
 *      In computer: $ ifconfig
 *      And look for you public address, usually: 192.168.XX.XX
 * **/


private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()



interface ICookApiService {
    @POST("signup")
    suspend fun signUpUser(@Body requestBody: User): String
}


/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */
object ICookApi {
    val retrofitService: ICookApiService by lazy {
        retrofit.create(ICookApiService::class.java)
    }
}