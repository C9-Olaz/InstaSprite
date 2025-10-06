package com.olaz.instasprite.data.network

import com.olaz.instasprite.utils.TokenUtils
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val tokenUtils: TokenUtils) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        

        val authHeader = tokenUtils.getAuthorizationHeader()
        
        val newRequest = if (authHeader != null) {
            originalRequest.newBuilder()
                .addHeader("Authorization", authHeader)
                .build()
        } else {
            originalRequest
        }
        
        return chain.proceed(newRequest)
    }
}
