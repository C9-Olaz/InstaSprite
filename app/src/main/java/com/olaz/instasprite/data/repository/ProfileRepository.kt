package com.olaz.instasprite.data.repository

import android.net.Uri
import com.olaz.instasprite.data.network.NetworkModule
import com.olaz.instasprite.data.network.model.EditProfileRequest
import com.olaz.instasprite.data.network.model.EditProfileResponse
import com.olaz.instasprite.data.network.model.ResultResponse
import com.olaz.instasprite.data.network.model.UserProfileResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

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

    suspend fun uploadProfileImage(imageUri: Uri, context: android.content.Context): ResultResponse<String> {
        return try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(imageUri)
            val file = File.createTempFile("profile_image", ".jpg", context.cacheDir)
            val outputStream = FileOutputStream(file)
            
            inputStream?.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }

            val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("uploadedImage", file.name, requestFile)
            

            val response = api.uploadProfileImage(body)
            

            file.delete()
            
            response
        } catch (e: Exception) {
            ResultResponse(
                status = 500,
                code = "UPLOAD_ERROR",
                message = e.message ?: "Failed to upload image",
                data = null
            )
        }
    }
}

