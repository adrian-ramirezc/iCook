package com.example.icook.ui

import android.content.ContentResolver
import android.net.Uri
import android.util.Log
import androidx.compose.material3.SnackbarDuration
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.icook.ICookState
import com.example.icook.data.models.Post
import com.example.icook.data.models.PostWithUser
import com.example.icook.data.models.RawPost
import com.example.icook.data.models.SimpleMessage
import com.example.icook.data.models.User
import com.example.icook.data.models.UserPostOptions
import com.example.icook.data.models.UserToUpdate
import com.example.icook.network.ICookApi
import com.example.icook.utils.hashPassword
import com.example.icook.utils.uriToBase64
import com.example.icook.utils.verifyPassword
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.time.LocalDateTime


const val TAG = "ICookViewModel"

data class FormState(
    val isUsernameError: Boolean = false,
    val isNameError: Boolean = false,
    val isLastNameError: Boolean = false,
    val isPasswordError: Boolean = false,
    val isPasswordVisible: Boolean = false,
    val isValidating: Boolean = false,
)

data class ProfileScreenState(
    val userPictureScreenEnabled: Boolean = false,
)
class ProfileScreenStateFlow{
    private val _profileScreenState = MutableStateFlow(ProfileScreenState())
    val profileScreenState: StateFlow<ProfileScreenState> = _profileScreenState.asStateFlow()

    fun setUserPictureScreenEnabled(value: Boolean) {
        _profileScreenState.update {
            it.copy(
                userPictureScreenEnabled = value
            )
        }
    }
}

class ICookViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ICookState())
    val uiState: StateFlow<ICookState> = _uiState.asStateFlow()

    private val _userState = MutableStateFlow(User())
    val userState: StateFlow<User> = _userState.asStateFlow()

    private val _newRawPostState = MutableStateFlow(RawPost())
    val newRawPostState: StateFlow<RawPost> = _newRawPostState.asStateFlow()

    private val _formState = MutableStateFlow(FormState())
    val formState: StateFlow<FormState> = _formState.asStateFlow()

    val profileScreenSF = ProfileScreenStateFlow()

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

    private fun setIsValidatingForm(value: Boolean) {
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
        setIsValidatingForm(value = true)
        if (!isValidSignUpForm()) {
            viewModelScope.launch(Dispatchers.Main) {
                showSnackbarMessage(message = "Invalid Form")
            }
            Log.d(TAG, "Sign Up Form is not valid")
            setIsValidatingForm(value = false)
        } else {
            tryCreateUser(user = userState.value, navController=navController)
        }
    }

    fun onLogInButtonClicked(navController: NavHostController) {
        setIsValidatingForm(value = true)
        if (!isValidLogInForm()) {
            viewModelScope.launch(Dispatchers.Main) {
                showSnackbarMessage(message = "Invalid Form")
            }
            Log.d(TAG, "Log In Form is not valid")
            setIsValidatingForm(value = false)
        } else {
            tryLogInUser(user = userState.value, navController = navController)
        }
    }

    private fun tryLogInUser(user: User, navController: NavHostController) {
        viewModelScope.launch(Dispatchers.Main) {
            val userResponse = getUser(username = user.username)
            setIsValidatingForm(value = false)
            if (userResponse.code() == 200) {
                Log.d(TAG, "User found")
                if (verifyPassword(user.password, userResponse.body()!!.password)) {
                    _userState.update { userResponse.body()!!.copy(password = "") } // Do not expose user password
                    switchToHome(navController=navController)
                } else {
                    showSnackbarMessage(message = "Wrong Password")
                    Log.d(TAG, "Incorrect Password")
                }
            } else {
                showSnackbarMessage(message = "User not found")
                Log.d(TAG, "User not found")
            }
        }
    }

    private fun tryCreateUser(user: User, navController: NavHostController) {
        viewModelScope.launch(Dispatchers.Main) {
            val userResponse = getUser(username = user.username)
            setIsValidatingForm(value = false)
            if (userResponse.isSuccessful) {
                Log.d(TAG, "Username already exists" )
            } else {
                Log.d(TAG, "Username is available")
                val createResponse = createUser(user = userState.value)
                if (createResponse.isSuccessful) {
                    switchToHome(navController=navController)
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

    fun updateNewRawPostUri(newUri: Uri?){
        if (newUri != null) {
            _newRawPostState.update {
                it.copy(
                    uri = newUri
                )
            }
        }
    }

    fun onDescriptionChange(newDescription: String) {
        _newRawPostState.update {
            it.copy(
                description = newDescription
            )
        }
    }

    private fun setIsValidatingNewPost(value : Boolean) {
        _newRawPostState.update {
            it.copy(
                isValidating = value
            )
        }
    }

    private suspend fun showSnackbarMessage(message: String){
        uiState.value.snackbarHostState.showSnackbar(
            message=message,
            withDismissAction = true,
            duration = SnackbarDuration.Short
        )
    }

    fun onCreateNewPostClicked(contentResolver: ContentResolver, navController: NavHostController){
        viewModelScope.launch {
            if (newRawPostState.value.uri == Uri.parse("")) {
                Log.d(TAG, "No picture was selected")
                showSnackbarMessage(message = "No picture was selected")
            } else if (newRawPostState.value.description.isBlank()) {
                Log.d(TAG, "Post has no description")
                showSnackbarMessage(message = "Post has no description")
            } else {
                val picture = uriToBase64(
                    uri = newRawPostState.value.uri,
                    contentResolver = contentResolver!!
                )
                var post = Post(
                    username = userState.value.username,
                    description = newRawPostState.value.description,
                    picture = picture!!,
                    date = LocalDateTime.now()
                )
                setIsValidatingNewPost(value = true)
                val response = createPost(post)
                setIsValidatingNewPost(value = false)
                if (response.isSuccessful) {
                    Log.d(TAG, "Post created")
                    switchToProfile(navController)
                } else {
                    showSnackbarMessage(message = "Post could not be created")
                    Log.d(TAG, "Post could not be created")
                }
            }
        }
    }
    private suspend fun createPost(post: Post): Response<SimpleMessage> = withContext(Dispatchers.IO) {
        return@withContext ICookApi.retrofitService.createPost(post)
    }

    fun persistNewUserDescription(newDescription: String){
        viewModelScope.launch {
            updateUser(username = userState.value.username, mapOf("description" to newDescription))
        }
    }

    fun persistNewUserProfilePicture(uri: Uri?, contentResolver: ContentResolver){
        viewModelScope.launch {
            if (uri == null) {
                showSnackbarMessage(message = "No picture selected")
            } else {
                var picture = uriToBase64(uri=uri, contentResolver = contentResolver)
                var responseUpdateUser = updateUser(username = userState.value.username, mapOf("picture" to picture!!))
                if (responseUpdateUser.isSuccessful){
                    profileScreenSF.setUserPictureScreenEnabled(false)
                    fetchUser()
                } else {
                    showSnackbarMessage(message = "Picture was not updated")
                }
            }
        }
    }

    private suspend fun updateUser(username: String, keys: Map<String, String> = mapOf()): Response<SimpleMessage> = withContext(Dispatchers.IO) {
        var userToUpdate = UserToUpdate(username=username)

        for ((key, value) in keys) {
            when (key) {
                "description" -> userToUpdate = userToUpdate.copy(description = value)
                "picture" -> userToUpdate = userToUpdate.copy(picture = value)
            }
        }
        return@withContext ICookApi.retrofitService.updateUser(userToUpdate)
    }

    private fun fetchUser(){
        viewModelScope.launch{
            val responseUser = getUser(username = userState.value.username)
            if (responseUser.isSuccessful) {
                _userState.update {responseUser.body()!!}
            }
        }
    }

    private fun fetchUserPosts(){
        viewModelScope.launch{
            var postsResponse = getUserPosts(username = userState.value.username)
            if (postsResponse.isSuccessful) {
                var posts = postsResponse.body()
                _uiState.update {
                    it.copy(
                        userPosts = posts!!
                    )
                }
            }
        }
    }
    fun switchToProfile(navController: NavHostController){
        fetchUserPosts()
        switchTo(navController, ICookScreen.Profile)
    }

    private fun fetchFeedPostsWithUsers(){
        viewModelScope.launch{
            var postsResponse = getFeedPostsWithUsers(username = userState.value.username)
            if (postsResponse.isSuccessful) {
                var postsWithUsers = postsResponse.body()
                _uiState.update {
                    it.copy(
                        feedPostsWithUsers = postsWithUsers!!
                    )
                }
            }
        }
    }

    fun switchToHome(navController: NavHostController){
        fetchFeedPostsWithUsers()
        switchTo(navController, ICookScreen.Home)
    }

    private suspend fun getUserPosts(username: String): Response<List<Post>> = withContext(Dispatchers.IO) {
        return@withContext ICookApi.retrofitService.getUserPosts(username)
    }

    private suspend fun getFeedPostsWithUsers(username: String): Response<List<PostWithUser>> = withContext(Dispatchers.IO) {
        return@withContext ICookApi.retrofitService.getFeedPostsWithUsers(username)
    }

    fun onPostOptionClicked(post: Post, postOption: UserPostOptions){
        if (postOption == UserPostOptions.Delete) {
            viewModelScope.launch {
                deletePost(post.id!!)
                fetchUserPosts()
            }
        }
    }

    private suspend fun deletePost(postId: Int): Response<SimpleMessage> = withContext(Dispatchers.IO) {
        return@withContext ICookApi.retrofitService.deletePost(postId)
    }
}