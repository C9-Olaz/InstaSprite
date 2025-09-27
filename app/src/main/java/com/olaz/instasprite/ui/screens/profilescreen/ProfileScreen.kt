package com.olaz.instasprite.ui.screens.profilescreen

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.olaz.instasprite.DrawingActivity

import com.olaz.instasprite.R
import com.olaz.instasprite.data.model.ISpriteData
import com.olaz.instasprite.data.model.ISpriteWithMetaData
import com.olaz.instasprite.data.model.SpriteMetaData
import com.olaz.instasprite.ui.screens.profilescreen.composable.EmptyStateContent
import com.olaz.instasprite.ui.screens.profilescreen.composable.ProfileBioSection
import com.olaz.instasprite.ui.screens.profilescreen.composable.ProfileHeader
import com.olaz.instasprite.ui.screens.profilescreen.composable.ProfileInfoSection
import com.olaz.instasprite.ui.screens.profilescreen.composable.ProfileStatsSection
import com.olaz.instasprite.ui.screens.profilescreen.composable.SpriteList
import com.olaz.instasprite.ui.theme.CatppuccinUI
import com.olaz.instasprite.ui.theme.InstaSpriteTheme
import com.olaz.instasprite.utils.UiUtils
import com.olaz.instasprite.ui.screens.profilescreen.dialog.EditProfileDialog
import com.olaz.instasprite.ui.screens.profilescreen.dialog.PostOptionsDialog


