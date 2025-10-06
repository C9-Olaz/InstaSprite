package com.olaz.instasprite.data.network.model



data class JwtResponse(
    val type: String,
    val accessToken: String,
    val refreshToken: String,
    val name: String?,
    val username: String?,
    val email: String?,
    val isFirstTime: Boolean?
)

data class ResultResponse<T>(
    val status: Int,
    val code: String,
    val message: String,
    val data: T?
)

data class UserProfileResponse(
    val memberId: Int,
    val memberUsername: String,
    val memberName: String,
    val memberImageUrl: String,
    val memberIntroduce: String?,
    val memberPostsCount: Int,
    val memberFollowingsCount: Int,
    val memberFollowersCount: Int,
    val followingMemberFollow: List<String>,
    val followingMemberFollowCount: Int,
    val blocking: Boolean,
    val following: Boolean,
    val me: Boolean,
    val follower: Boolean,
    val blocked: Boolean
)

data class MemberImage(
    val imageUrl: String,
    val imageType: String,
    val imageName: String,
    val imageUUID: String
)


