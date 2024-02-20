package com.example.icook.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.icook.data.models.SimpleMessage
import com.example.icook.data.models.User
import com.example.icook.network.ICookApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

const val TAG = "ICookViewModel"

data class ICookUiState(
    val user : User = User(),
    val isUserLoggedIn : Boolean = false
)
data class SignUpState(
    val user: User = User(),
    val isUsernameError: Boolean = false,
    val isNameError: Boolean = false,
    val isLastNameError: Boolean = false,
    val isPasswordError: Boolean = false,
    val isPasswordVisible: Boolean = false,
    val isValidating: Boolean = false,
)


class ICookViewModel : ViewModel() {
    private val _signUpState = MutableStateFlow(SignUpState())
    val signUpState: StateFlow<SignUpState> = _signUpState.asStateFlow()

    private val _uiState = MutableStateFlow(ICookUiState())
    val uiState: StateFlow<ICookUiState> = _uiState.asStateFlow()

    fun onUsernameChange(newUsername: String) {
        _signUpState.update{state ->
            state.copy(
                user = state.user.copy(
                    username = newUsername
                ),
                isUsernameError = false,
            )
        }
    }
    fun onNameChange(newName: String) {
        _signUpState.update{ state ->
            state.copy(
                user = state.user.copy(
                    name = newName
                ),    isNameError = false,
            )
        }
    }
    fun onLastNameChange(newLastname: String) {
        _signUpState.update{ state ->
            state.copy(
                user = state.user.copy(
                    lastname = newLastname
                ),
                isLastNameError = false,
            )
        }
    }
    fun onPasswordChange(currentPassword: String, newPassword: String) {
        val justAdded = newPassword.substring(currentPassword.length)
        val realNewPassword: String = currentPassword + justAdded

        _signUpState.update{ state ->
            state.copy(
                user = state.user.copy(
                    password = realNewPassword
                ),
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
        return signUpState.value.user.username.isBlank() or (signUpState.value.user.username.length < 8)
    }

    private fun isNameInvalid(): Boolean {
        return signUpState.value.user.name.isBlank()
    }

    private fun isLastNameInvalid(): Boolean {
        return signUpState.value.user.lastname.isBlank()
    }

    private fun isPasswordInvalid(): Boolean {
        return signUpState.value.user.password.isBlank() or (signUpState.value.user.password.length < 8)
    }

    private fun setIsValidating(value: Boolean) {
        _signUpState.update {
            it.copy(
                isValidating = value
            )
        }
    }

    private fun isValidSignUpForm(): Boolean {
        var isValid = true
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

    fun onSignUpButtonClicked(navController: NavHostController) {
        setIsValidating(value = true)
        validateAndCreateNewUser(navController=navController)
    }

    private fun validateAndCreateNewUser(navController: NavHostController) {
        if (isValidSignUpForm()) {
            viewModelScope.launch(Dispatchers.Main) {
                userExists(user = signUpState.value.user, navController)
            }
        } else {
            Log.d(TAG, "Sign Up Form is not valid")
            setIsValidating(value = false)
        }
    }

    private suspend fun userExists(user: User, navController: NavHostController) {
        viewModelScope.launch(Dispatchers.Main) {
            val getUserResponse = getUser(username = user.username)
            setIsValidating(value = false)
            if (getUserResponse.isSuccessful) {
                Log.d(TAG, "Username already exists" )
            } else {
                Log.d(TAG, "Username is available")
                val createResponse = createUser(user = signUpState.value.user)
                if (createResponse.isSuccessful) {
                    switchToHome(navController)
                } else {
                    Log.e(TAG, "User was not created: ${createResponse.body()}")
                }
            }

        }
    }

    private suspend fun getUser(username : String): Response<User?> = withContext(Dispatchers.IO) {
        return@withContext ICookApi.retrofitService.getUser(username)
    }

    private suspend fun createUser(user : User): Response<SimpleMessage> = withContext(Dispatchers.IO) {
        return@withContext ICookApi.retrofitService.createUser(user)
    }
}