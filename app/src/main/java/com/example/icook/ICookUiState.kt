package com.example.icook

import com.example.icook.data.User

data class ICookUiState(
    val user : User = User(),
    val verses : String = ""
) {

}