package com.olaz.instasprite.data.repository

import com.olaz.instasprite.data.network.NetworkModule
import com.olaz.instasprite.data.network.model.GoogleLoginRequest
import com.olaz.instasprite.data.network.model.JwtResponse
import com.olaz.instasprite.data.network.model.ResultResponse

class AuthRepository {
    private val api = NetworkModule.authApi

    suspend fun loginWithGoogle(idToken: String): ResultResponse<JwtResponse> {
        return api.loginWithGoogle(GoogleLoginRequest(idToken))
    }
}


