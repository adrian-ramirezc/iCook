package com.example.icook.data.models

import java.time.LocalDateTime

data class Comment(
    val id: Int? = null,
    val username: String? = null,
    val post_id: Int? = null,
    val text: String? = null,
    val date: LocalDateTime? = null,
)