package com.example.icook.data.models

data class User(
    val username: String = "",
    val name: String = "",
    val lastname: String = "",
    val description: String? = null,
    val picture: String = "",
    val password: String = "",
)

data class UserToUpdate(
    val username: String = "",
    val name: String? = null,
    val lastname: String? = null,
    val description: String? = null,
    val picture: String? = null,
    val password: String? = null,
)
