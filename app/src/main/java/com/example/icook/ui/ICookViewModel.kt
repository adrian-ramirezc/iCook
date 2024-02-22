package com.example.icook.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.icook.data.models.SimpleMessage
import com.example.icook.data.models.User
import com.example.icook.network.ICookApi
import com.example.icook.utils.hashPassword
import com.example.icook.utils.verifyPassword
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response


const val TAG = "ICookViewModel"

data class FormState(
    val isUsernameError: Boolean = false,
    val isNameError: Boolean = false,
    val isLastNameError: Boolean = false,
    val isPasswordError: Boolean = false,
    val isPasswordVisible: Boolean = false,
    val isValidating: Boolean = false,
)

class ICookViewModel : ViewModel() {
    private val _userState = MutableStateFlow(User())
    val userState: StateFlow<User> = _userState.asStateFlow()

    private val _formState = MutableStateFlow(FormState())
    val formState: StateFlow<FormState> = _formState.asStateFlow()

    fun onUsernameChange(newUsername: String) {
        _userState.update {
            it.copy(
            username = newUsername
        )
        }
        _formState.update{
            it.copy(
                isUsernameError = false,
            )
        }
    }
    fun onNameChange(newName: String) {
        _userState.update {
            it.copy(
                name = newName
            )
        }
        _formState.update{ state ->
            state.copy(
               isNameError = false,
            )
        }
    }
    fun onLastNameChange(newLastname: String) {
        _userState.update {
            it.copy(
                lastname = newLastname
            )
        }
        _formState.update{ state ->
            state.copy(
                isLastNameError = false,
            )
        }
    }
    fun onPasswordChange(currentPassword: String, newPassword: String) {
        var realNewPassword: String = ""
        if (newPassword.length < currentPassword.length) {
            realNewPassword = currentPassword.substring(
                startIndex = 0,
                endIndex = newPassword.length
            )
        } else {
            val justAdded = newPassword.substring(currentPassword.length)
            realNewPassword = currentPassword + justAdded

        }
        _userState.update {
            it.copy(
                password = realNewPassword
            )
        }
        _formState.update{ state ->
            state.copy(
                isPasswordError = false,
            )
        }
    }

    fun onShowHidePasswordButtonClicked() {
        _formState.update {
            it.copy(
                isPasswordVisible = !formState.value.isPasswordVisible
            )
        }
    }

    private fun isUsernameInvalid(): Boolean {
        return userState.value.username.isBlank() or (userState.value.username.length < 8)
    }

    private fun isNameInvalid(): Boolean {
        return userState.value.name.isBlank()
    }

    private fun isLastNameInvalid(): Boolean {
        return userState.value.lastname.isBlank()
    }

    private fun isPasswordInvalid(): Boolean {
        return userState.value.password.isBlank() or (userState.value.password.length < 3)
    }

    private fun setIsValidating(value: Boolean) {
        _formState.update {
            it.copy(
                isValidating = value
            )
        }
    }

    private fun isValidSignUpForm(): Boolean {
        var isValid = true
        if (isUsernameInvalid()) {
            isValid = false
            _formState.update {
                it.copy(
                    isUsernameError = true
                )
            }
        }
        if (isNameInvalid()) {
            isValid = false
            _formState.update {
                it.copy(
                    isNameError = true
                )
            }
        }
        if (isLastNameInvalid()) {
            isValid = false
            _formState.update {
                it.copy(
                    isLastNameError = true
                )
            }
        }
        if (isPasswordInvalid()) {
            isValid = false
            _formState.update {
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

    private fun isValidLogInForm(): Boolean {
        var isValid = true
        if (isUsernameInvalid()) {
            isValid = false
            _formState.update {
                it.copy(
                    isUsernameError = true
                )
            }
        }
        if (isPasswordInvalid()) {
            isValid = false
            _formState.update {
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
        if (!isValidSignUpForm()) {
            Log.d(TAG, "Sign Up Form is not valid")
            setIsValidating(value = false)
        } else {
            tryCreateUser(user = userState.value, navController=navController)
        }
    }

    fun onLogInButtonClicked(navController: NavHostController) {
        setIsValidating(value = true)
        if (!isValidLogInForm()) {
            Log.d(TAG, "Log In Form is not valid")
            setIsValidating(value = false)
        } else {
            tryLogInUser(user = userState.value, navController = navController)
        }
    }

    private fun tryLogInUser(user: User, navController: NavHostController) {
        viewModelScope.launch(Dispatchers.Main) {
            val userResponse = getUser(username = user.username)
            setIsValidating(value = false)
            if (userResponse.code() == 200) {
                Log.d(TAG, "User found")
                if (verifyPassword(user.password, userResponse.body()!!.password)) {
                    switchToHome(navController)
                } else {
                    Log.d(TAG, "Incorrect Password")
                }
            } else {
                Log.d(TAG, "User not found")
            }
        }
    }

    private fun tryCreateUser(user: User, navController: NavHostController) {
        viewModelScope.launch(Dispatchers.Main) {
            val userResponse = getUser(username = user.username)
            setIsValidating(value = false)
            if (userResponse.isSuccessful) {
                Log.d(TAG, "Username already exists" )
            } else {
                Log.d(TAG, "Username is available")
                val createResponse = createUser(user = userState.value)
                if (createResponse.isSuccessful) {
                    switchToHome(navController)
                } else {
                    Log.e(TAG, "User was not created: $createResponse")
                }
            }

        }
    }

    private suspend fun getUser(username : String): Response<User> = withContext(Dispatchers.IO) {
        return@withContext ICookApi.retrofitService.getUser(username)
    }

    private suspend fun createUser(user : User): Response<SimpleMessage> = withContext(Dispatchers.IO) {
        val hashedUser = user.copy(password = hashPassword(user.password))
        return@withContext ICookApi.retrofitService.createUser(hashedUser)
    }
}