package com.olaz.instasprite.data.network.api

import com.olaz.instasprite.data.network.model.EditProfileRequest
import com.olaz.instasprite.data.network.model.EditProfileResponse
import com.olaz.instasprite.data.network.model.ResultResponse
import com.olaz.instasprite.data.network.model.UserProfileResponse
import okhttp3.MultipartBody
import retrofit2.http.*

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

    @Multipart
    @POST("/api/v1/accounts/image")
    suspend fun uploadProfileImage(
        @Part image: MultipartBody.Part
    ): ResultResponse<String>
}