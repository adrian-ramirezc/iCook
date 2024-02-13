package com.example.icook.data

data class SignUpState(
    val username: String = "",
    val isUsernameError: Boolean = false,
    val name: String = "",
    val isNameError: Boolean = false,
    val lastName: String = "",
    val isLastNameError: Boolean = false,
    val password: String = "",
    val isPasswordError: Boolean = false,
    val isPasswordVisible: Boolean = false,
) {

}