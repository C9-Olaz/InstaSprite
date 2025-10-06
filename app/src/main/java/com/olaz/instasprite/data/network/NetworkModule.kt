package com.olaz.instasprite.data.network

import android.content.Context
import com.olaz.instasprite.utils.TokenUtils
import com.olaz.instasprite.data.network.api.AuthApi
import com.olaz.instasprite.data.network.api.ProfileApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkModule {

    private const val BASE_URL = "http://localhost:8080"
    private var tokenUtils: TokenUtils? = null

    fun initialize(context: Context) {
        tokenUtils = TokenUtils(context)
    }

    private val logging: HttpLoggingInterceptor by lazy {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
    }

    private val okHttpClient: OkHttpClient by lazy {
        val builder = OkHttpClient.Builder()
            .addInterceptor(logging)

        tokenUtils?.let { tokenManager ->
            builder.addInterceptor(AuthInterceptor(tokenManager))
        }
        
        builder.build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val authApi: AuthApi by lazy { retrofit.create(AuthApi::class.java) }
    val profileApi: ProfileApi by lazy { retrofit.create(ProfileApi::class.java) }
}


