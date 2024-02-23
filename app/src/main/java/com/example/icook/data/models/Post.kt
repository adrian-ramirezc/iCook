package com.example.icook.data.models

import android.net.Uri

data class RawPost(
    val uri: Uri = Uri.parse(""),
    val description: String = "",
    val isValidating: Boolean = false,
)
data class Post(
    val username: String = "",
    val description: String = "",
    val picture: String = "",
) {
}