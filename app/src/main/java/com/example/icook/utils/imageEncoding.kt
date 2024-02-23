package com.example.icook.utils

import android.content.ContentResolver
import android.net.Uri
import java.io.IOException
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@OptIn(ExperimentalEncodingApi::class)
fun uriToBase64(uri: Uri, contentResolver: ContentResolver): String? {
    return try {
        val inputStream = contentResolver.openInputStream(uri)
        val bytes = inputStream?.readBytes()
        Base64.encode(bytes!!)
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}