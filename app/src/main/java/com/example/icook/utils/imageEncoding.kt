package com.example.icook.utils

import android.content.ContentResolver
import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
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

@OptIn(ExperimentalEncodingApi::class)
fun loadImageFromBase64(context: Context, base64Image: String): Painter {
    val decodedBytes = Base64.decode(base64Image)
    val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    return BitmapPainter(bitmap!!.asImageBitmap())
}