package com.example.icook

import androidx.compose.material3.SnackbarHostState
import com.example.icook.data.models.Comment
import com.example.icook.data.models.Post
import com.example.icook.data.models.PostWithUser
import com.example.icook.data.models.User

data class ICookState(
    val snackbarHostState : SnackbarHostState = SnackbarHostState(),
    val userPosts: List<Post> = listOf(),
    val feedPostsWithUsers: List<PostWithUser> = listOf(),
    val postWithComments: Map<Post, List<Comment>> = mapOf(),
)