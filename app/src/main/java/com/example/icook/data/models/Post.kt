package com.example.icook.data.models

import android.net.Uri
import java.time.LocalDateTime

data class RawPost(
    val uri: Uri = Uri.parse(""),
    val description: String = "",
    val isValidating: Boolean = false,
)
data class Post(
    val id: Int? = null,
    val username: String = "",
    val description: String = "",
    val picture: String = "",
    val date: LocalDateTime = LocalDateTime.now(),
)

data class PostWithUser(
    val post: Post,
    val user: User,
)
enum class UserPostOptions{
    Delete,
}