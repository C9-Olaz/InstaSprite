package com.olaz.instasprite.ui.screens.profilescreen

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

import com.olaz.instasprite.R
import com.olaz.instasprite.data.database.AppDatabase
import com.olaz.instasprite.data.repository.ISpriteDatabaseRepository
import com.olaz.instasprite.data.repository.SortSettingRepository
import com.olaz.instasprite.data.repository.StorageLocationRepository
import com.olaz.instasprite.ui.screens.homescreen.ImagePagerOverlay
import com.olaz.instasprite.ui.theme.CatppuccinUI
import com.olaz.instasprite.ui.theme.InstaSpriteTheme
import com.olaz.instasprite.utils.UiUtils
import com.olaz.instasprite.ui.screens.profilescreen.SpriteCard
import com.olaz.instasprite.ui.screens.profilescreen.ProfileScreenViewModel
import com.olaz.instasprite.ui.screens.profilescreen.ProfileImagePagerOverlay

@Composable
fun ProfileScreen(
    viewModel: ProfileScreenViewModel, onBackClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()
    val userPosts by viewModel.sprites.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    UiUtils.SetStatusBarColor(CatppuccinUI.BackgroundColorDarker)
    UiUtils.SetNavigationBarColor(CatppuccinUI.BackgroundColorDarker)

    // Show error message if any
    uiState.errorMessage?.let { error ->
        LaunchedEffect(error) {
            viewModel.clearError()
        }
    }

    // Show loading indicator
    if (uiState.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                color = CatppuccinUI.BottomBarColor
            )
        }
        return
    }

    if (uiState.showImagePager) {
        ProfileImagePagerOverlay(
            viewModel = viewModel, onDismiss = { lastSpriteSeen ->
                viewModel.toggleImagePager(null)
                viewModel.lastSpriteSeenInPager = lastSpriteSeen
            })
    }

    val lazyListState = rememberLazyListState()

    // Handle scroll to edited sprite
    LaunchedEffect(userPosts, viewModel.lastEditedSpriteId) {
        viewModel.lastEditedSpriteId?.let { editedId ->
            val index = userPosts.indexOfFirst { it.sprite.id == editedId }
            if (index != -1) {
                coroutineScope.launch {
                    lazyListState.animateScrollToItem(index + 1)
                }
            }
            viewModel.lastEditedSpriteId = null
        }
    }

    LaunchedEffect(viewModel.lastSpriteSeenInPager) {
        viewModel.lastSpriteSeenInPager?.let { sprite ->
            val index = userPosts.indexOfFirst { it.sprite.id == sprite.id }
            if (index != -1) {
                // Add 1 to account for the profile header item
                coroutineScope.launch {
                    lazyListState.animateScrollToItem(index + 1)
                }
            }
            viewModel.lastSpriteSeenInPager = null
        }
    }

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
                    .padding(top = 64.dp) // Account for persistent header
            ) {
                // Profile sections
                ProfileInfoSection(userProfile = userProfile, onEditProfileClick = {
                    viewModel.toggleEditProfileDialog()
                }, onFollowClick = {
                    viewModel.toggleFollowUser()
                }, onBackgroundTabClick = {
                    viewModel.toggleBackgroundSelectorDialog()
                })

                ProfileBioSection(
                    displayName = userProfile.displayName, bio = userProfile.bio
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
                viewModel = viewModel,
                spritesWithMetaData = userPosts,
                lazyListState = lazyListState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 64.dp), // Account for persistent header
                headerContent = {
                    Column {
                        ProfileInfoSection(userProfile = userProfile, onEditProfileClick = {
                            viewModel.toggleEditProfileDialog()
                        }, onFollowClick = {
                            viewModel.toggleFollowUser()
                        }, onBackgroundTabClick = {
                            viewModel.toggleBackgroundSelectorDialog()
                        })

                        ProfileBioSection(
                            displayName = userProfile.displayName, bio = userProfile.bio
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
                })
        }

        // Persistent header - always visible at the top
        ProfileHeader(
            username = userProfile.username,
            isOwnProfile = userProfile.isOwnProfile,
            onBackClick = onBackClick,
            onShareProfileClick = {
                viewModel.shareProfile(context)
            },
            modifier = Modifier.align(Alignment.TopStart)
        )
    }

    // Show dialogs based on UI state
    if (uiState.showEditProfileDialog) {
        EditProfileDialog(
            viewModel = viewModel, onDismiss = {
                viewModel.toggleEditProfileDialog()
            })
    }

    if (uiState.showPostOptionsDialog) {
        PostOptionsDialog(
            viewModel = viewModel,
            postId = viewModel.selectedPostId ?: "",
            isOwnPost = userProfile.isOwnProfile,
            onDismiss = {
                viewModel.togglePostOptionsDialog()
            })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileHeader(
    username: String,
    isOwnProfile: Boolean,
    onBackClick: () -> Unit,
    onShareProfileClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Text(
                text = username,
                color = CatppuccinUI.TextColorLight,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }, navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = CatppuccinUI.TextColorLight
                )
            }
        }, actions = {
            if (!isOwnProfile) {
                IconButton(onClick = onShareProfileClick) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share Profile",
                        tint = CatppuccinUI.TextColorLight
                    )
                }
            }
        }, colors = TopAppBarDefaults.topAppBarColors(
            containerColor = CatppuccinUI.BackgroundColorDarker
        ), modifier = modifier
    )
}

