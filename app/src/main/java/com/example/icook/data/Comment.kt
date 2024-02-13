package com.example.icook.data

import java.sql.Timestamp

class Comment(
    val text: String = "",
    val user: User = User(),
    val post: Post = Post(),
    val date_created: Timestamp = Timestamp(123455),
) {
}