data class ProfileScreenState(
    val isProfileOwned: Boolean = true,
    val showEditProfileDialog: Boolean = false,
    val showBackgroundSelectorDialog: Boolean = false,
    val showPostOptionsDialog: Boolean = false,
    val showImagePager: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

data class UserProfile(
    val id: String = "",
    val username: String = "johndoe",
    val displayName: String = "John Doe",
    val bio: String = "Digital artist & creative enthusiast 🎨\nLove sharing my journey through art",
    val profileImageRes: Int = R.drawable.ic_launcher,
    val backgroundImageRes: Int? = null,
    val profileImageUri: Uri? = null,
    val backgroundImageUri: Uri? = null,
    val backgroundGradient: List<androidx.compose.ui.graphics.Color>? = null,
    val postsCount: Int = 0,
    val followersCount: Int = 0,
    val followingCount: Int = 0,
    val isFollowing: Boolean = false,
    val isOwnProfile: Boolean = true,
    val joinDate: Long = System.currentTimeMillis()
)

@Composable
fun ProfileScreen(
    userId: String? = null,
    onBackClick: () -> Unit = {},
    onPostClick: ((String) -> Unit)? = null,
    enableDummyPosts: Boolean = true // Add this parameter to control dummy posts
) {
    val context = LocalContext.current
    var uiState by remember { mutableStateOf(ProfileScreenState()) }
    var userProfile by remember { mutableStateOf(UserProfile()) }
    var userPosts by remember { mutableStateOf<List<ISpriteWithMetaData>>(emptyList()) }
    var selectedPostId by remember { mutableStateOf<String?>(null) }


    val handlePostClick = onPostClick ?: { postId: String ->
        val intent = Intent(context, com.olaz.instasprite.PostActivity::class.java)
        intent.putExtra("POST_ID", postId)
        context.startActivity(intent)
    }

    // Initialize profile data
    LaunchedEffect(userId) {
        userProfile = if (userId == null) {
            // Current user's profile
            UserProfile(
                id = "current_user",
                username = "current_user",
                displayName = "Current User",
                bio = "Welcome to my profile!",
                profileImageRes = R.drawable.ic_launcher,
                isOwnProfile = true,
                postsCount = if (enableDummyPosts) 5 else 0,
                followersCount = 42,
                followingCount = 18
            )
        } else {
            // Other user's profile
            UserProfile(
                id = userId,
                username = "other_user",
                displayName = "Other User",
                bio = "Another user's profile",
                profileImageRes = R.drawable.ic_launcher,
                isOwnProfile = false,
                isFollowing = false,
                postsCount = if (enableDummyPosts) 3 else 0,
                followersCount = 128,
                followingCount = 67
            )
        }

        // Load dummy posts if enabled
        if (enableDummyPosts) {
            userPosts = createDummyPosts()
            Log.d("ProfileScreen", "Loaded ${userPosts.size} dummy posts")
        }
    }

    UiUtils.SetStatusBarColor(CatppuccinUI.BackgroundColorDarker)
    UiUtils.SetNavigationBarColor(CatppuccinUI.BackgroundColorDarker)

    // Show loading indicator
    if (uiState.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                color = CatppuccinUI.BottomBarColor
            )
        }
        return
    }

    val lazyListState = rememberLazyListState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CatppuccinUI.BackgroundColorDarker)
    ) {
        // Use SpriteList with header content to make everything scroll together
        if (userPosts.isEmpty()) {
            // When empty, show profile sections + empty state
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 64.dp)
            ) {
                // Profile sections
                ProfileInfoSection(
                    userProfile = userProfile,
                    onEditProfileClick = {
                        uiState = uiState.copy(showEditProfileDialog = true)
                    },
                    onFollowClick = {
                        if (!userProfile.isOwnProfile) {
                            val newFollowState = !userProfile.isFollowing
                            userProfile = userProfile.copy(
                                isFollowing = newFollowState,
                                followersCount = if (newFollowState) {
                                    userProfile.followersCount + 1
                                } else {
                                    maxOf(0, userProfile.followersCount - 1)
                                }
                            )
                        }
                    },
                    onBackgroundTabClick = {
                        uiState = uiState.copy(showBackgroundSelectorDialog = true)
                    }
                )

                ProfileBioSection(
                    displayName = userProfile.displayName,
                    bio = userProfile.bio
                )

                ProfileStatsSection(
                    postsCount = userPosts.size,
                    followersCount = userProfile.followersCount,
                    followingCount = userProfile.followingCount
                )

                Text(
                    text = "Posts",
                    color = CatppuccinUI.TextColorLight,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )

                EmptyStateContent(
                    icon = Icons.Default.AccountBox,
                    title = "No posts yet",
                    subtitle = if (userProfile.isOwnProfile) "Create your first sprite!" else "No posts to show"
                )
            }
        } else {
            // When there are posts, use SpriteList with header content
            SpriteList(
                spritesWithMetaData = userPosts,
                lazyListState = lazyListState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 64.dp),
                onSpriteClick = { sprite ->
                    Log.d("ProfileScreen", "Clicked sprite: ${sprite.id}")
                    handlePostClick(sprite.id)
                },
                onSpriteEdit = { sprite ->
                    Log.d("ProfileScreen", "Edit sprite: ${sprite.id}")
                    val intent = Intent(context, DrawingActivity::class.java)
                    intent.putExtra(DrawingActivity.EXTRA_SPRITE_ID, sprite.id)
                    context.startActivity(intent)
                },
                onSpriteDelete = { spriteId ->
                    Log.d("ProfileScreen", "Delete sprite: $spriteId")
                    // Remove from local list (in real app, would delete from database)
                    userPosts = userPosts.filter { it.sprite.id != spriteId }
                    userProfile = userProfile.copy(
                        postsCount = maxOf(0, userProfile.postsCount - 1)
                    )
                },
                headerContent = {
                    Column {
                        ProfileInfoSection(
                            userProfile = userProfile,
                            onEditProfileClick = {
                                uiState = uiState.copy(showEditProfileDialog = true)
                            },
                            onFollowClick = {
                                if (!userProfile.isOwnProfile) {
                                    val newFollowState = !userProfile.isFollowing
                                    userProfile = userProfile.copy(
                                        isFollowing = newFollowState,
                                        followersCount = if (newFollowState) {
                                            userProfile.followersCount + 1
                                        } else {
                                            maxOf(0, userProfile.followersCount - 1)
                                        }
                                    )
                                }
                            },
                            onBackgroundTabClick = {
                                uiState = uiState.copy(showBackgroundSelectorDialog = true)
                            }
                        )

                        ProfileBioSection(
                            displayName = userProfile.displayName,
                            bio = userProfile.bio
                        )

                        ProfileStatsSection(
                            postsCount = userPosts.size,
                            followersCount = userProfile.followersCount,
                            followingCount = userProfile.followingCount
                        )

                        Text(
                            text = "Posts",
                            color = CatppuccinUI.TextColorLight,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                }
            )
        }

        // Persistent header - always visible at the top
        ProfileHeader(
            username = userProfile.username,
            isOwnProfile = userProfile.isOwnProfile,
            onBackClick = onBackClick,
            onShareProfileClick = {
                Log.d("ProfileScreen", "Sharing profile: ${userProfile.username}")
            },
            modifier = Modifier.align(Alignment.TopStart)
        )
    }

    // Show dialogs based on UI state
    if (uiState.showEditProfileDialog) {
        EditProfileDialog(
            userProfile = userProfile,
            onDismiss = {
                uiState = uiState.copy(showEditProfileDialog = false)
            },
            onSave = { newDisplayName, newBio ->
                userProfile = userProfile.copy(
                    displayName = newDisplayName,
                    bio = newBio
                )
                uiState = uiState.copy(showEditProfileDialog = false)
            }
        )
    }

    if (uiState.showPostOptionsDialog) {
        PostOptionsDialog(
            postId = selectedPostId ?: "",
            isOwnPost = userProfile.isOwnProfile,
            onDismiss = {
                uiState = uiState.copy(showPostOptionsDialog = false)
                selectedPostId = null
            },
            onDelete = { postId ->
                userPosts = userPosts.filter { it.sprite.id != postId }
                userProfile = userProfile.copy(
                    postsCount = maxOf(0, userProfile.postsCount - 1)
                )
            },
            onShare = { postId ->
                Log.d("ProfileScreen", "Sharing post: $postId")
            }
        )
    }
}

