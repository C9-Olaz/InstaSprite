package com.olaz.instasprite.data.network.api

import com.olaz.instasprite.data.network.model.GoogleLoginRequest
import com.olaz.instasprite.data.network.model.JwtResponse
import com.olaz.instasprite.data.network.model.ResultResponse
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface AuthApi {

    @Headers("Content-Type: application/json")
    @POST("/api/v1/auth/google")
    suspend fun loginWithGoogle(
        @Body request: GoogleLoginRequest
    ): ResultResponse<JwtResponse>
}