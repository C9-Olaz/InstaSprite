package com.olaz.instasprite.data.network.model

data class GoogleLoginRequest(
    val idToken: String
)

data class EditProfileRequest(
    val memberUsername: String,
    val memberName: String,
    val memberIntroduce: String?,
    val memberEmail: String
)

data class EditProfileResponse(
    val memberUsername: String,
    val memberImageUrl: String,
    val memberName: String,
    val memberIntroduce: String?,
    val memberEmail: String
)
