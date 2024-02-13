package com.example.icook.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.icook.data.SignUpState
import com.example.icook.ui.components.SignUpField

@Composable
fun SignUpScreen(
    onSignUpButtonClicked: () -> Unit = {},
    signUpState: SignUpState = SignUpState(),
    onUsernameChange: (String) -> Unit = {},
    onNameChange: (String) -> Unit = {},
    onLastNameChange: (String) -> Unit = {},
    onPasswordChange: (String, String) -> Unit = {currentPassword: String, newValue: String -> },
    onShowHidePasswordButtonClicked: () -> Unit = {},
) {

    Column(
        modifier = Modifier.padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "Sign up",
            textAlign = TextAlign.Center,
            fontSize = 40.sp,
            modifier = Modifier.fillMaxWidth(),
        )
        Text(
            text = "Create your iCook account",
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 40.dp)
        )
        SignUpField(
            imageVector = Icons.Outlined.Create,
            value = signUpState.username,
            onValueChange = onUsernameChange,
            labelText = "Username",
            isError = signUpState.isUsernameError,
        )
        SignUpField(
            imageVector = Icons.Outlined.AccountBox,
            value = signUpState.name,
            onValueChange = onNameChange,
            labelText = "Name",
            isError = signUpState.isNameError
        )
        SignUpField(
            imageVector = Icons.Outlined.AccountBox,
            value = signUpState.lastName,
            onValueChange = onLastNameChange,
            labelText = "Last Name",
            isError = signUpState.isLastNameError
        )
        SignUpField(
            imageVector = Icons.Outlined.Lock,
            value = signUpState.password,
            onShowHideValueChange = onPasswordChange,
            labelText = "Password",
            showHideValue = true,
            isTextVisible = signUpState.isPasswordVisible,
            onShowHideButtonClicked = onShowHidePasswordButtonClicked,
            isError = signUpState.isPasswordError,
        )
        Button(
            onClick = onSignUpButtonClicked,
            modifier = Modifier.padding(15.dp)
        ) {
            Text(
                text = "Sign Up"
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Already have an account?")
            TextButton(onClick = { /*TODO*/ }) {
                Text(text = "Login")
            }
        }
        
        
    }
}


@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview(){
    SignUpScreen()
}