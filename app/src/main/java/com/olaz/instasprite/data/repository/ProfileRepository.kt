package com.olaz.instasprite.data.repository

import com.olaz.instasprite.data.network.NetworkModule
import com.olaz.instasprite.data.network.model.EditProfileRequest
import com.olaz.instasprite.data.network.model.EditProfileResponse
import com.olaz.instasprite.data.network.model.ResultResponse
import com.olaz.instasprite.data.network.model.UserProfileResponse

class ProfileRepository {
    private val api = NetworkModule.profileApi

    suspend fun getCurrentUserProfile(): ResultResponse<UserProfileResponse> {
        return api.getCurrentUserProfile()
    }

    suspend fun getEditProfile(): ResultResponse<EditProfileResponse> {
        return api.getEditProfile()
    }

    suspend fun editProfile(request: EditProfileRequest): ResultResponse<String> {
        return api.editProfile(request)
    }
}

