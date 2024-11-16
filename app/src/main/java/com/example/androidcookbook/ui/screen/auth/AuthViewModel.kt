package com.example.androidcookbook.ui.screen.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.androidcookbook.CookbookApplication
import com.example.androidcookbook.data.AuthRepository
import com.example.androidcookbook.model.auth.RegisterRequest
import com.example.androidcookbook.model.auth.SignInRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun ChangeOpenDialog(open: Boolean) {
        _uiState.update {
            it.copy(
                openDialog = open
            )
        }
    }

    fun ChangeDialogMessage(message: String) {
        _uiState.update {
            it.copy(
                dialogMessage = message
            )
        }
    }

    fun SignInSuccess() {
        _uiState.update {
            it.copy(
                signInSuccess = true
            )
        }
    }

    fun SignUp(req: RegisterRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = authRepository.register(req)
            ChangeOpenDialog(true)
            if (response.isSuccessful) {
                // Trường hợp đăng ký thành công
                val registerResponse = response.body()
                Log.d("Register", "Success: ${registerResponse?.message}")
                registerResponse?.message?.let { ChangeDialogMessage(it) }
            } else {
                Log.e("Register", "Error: ${response}")
                ChangeDialogMessage(response.toString())
            }
        }
    }

    fun SignIn(req: SignInRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = authRepository.login(req)

                ChangeOpenDialog(true)
                if (response.isSuccessful) {
                    // Trường hợp đăng ký thành công
                    val signInResponse = response.body()
                    Log.d("Login", "Success: ${signInResponse?.message}")
                    SignInSuccess()
                    signInResponse?.message?.let { ChangeDialogMessage(it) }
                } else {
                    Log.e("Login", "Test: $response")
                    ChangeDialogMessage(response.code().toString())
                }
            } catch (e: SocketTimeoutException) {
                Log.e("Login", e.toString())
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as CookbookApplication)
                val authRepository = application.authContainer.repository as AuthRepository
                AuthViewModel(authRepository = authRepository)
            }
        }
    }
}