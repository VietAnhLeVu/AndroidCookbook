package com.example.androidcookbook.ui.screen.auth

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androidcookbook.model.auth.SignInRequest
import com.example.androidcookbook.model.auth.RegisterRequest

@Composable
fun SignBackground(
    viewModel: SignViewModel,
    isSignIn: Boolean,
    isOpenDialog: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFF251404))
            .verticalScroll(rememberScrollState())
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawOval(
                color = Color(0xFF4F3423),
                topLeft = Offset(size.width * (-0.1f), size.height / (-14)),
                size = Size(size.width * 1.2f, size.height / 4)
            )
        }

        Column(
            modifier = Modifier
                .wrapContentWidth()
                .fillMaxHeight()
                .padding(start = 11.dp, top = 11.dp, end = 11.dp, bottom = 11.dp)
                .align(Alignment.TopCenter)
                .offset(y = 70.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(50.dp)
        ) {
            AppLogo()

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                if (isSignIn) {
                    SignInCompose(
                        onSignUpClick = {viewModel.ChangeInOrUp(false)},
                        onForgotPasswordClick = {},
                        viewModel
                    )
                } else {
                    SignUpCompose(
                        onSignInClick = {viewModel.ChangeInOrUp(true)},
                        viewModel = viewModel
                    )
                }
            }
        }
        if (isOpenDialog) {
            MinimalDialog(
                dialogMessage = viewModel.uiState.value.dialogMessage,
                onDismissRequest = {
                    viewModel.ChangeOpenDialog(false)
                    if (viewModel.uiState.value.signInSuccess) {
                        viewModel.SignInApp()
                    }
                }
            )
        }
    }
}



@Composable
fun SignInCompose(
    onSignUpClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    viewModel: SignViewModel
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    SignInComponents(
        username = username,
        onTypeUsername = {
            username = it
        },
        password = password,
        onTypePassword = {
            password = it
        },
        onSignInClick = { viewModel.SignIn(SignInRequest(username, password)) }
    )

    ClickableSeparatedText(
        unclickableText = "Doesn’t have account ? ",
        clickableText = "Sign Up",
        onClick = onSignUpClick
    )
    ClickableSeparatedText(
        unclickableText = "",
        clickableText = "Forgot password ?",
        onClick = onForgotPasswordClick
    )
}

@Composable
fun SignUpCompose(
    onSignInClick: () -> Unit,
    viewModel: SignViewModel
) {
    var email by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var repassword by remember { mutableStateOf("") }
    SignUpComponents(
        email = email,
        onTypeEmail = {
            email = it
        },
        username = username,
        onTypeUsername = {
            username = it
        },
        password = password,
        onTypePassword = {
            password = it
        },
        repassword = repassword,
        onRetypePassword = {
            repassword = it
        },
        onSignUpClick = {
            if (password == repassword) {
                viewModel.SignUp(RegisterRequest(username, password, email))
            } else {
                viewModel.ChangeOpenDialog(true)
                viewModel.ChangeDialogMessage("Retype password not correct")
            }
        }
    )

    ClickableSeparatedText(
        unclickableText = "Already have an account ? ",
        clickableText = "Sign In",
        onClick = onSignInClick
    )
}

@Preview
@Composable
fun SignPreview() {
    val viewModel: SignViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()
    SignBackground(viewModel = viewModel, uiState.isSignIn, uiState.openDialog)
}


@Preview
@Composable
fun TextPreview() {
    ClickableSeparatedText(
        unclickableText = "Doesn’t have account ?",
        clickableText = "Sign Up"
    ) {
        
    }
}