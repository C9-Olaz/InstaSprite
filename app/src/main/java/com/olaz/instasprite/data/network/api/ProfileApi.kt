package com.olaz.instasprite.data.network.api

import com.olaz.instasprite.data.network.model.EditProfileRequest
import com.olaz.instasprite.data.network.model.EditProfileResponse
import com.olaz.instasprite.data.network.model.ResultResponse
import com.olaz.instasprite.data.network.model.UserProfileResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.PUT

interface ProfileApi {

    @Headers("Content-Type: application/json")
    @GET("/api/v1/accounts/profile")
    suspend fun getCurrentUserProfile(): ResultResponse<UserProfileResponse>

    @Headers("Content-Type: application/json")
    @GET("/api/v1/accounts/edit")
    suspend fun getEditProfile(): ResultResponse<EditProfileResponse>

    @Headers("Content-Type: application/json")
    @PUT("/api/v1/accounts/edit")
    suspend fun editProfile(
        @Body request: EditProfileRequest
    ): ResultResponse<String>
}