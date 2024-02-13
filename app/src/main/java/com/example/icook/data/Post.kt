package com.example.icook.data

import java.sql.Timestamp

class Post(
    val author: User = User(),
    val picture: String = "",
    val likes_counter: Int = 999,
    val description: String = "This is a description",
    val comments: List<Comment> = listOf(),
    val date_created: Timestamp = Timestamp(102323),
) {
}