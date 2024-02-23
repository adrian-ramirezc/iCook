package com.example.icook.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.icook.data.models.User
import com.example.icook.ui.FormState
import com.example.icook.ui.components.CircularProgress
import com.example.icook.ui.components.SignUpField
import com.example.icook.ui.components.Snackbar

@Composable
fun LogInScreen(
    snackbarHostState: SnackbarHostState = SnackbarHostState(),
    onLogInButtonClicked: () -> Unit = {},
    user: User = User(),
    logInState: FormState = FormState(),
    onUsernameChange: (String) -> Unit = {},
    onPasswordChange: (String, String) -> Unit = {currentPassword: String, newValue: String -> },
    onShowHidePasswordButtonClicked: () -> Unit = {},
) {
    Column(
        modifier = if (logInState.isValidating) Modifier.padding(10.dp).blur(5.dp) else Modifier.padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "Log In",
            textAlign = TextAlign.Center,
            fontSize = 40.sp,
            modifier = Modifier.fillMaxWidth(),
        )
        Text(
            text = "Check out the new trends",
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 40.dp)
        )
        SignUpField(
            imageVector = Icons.Outlined.Create,
            value = user.username.trim(),
            onValueChange = onUsernameChange,
            labelText = "Username",
            isError = logInState.isUsernameError,
        )
        SignUpField(
            imageVector = Icons.Outlined.Lock,
            value = user.password.trim(),
            onShowHideValueChange = onPasswordChange,
            labelText = "Password",
            showHideValue = true,
            isTextVisible = logInState.isPasswordVisible,
            onShowHideButtonClicked = onShowHidePasswordButtonClicked,
            isError = logInState.isPasswordError,
        )
        Button(
            onClick = onLogInButtonClicked,
            modifier = Modifier.padding(15.dp)
        ) {
            Text(
                text = "Log In"
            )
        }
    }
    if (logInState.isValidating) {
        CircularProgress()
    }
    Snackbar(hostState = snackbarHostState)
}


@Preview(showBackground = true)
@Composable
fun LogInScreenPreview(){
    LogInScreen(logInState = FormState(isValidating = true))
}