private fun createDummySpriteData(id: String, name: String): ISpriteData {
    val width = 32
    val height = 32
    val totalPixels = width * height

    // Create sample pixel data with colors (ARGB format as Int)
    val pixelsData = List(totalPixels) { index ->
        val row = index / width
        val col = index % width

        // Create different patterns for each sprite based on id
        when (id) {
            "dummy_1" -> {
                // Checkerboard pattern
                if ((row + col) % 2 == 0) 0xFF_FF_00_00.toInt() // Red
                else 0xFF_00_FF_00.toInt() // Green
            }
            "dummy_2" -> {
                // Gradient pattern
                val ratio = col.toFloat() / width
                when {
                    ratio < 0.33f -> 0xFF_FF_69_B4.toInt() // Hot Pink
                    ratio < 0.66f -> 0xFF_00_CED1.toInt() // Dark Turquoise
                    else -> 0xFF_FF_D700.toInt() // Gold
                }
            }
            "dummy_3" -> {
                // Random colors for abstract art
                val colors = listOf(
                    0xFF_FF_69_B4.toInt(), // Hot Pink
                    0xFF_00_CED1.toInt(), // Dark Turquoise
                    0xFF_FF_D700.toInt(), // Gold
                    0xFF_9370DB.toInt()   // Medium Purple
                )
                colors[kotlin.random.Random.nextInt(colors.size)]
            }
            "dummy_4" -> {
                // Nature scene - simple sky and ground
                if (row < height / 2) 0xFF_87_CEEB.toInt() // Sky Blue
                else 0xFF_22_8B_22.toInt() // Forest Green
            }
            "dummy_5" -> {
                // Robot design - geometric shapes
                when {
                    row < 8 || row > 24 -> 0xFF_C0_C0_C0.toInt() // Silver
                    col < 8 || col > 24 -> 0xFF_C0_C0_C0.toInt()
                    else -> 0xFF_FF_00_00.toInt() // Red center
                }
            }
            else -> {
                // Default random pattern
                (0xFF_000000 ).toInt()
            }
        }
    }

    // Create a basic color palette
    val colorPalette = listOf(
        0xFF_FF_00_00.toInt(), // Red
        0xFF_00_FF_00.toInt(), // Green
        0xFF_00_00_FF.toInt(), // Blue
        0xFF_FF_FF_00.toInt(), // Yellow
        0xFF_FF_00_FF.toInt(), // Magenta
        0xFF_00_FF_FF.toInt(), // Cyan
        0xFF_FF_FF_FF.toInt(), // White
        0xFF_00_00_00.toInt()  // Black
    )

    return ISpriteData(
        id = id,
        width = width,
        height = height,
        pixelsData = pixelsData,
        colorPalette = colorPalette
    )
}

// Helper function to create dummy posts
private fun createDummyPosts(): List<ISpriteWithMetaData> {
    val currentTime = System.currentTimeMillis()

    return listOf(
        ISpriteWithMetaData(
            sprite = createDummySpriteData("dummy_1", "My First Sprite"),
            meta = SpriteMetaData(
                spriteId = "dummy_1",
                spriteName = "My First Sprite",
                createdAt = currentTime - 86400000L, // 1 day ago
                lastModifiedAt = currentTime - 86400000L
            )
        ),
        ISpriteWithMetaData(
            sprite = createDummySpriteData("dummy_2", "Cool Character"),
            meta = SpriteMetaData(
                spriteId = "dummy_2",
                spriteName = "Cool Character",
                createdAt = currentTime - 172800000L, // 2 days ago
                lastModifiedAt = currentTime - 172800000L
            )
        ),
        ISpriteWithMetaData(
            sprite = createDummySpriteData("dummy_3", "Abstract Art"),
            meta = SpriteMetaData(
                spriteId = "dummy_3",
                spriteName = "Abstract Art",
                createdAt = currentTime - 259200000L, // 3 days ago
                lastModifiedAt = currentTime - 86400000L // Modified 1 day ago
            )
        ),
        ISpriteWithMetaData(
            sprite = createDummySpriteData("dummy_4", "Nature Scene"),
            meta = SpriteMetaData(
                spriteId = "dummy_4",
                spriteName = "Nature Scene",
                createdAt = currentTime - 345600000L, // 4 days ago
                lastModifiedAt = currentTime - 345600000L
            )
        ),
        ISpriteWithMetaData(
            sprite = createDummySpriteData("dummy_5", "Robot Design"),
            meta = SpriteMetaData(
                spriteId = "dummy_5",
                spriteName = "Robot Design",
                createdAt = currentTime - 432000000L, // 5 days ago
                lastModifiedAt = currentTime - 172800000L // Modified 2 days ago
            )
        )
    )
}



@Preview(showBackground = true)
@Composable
private fun ProfileScreenPreview() {
    InstaSpriteTheme {
        ProfileScreen()
    }
}