@Composable
private fun ProfileInfoSection(
    userProfile: UserProfile,
    onEditProfileClick: () -> Unit,
    onFollowClick: () -> Unit,
    onBackgroundTabClick: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Background section
        ProfileBackgroundTab(
            userProfile = userProfile,
            onEditProfileClick = onEditProfileClick
        )

        // Profile Image overlapping background
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .align(Alignment.BottomStart) // anchors row at bottom of the Box
        ) {
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .offset(y = 75.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(CatppuccinUI.BackgroundColorDarker, CircleShape)
                        .padding(4.dp)
                ) {
                    Image(
                        painter = painterResource(
                            id = userProfile.profileImageRes ?: R.drawable.ic_launcher
                        ),
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            )
        }
    }
}


@Composable
private fun ProfileBackgroundTab(
    userProfile: UserProfile, onEditProfileClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clickable {
                if (userProfile.isOwnProfile) {
                    onEditProfileClick()
                }
            }) {
        when {
            // Custom image background
            userProfile.backgroundImageRes != null -> {
                Image(
                    painter = painterResource(id = userProfile.backgroundImageRes),
                    contentDescription = "Profile Background",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Overlay for better text readability
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            androidx.compose.ui.graphics.Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    CatppuccinUI.BackgroundColorDarker.copy(alpha = 0.3f)
                                )
                            )
                        )
                )
            }

            // Custom gradient background
            userProfile.backgroundGradient != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            androidx.compose.ui.graphics.Brush.linearGradient(
                                colors = userProfile.backgroundGradient
                            )
                        )
                )
            }

            // Default background with subtle gradient
            else -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            androidx.compose.ui.graphics.Brush.linearGradient(
                                colors = listOf(
                                    CatppuccinUI.CanvasChecker1Color.copy(alpha = 0.3f),
                                    CatppuccinUI.CanvasChecker2Color.copy(alpha = 0.8f)
                                )
                            )
                        )
                )
            }
        }

        // Add background edit hint for own profile
        if (userProfile.isOwnProfile && userProfile.backgroundImageUri == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.TopEnd
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Background",
                    tint = CatppuccinUI.TextColorLight.copy(alpha = 0.6f),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
private fun ProfileBioSection(
    displayName: String, bio: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = displayName,
            color = CatppuccinUI.TextColorLight,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        if (bio.isNotBlank()) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = bio,
                color = CatppuccinUI.TextColorLight.copy(alpha = 0.8f),
                fontSize = 14.sp,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
private fun ProfileStatsSection(
    postsCount: Int, followersCount: Int, followingCount: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        StatItem(
            count = postsCount, label = "Posts"
        )
        StatItem(
            count = followersCount, label = "Followers"
        )
        StatItem(
            count = followingCount, label = "Following"
        )
    }
}

@Composable
private fun StatItem(
    count: Int, label: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = formatCount(count),
            color = CatppuccinUI.TextColorLight,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label, color = CatppuccinUI.TextColorLight.copy(alpha = 0.7f), fontSize = 14.sp
        )
    }
}

@Composable
private fun EmptyStateContent(
    icon: ImageVector, title: String, subtitle: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = CatppuccinUI.TextColorLight.copy(alpha = 0.4f),
            modifier = Modifier.size(64.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = title,
            color = CatppuccinUI.TextColorLight,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = subtitle,
            color = CatppuccinUI.TextColorLight.copy(alpha = 0.7f),
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun EditProfileDialog(
    viewModel: ProfileScreenViewModel, onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Background") },
        text = { Text("Background selector dialog - implement UI here") },
        confirmButton = {
            TextButton(onClick = {
                viewModel.saveProfileChanges()
            }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        })
}

@Composable
private fun PostOptionsDialog(
    viewModel: ProfileScreenViewModel, postId: String, isOwnPost: Boolean, onDismiss: () -> Unit
) {
    val context = LocalContext.current

    AlertDialog(onDismissRequest = onDismiss, title = { Text("Post Options") }, text = {
        Column {
            if (isOwnPost) {
                TextButton(
                    onClick = {
                        viewModel.deletePost(postId)
                        onDismiss()
                    }) {
                    Text("Delete Post")
                }
            }
            TextButton(
                onClick = {
                    viewModel.sharePost(context, postId)
                    onDismiss()
                }) {
                Text("Share Post")
            }
        }
    }, confirmButton = {
        TextButton(onClick = onDismiss) {
            Text("Close")
        }
    })
}

// Helper function to format counts
private fun formatCount(count: Int): String {
    return when {
        count >= 1_000_000 -> "${(count / 1_000_000)}M"
        count >= 1_000 -> "${(count / 1_000)}K"
        else -> count.toString()
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileScreenPreview() {
    val context = LocalContext.current
    val database = AppDatabase.getInstance(context)
    val spriteDataRepository =
        ISpriteDatabaseRepository(database.spriteDataDao(), database.spriteMetaDataDao())
    val sortSettingRepository = SortSettingRepository(context)
    val storageLocationRepository = StorageLocationRepository(context)

    val viewModel = ProfileScreenViewModel(
        spriteDatabaseRepository = spriteDataRepository,
        userId = null,
        storageLocationRepository = storageLocationRepository, // Own profile
    )

    InstaSpriteTheme {
        ProfileScreen(viewModel = viewModel)
    }
}