package com.example.icook

import androidx.compose.material3.SnackbarHostState
import com.example.icook.data.models.Post

data class ICookState(
    val snackbarHostState : SnackbarHostState = SnackbarHostState(),
    val userPosts: List<Post> = listOf(),
    val feedPosts: List<Post> = listOf(),
)