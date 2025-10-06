package com.olaz.instasprite.ui.screens.completionprofilescreen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.olaz.instasprite.data.network.model.EditProfileRequest
import com.olaz.instasprite.data.network.model.EditProfileResponse
import com.olaz.instasprite.data.repository.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class ProfileCompletionUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val profileData: EditProfileResponse? = null,
    val isProfileUpdated: Boolean = false
)

class ProfileCompletionViewModel(
    private val repository: ProfileRepository = ProfileRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileCompletionUiState())
    val uiState: StateFlow<ProfileCompletionUiState> = _uiState

    fun loadProfileData() {
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch {
            try {
                val response = repository.getEditProfile()
                if (response.status == 200 && response.data != null) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        profileData = response.data
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = response.message ?: "Failed to load profile data"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Unknown error occurred"
                )
            }
        }
    }

    fun updateProfile(
        username: String,
        name: String,
        introduce: String?,
        email: String
    ) {
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch {
            try {
                val request = EditProfileRequest(
                    memberUsername = username,
                    memberName = name,
                    memberIntroduce = introduce,
                    memberEmail = email
                )
                val response = repository.editProfile(request)
                if (response.status == 200) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isProfileUpdated = true
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = response.message ?: "Failed to update profile"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Unknown error occurred"
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}
