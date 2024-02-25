package com.example.icook.ui

import android.net.Uri
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.icook.data.models.Post
import com.example.icook.data.models.User
import com.example.icook.ui.screens.CreatePostScreen
import com.example.icook.ui.screens.HomeScreen
import com.example.icook.ui.screens.LogInScreen
import com.example.icook.ui.screens.ProfileScreen
import com.example.icook.ui.screens.SignUpScreen

enum class ICookScreen{
    SignUp,
    LogIn,
    Home,
    CreatePost,
    MyProfile,
    OtherProfile,
}

@Composable
fun ICookApp(
    viewModel: ICookViewModel = viewModel(),
    navController : NavHostController = rememberNavController()
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        val userState by viewModel.userState.collectAsState()
        val newRawPostState by viewModel.newRawPostState.collectAsState()
        val formState by viewModel.formState.collectAsState()
        val uiState by viewModel.uiState.collectAsState()
        val profileScreenState by viewModel.profileScreenSF.state.collectAsState()
        val otherProfileScreenState by viewModel.otherProfileScreenSF.state.collectAsState()

        val contentResolver = LocalContext.current.contentResolver

        NavHost(
            navController = navController,
            startDestination = ICookScreen.SignUp.name
        ){
            composable(route = ICookScreen.SignUp.name){
                SignUpScreen(
                    snackbarHostState = uiState.snackbarHostState,
                    user = userState,
                    signUpState = formState,
                    onUsernameChange = { newUsername -> viewModel.onUsernameChange(newUsername) },
                    onNameChange = {newName -> viewModel.onNameChange(newName)},
                    onLastNameChange = {newLastName -> viewModel.onLastNameChange(newLastName)},
                    onShowHidePasswordButtonClicked = {viewModel.onShowHidePasswordButtonClicked()},
                    onPasswordChange = {currentPassword, newValue -> viewModel.onPasswordChange(currentPassword,newValue)},
                    onSignUpButtonClicked = {viewModel.onSignUpButtonClicked(navController)},
                    onLogInTextButtonClicked = { viewModel.switchTo(navController, ICookScreen.LogIn) }
                )
            }
            composable(route = ICookScreen.LogIn.name){
                LogInScreen(
                    snackbarHostState = uiState.snackbarHostState,
                    user = userState,
                    logInState = formState,
                    onUsernameChange = { newUsername -> viewModel.onUsernameChange(newUsername) },
                    onShowHidePasswordButtonClicked = {viewModel.onShowHidePasswordButtonClicked()},
                    onPasswordChange = {currentPassword, newValue -> viewModel.onPasswordChange(currentPassword,newValue)},
                    onLogInButtonClicked = {viewModel.onLogInButtonClicked(navController)},
                )
            }
            composable(route = ICookScreen.Home.name){
                HomeScreen(
                    user = userState,
                    feedPostsWithUsers = uiState.feedPostsWithUsers,
                    postsWithComments = uiState.postWithComments,
                    onProfileButtonClicked = { viewModel.switchToProfile(navController=navController) },
                    onCreatePostButtonClicked = { viewModel.switchTo(navController, ICookScreen.CreatePost) },
                    onOtherUserPictureClicked = {otherUser: User -> viewModel.onOtherUserPictureClicked(navController=navController, otherUser=otherUser)},
                    onCreateNewCommentButtonClicked = {newCommentText: String, post: Post -> viewModel.onCreateNewCommentButtonClicked(newCommentText,post) },
                    onViewAllCommentsClicked = {post: Post -> viewModel.onViewAllCommentsClicked(post)},
                    onLikePostClicked = {postId: Int, isPostLiked: Boolean -> viewModel.onLikePostClicked(postId, isPostLiked)}
                )
            }
            composable(route = ICookScreen.CreatePost.name) {
                CreatePostScreen(
                    snackbarHostState = uiState.snackbarHostState,
                    newRawPostState = newRawPostState,
                    onDescriptionChange = {newValue -> viewModel.onDescriptionChange(newValue)},
                    updateNewRawPostUri = {uri: Uri? -> viewModel.updateNewRawPostUri(uri)},
                    onCreateNewPostClicked = {viewModel.onCreateNewPostClicked(contentResolver = contentResolver, navController = navController)},
                    onHomeButtonClicked = { viewModel.switchToHome(navController=navController)  },
                    onProfileButtonClicked = {viewModel.switchToProfile(navController=navController) }
                )
            }
            composable(route = ICookScreen.MyProfile.name) {
                ProfileScreen(
                    user = userState,
                    posts = uiState.userPosts,
                    postsWithComments = uiState.postWithComments,
                    snackbarHostState = uiState.snackbarHostState,
                    userPictureScreenEnabled = profileScreenState.userPictureScreenEnabled,
                    setUserPictureScreenEnabled = {value: Boolean -> viewModel.profileScreenSF.setUserPictureScreenEnabled(value)},
                    onHomeButtonClicked = { viewModel.switchToHome(navController=navController)  },
                    onCreatePostButtonClicked = { viewModel.switchTo(navController, ICookScreen.CreatePost)},
                    persistNewUserDescription = {newDescription: String -> viewModel.persistNewUserDescription(newDescription)},
                    onPostOptionClicked = {post, postOption -> viewModel.onPostOptionClicked(post, postOption)},
                    onUpdateUserPictureClicked = {uri: Uri? ->  viewModel.persistNewUserProfilePicture(uri = uri, contentResolver=contentResolver)},
                    onLogOutButtonClicked = {viewModel.onLogOutButtonClicked(navController)},
                    onCreateNewCommentButtonClicked = {newCommentText: String, post: Post -> viewModel.onCreateNewCommentButtonClicked(newCommentText,post) },
                    onViewAllCommentsClicked = {post: Post -> viewModel.onViewAllCommentsClicked(post)},
                    onLikePostClicked = {postId: Int, isPostLiked: Boolean -> viewModel.onLikePostClicked(postId, isPostLiked)}
                    )
            }
            composable(route = ICookScreen.OtherProfile.name) {
                ProfileScreen(
                    isMyProfile = false,
                    user = otherProfileScreenState.user,
                    posts = otherProfileScreenState.userPosts,
                    postsWithComments = uiState.postWithComments,
                    onHomeButtonClicked = { viewModel.switchToHome(navController=navController)  },
                    onCreatePostButtonClicked = { viewModel.switchTo(navController, ICookScreen.CreatePost)},
                    onProfileButtonClicked = {viewModel.switchToProfile(navController=navController) },
                    onCreateNewCommentButtonClicked = {newCommentText: String, post: Post -> viewModel.onCreateNewCommentButtonClicked(newCommentText,post) },
                    onViewAllCommentsClicked = {post: Post -> viewModel.onViewAllCommentsClicked(post)},
                    onLikePostClicked = {postId: Int, isPostLiked: Boolean -> viewModel.onLikePostClicked(postId, isPostLiked)}
                )
            }

        }
    }
}