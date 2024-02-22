package com.example.icook.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
    Profile,
}

@Composable
fun ICookApp(
    viewModel: ICookViewModel = viewModel(),
    navController : NavHostController = rememberNavController()
) {
    // A surface container using the 'background' color from the theme
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        val userState by viewModel.userState.collectAsState()
        val formState by viewModel.formState.collectAsState()


        NavHost(
            navController = navController,
            startDestination = ICookScreen.SignUp.name
        ){
            composable(route = ICookScreen.SignUp.name){
                SignUpScreen(
                    user = userState,
                    signUpState = formState,
                    onUsernameChange = { newUsername -> viewModel.onUsernameChange(newUsername) },
                    onNameChange = {newName -> viewModel.onNameChange(newName)},
                    onLastNameChange = {newLastName -> viewModel.onLastNameChange(newLastName)},
                    onShowHidePasswordButtonClicked = {viewModel.onShowHidePasswordButtonClicked()},
                    onPasswordChange = {currentPassword, newValue -> viewModel.onPasswordChange(currentPassword,newValue)},
                    onSignUpButtonClicked = {viewModel.onSignUpButtonClicked(navController)},
                    onLogInTextButtonClicked = { switchTo(navController, ICookScreen.LogIn)}
                )
            }
            composable(route = ICookScreen.LogIn.name){
                LogInScreen(
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
                    onProfileButtonClicked = { switchTo(navController, ICookScreen.Profile)},
                    onCreatePostButtonClicked = { switchTo(navController, ICookScreen.CreatePost) }
                )
            }
            composable(route = ICookScreen.CreatePost.name) {
                CreatePostScreen(
                    onHomeButtonClicked = { switchTo(navController, ICookScreen.Home) },
                    onProfileButtonClicked = { switchTo(navController, ICookScreen.Profile) }
                )
            }
            composable(route = ICookScreen.Profile.name) {
                ProfileScreen(
                    onHomeButtonClicked = { switchTo(navController, ICookScreen.Home) },
                    onCreatePostButtonClicked = { switchTo(navController, ICookScreen.CreatePost)}
                )
            }

        }
    }
}

@SuppressLint("RestrictedApi")
fun switchTo(navController: NavHostController, screen: ICookScreen) {
    navController.popBackStack(ICookScreen.Home.name, inclusive = false)
    if (navController.currentBackStack.value[1].destination.route != screen.name) {
        navController.navigate(screen.name)
    }
}

@SuppressLint("RestrictedApi")
fun switchToHome(navController: NavHostController) {
    switchTo(navController, ICookScreen.Home)
}