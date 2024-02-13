package com.example.icook.network

import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.Retrofit

import retrofit2.http.GET

private const val BASE_URL = "http://192.168.27.168:8080"
/**
 * when debugging/testing:
 *  - from emulator: "http://10.0.2.2:PORT_NUMBER"
 *  - from android phone (via usb connection): "http://192.168.27.168:PORT_NUMBER"
 *      Tip: Turn on mobile hotspot and connect computer to that network
 *      In computer: $ ifconfig
 *      And look for you public address, usually: 192.168.XX.XX
 * **/


private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()


interface ICookApiService {
    @GET("photos")
    suspend fun getVerses(): String
}


/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */
object ICookApi {
    val retrofitService: ICookApiService by lazy {
        retrofit.create(ICookApiService::class.java)
    }
}