package com.olaz.instasprite.ui.screens.googleauthscreen
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.olaz.instasprite.utils.TokenUtils
import com.olaz.instasprite.data.network.model.JwtResponse
import com.olaz.instasprite.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class GoogleAuthUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val jwt: JwtResponse? = null,
    val isFirstTime: Boolean = false
)

class GoogleAuthViewModel(
    private val repository: AuthRepository = AuthRepository(),
    private val tokenUtils: TokenUtils
) : ViewModel() {

    private val _uiState = MutableStateFlow(GoogleAuthUiState())
    val uiState: StateFlow<GoogleAuthUiState> = _uiState

    fun loginWithGoogleIdToken(idToken: String) {
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch {
            try {
                val response = repository.loginWithGoogle(idToken)
                val jwt = response.data
                if (jwt != null) {
                    tokenUtils.saveTokens(
                        accessToken = jwt.accessToken,
                        refreshToken = jwt.refreshToken,
                        tokenType = jwt.type
                    )
                    
                    _uiState.value = GoogleAuthUiState(
                        isLoading = false, 
                        jwt = jwt,
                        isFirstTime = jwt.isFirstTime ?: false
                    )
                } else {
                    _uiState.value = GoogleAuthUiState(
                        isLoading = false,
                        errorMessage = "Empty response"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = GoogleAuthUiState(
                    isLoading = false,
                    errorMessage = e.message ?: "Unknown error"
                )
            }
        }
    }
}


