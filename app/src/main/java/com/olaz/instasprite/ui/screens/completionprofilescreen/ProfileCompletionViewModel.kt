package com.olaz.instasprite.ui.screens.completionprofilescreen

import android.content.Context
import android.net.Uri
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
    val isProfileUpdated: Boolean = false,
    val selectedImageUri: Uri? = null,
    val isUploadingImage: Boolean = false,
    val imageUploadError: String? = null
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

    fun selectImage(imageUri: Uri?) {
        _uiState.value = _uiState.value.copy(
            selectedImageUri = imageUri,
            imageUploadError = null
        )
    }

    fun uploadImage(context: Context) {
        val currentState = _uiState.value
        val imageUri = currentState.selectedImageUri
        
        if (imageUri == null) {
            _uiState.value = currentState.copy(imageUploadError = "No image selected")
            return
        }

        _uiState.value = currentState.copy(
            isUploadingImage = true,
            imageUploadError = null
        )

        viewModelScope.launch {
            try {
                val response = repository.uploadProfileImage(imageUri, context)
                if (response.status == 200) {
                    _uiState.value = _uiState.value.copy(
                        isUploadingImage = false,
                        imageUploadError = null
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isUploadingImage = false,
                        imageUploadError = response.message ?: "Failed to upload image"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isUploadingImage = false,
                    imageUploadError = e.message ?: "Unknown error occurred"
                )
            }
        }
    }

    fun clearImageError() {
        _uiState.value = _uiState.value.copy(imageUploadError = null)
    }
}
