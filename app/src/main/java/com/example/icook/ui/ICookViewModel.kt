package com.example.icook.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.icook.ICookUiState
import com.example.icook.data.SignUpState
import com.example.icook.network.ICookApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

const val TAG = "ICookViewModel"

class ICookViewModel(
) : ViewModel() {
    private val _signUpState = MutableStateFlow(SignUpState())
    val signUpState: StateFlow<SignUpState> = _signUpState.asStateFlow()

    private val _uiState = MutableStateFlow(ICookUiState())
    val uiState: StateFlow<ICookUiState> = _uiState.asStateFlow()

    fun onUsernameChange(newUsername: String) {
        _signUpState.update{
            it.copy(
                username = newUsername,
                isUsernameError = false,
            )
        }
    }
    fun onNameChange(newName: String) {
        _signUpState.update{
            it.copy(
                name = newName,
                isNameError = false,
            )
        }
    }
    fun onLastNameChange(newLastname: String) {
        _signUpState.update{
            it.copy(
                lastName = newLastname,
                isLastNameError = false,
            )
        }
    }
    fun onPasswordChange(currentPassword: String, newPassword: String) {
        val justAdded = newPassword.substring(currentPassword.length)
        val realNewPassword: String = currentPassword + justAdded

        _signUpState.update{
            it.copy(
                password = realNewPassword,
                isPasswordError = false,
            )
        }
    }

    fun onShowHidePasswordButtonClicked() {
        _signUpState.update {
            it.copy(
                isPasswordVisible = !signUpState.value.isPasswordVisible
            )
        }
    }

    private fun isUsernameInvalid(): Boolean {
        return signUpState.value.username.isBlank() or (signUpState.value.username.length < 8)
    }

    private fun isNameInvalid(): Boolean {
        return signUpState.value.name.isBlank()
    }

    private fun isLastNameInvalid(): Boolean {
        return signUpState.value.lastName.isBlank()
    }

    private fun isPasswordInvalid(): Boolean {
        return signUpState.value.password.isBlank() or (signUpState.value.password.length < 8)
    }

    fun isValidSignUp(): Boolean {
        var isValid: Boolean = true
        if (isUsernameInvalid()) {
            isValid = false
            _signUpState.update {
                it.copy(
                    isUsernameError = true
                )
            }
        }
        if (isNameInvalid()) {
            isValid = false
            _signUpState.update {
                it.copy(
                    isNameError = true
                )
            }
        }
        if (isLastNameInvalid()) {
            isValid = false
            _signUpState.update {
                it.copy(
                    isLastNameError = true
                )
            }
        }
        if (isPasswordInvalid()) {
            isValid = false
            _signUpState.update {
                it.copy(
                    isPasswordError = true
                )
            }
        }
        if (isValid) {
            Log.d(TAG, "Sign up is valid!")
            return true
        }
        Log.d(TAG, "Sign up invalid!")
        return false

    }

    fun getVerses() {
        viewModelScope.launch {
            try {
                val listResult = ICookApi.retrofitService.getVerses()

                _uiState.update {
                    it.copy(
                        verses = "Success: ${listResult.length} Mars photos retrieved"
                    )
                }
            } catch (e: IOException) {
                val error = e.message
                Log.d("View Model", "error")
            }
        }
    }


}