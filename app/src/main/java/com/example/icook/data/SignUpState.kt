package com.example.icook.data

data class SignUpState(
    val user: User = User(),
    val isUsernameError: Boolean = false,
    val isNameError: Boolean = false,
    val isLastNameError: Boolean = false,
    val isPasswordError: Boolean = false,
    val isPasswordVisible: Boolean = false,
)
