package com.olaz.instasprite.ui.screens.postscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.olaz.instasprite.R
import com.olaz.instasprite.data.model.ISpriteData
import com.olaz.instasprite.data.model.ISpriteWithMetaData
import com.olaz.instasprite.data.model.SpriteMetaData
import com.olaz.instasprite.ui.screens.postscreen.composable.CommentInput
import com.olaz.instasprite.ui.screens.postscreen.composable.CommentItem
import com.olaz.instasprite.ui.screens.postscreen.composable.CommentsHeader
import com.olaz.instasprite.ui.screens.postscreen.composable.PostActions
import com.olaz.instasprite.ui.screens.postscreen.composable.PostDescription
import com.olaz.instasprite.ui.screens.postscreen.composable.PostHeader
import com.olaz.instasprite.ui.screens.postscreen.composable.PostImage
import com.olaz.instasprite.ui.screens.postscreen.dialog.DeletePostDialog
import com.olaz.instasprite.ui.theme.CatppuccinUI
import com.olaz.instasprite.ui.theme.InstaSpriteTheme
import com.olaz.instasprite.utils.UiUtils
import kotlinx.coroutines.launch

data class PostScreenState(
    val isLoading: Boolean = false,
    val isLiked: Boolean = false,
    val likesCount: Int = 0,
    val isBookmarked: Boolean = false,
    val showShareDialog: Boolean = false,
    val showDeleteDialog: Boolean = false,
    val errorMessage: String? = null,
    val showImagePager: Boolean = false
)

data class Comment(
    val id: String,
    val userId: String,
    val username: String,
    val displayName: String,
    val profileImageRes: Int,
    val content: String,
    val createdAt: Long,
    val likesCount: Int = 0,
    val isLiked: Boolean = false,
    val isOwnComment: Boolean = false
)

data class PostAuthor(
    val id: String,
    val username: String,
    val displayName: String,
    val profileImageRes: Int,
    val isFollowing: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostScreen(
    postId: String? = null,
    onBackClick: () -> Unit = {},
    onProfileClick: (String) -> Unit = {},
    onEditPost: (ISpriteData) -> Unit = {},
    onDeletePost: (String) -> Unit = {}
) {
    val lazyListState = rememberLazyListState()
    val focusManager = LocalFocusManager.current
    val commentFocusRequester = remember { FocusRequester() }
    val coroutineScope = rememberCoroutineScope()

    var uiState by remember { mutableStateOf(PostScreenState()) }
    var post by remember { mutableStateOf<ISpriteWithMetaData?>(null) }
    var postAuthor by remember { mutableStateOf<PostAuthor?>(null) }
    var comments by remember { mutableStateOf<List<Comment>>(emptyList()) }
    var newCommentText by remember { mutableStateOf("") }
    var isOwnPost by remember { mutableStateOf(true) }

    // Initialize post data
    LaunchedEffect(postId) {
        post = createDummyPost(postId ?: "own_post")
        postAuthor = PostAuthor(
            id = "author_1",
            username = "pixel_artist_99",
            displayName = "Pixel Artist",
            profileImageRes = R.drawable.ic_launcher,
            isFollowing = false
        )
        comments = createDummyComments()
        isOwnPost = (postId == "own_post") // Simulate own post check

        uiState = uiState.copy(
            likesCount = 42,
            isLiked = false,
            isBookmarked = false
        )
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CatppuccinUI.BackgroundColorDarker)
    ) {
        // Top App Bar
        TopAppBar(
            title = {
                Text(
                    text = "Post",
                    color = CatppuccinUI.TextColorLight,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = CatppuccinUI.TextColorLight
                    )
                }
            },
            actions = {
                if (true) {
                    IconButton(onClick = {
                        uiState = uiState.copy(showDeleteDialog = true)
                    }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete Post",
                            tint = CatppuccinUI.TextColorLight
                        )
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = CatppuccinUI.BackgroundColorDarker
            )
        )

        // Content
        LazyColumn(
            state = lazyListState,
            modifier = Modifier.weight(1f)
        ) {
            post?.let { postData ->
                // Post Header
                item {
                    PostHeader(
                        author = postAuthor,
                        post = postData,
                        isOwnPost = isOwnPost,
                        onProfileClick = onProfileClick,
                        onFollowClick = {
                            postAuthor = postAuthor?.copy(
                                isFollowing = !(postAuthor?.isFollowing ?: false)
                            )
                        },
                        onEditClick = {
                            onEditPost(postData.sprite)
                        }
                    )
                }

                // Post Image
                item {
                    PostImage(
                        sprite = postData.sprite,
                        onDoubleClick = {
                            uiState = uiState.copy(
                                isLiked = !uiState.isLiked,
                                likesCount = if (!uiState.isLiked) {
                                    uiState.likesCount + 1
                                } else {
                                    maxOf(0, uiState.likesCount - 1)
                                }
                            )
                        },
                        onImageClick = {
                            uiState = uiState.copy(showImagePager = true)
                        }
                    )
                }

                // Post Actions
                item {
                    PostActions(
                        isLiked = uiState.isLiked,
                        isBookmarked = uiState.isBookmarked,
                        likesCount = uiState.likesCount,
                        commentsCount = comments.size,
                        onLikeClick = {
                            uiState = uiState.copy(
                                isLiked = !uiState.isLiked,
                                likesCount = if (!uiState.isLiked) {
                                    uiState.likesCount + 1
                                } else {
                                    maxOf(0, uiState.likesCount - 1)
                                }
                            )
                        },
                        onCommentClick = {
                            coroutineScope.launch {
                                commentFocusRequester.requestFocus()
                            }
                        },
                        onShareClick = {
                            uiState = uiState.copy(showShareDialog = true)
                        },
                        onBookmarkClick = {
                            uiState = uiState.copy(isBookmarked = !uiState.isBookmarked)
                        }
                    )
                }

                // Post Description
                item {
                    PostDescription(
                        author = postAuthor,
                        post = postData,
                        onProfileClick = onProfileClick
                    )
                }

                // Comments Header
                item {
                    CommentsHeader(
                        commentsCount = comments.size
                    )
                }
            }

            // Comments List
            items(
                items = comments,
                key = { it.id }
            ) { comment ->
                CommentItem(
                    comment = comment,
                    onProfileClick = onProfileClick,
                    onLikeClick = { commentId ->
                        comments = comments.map {
                            if (it.id == commentId) {
                                it.copy(
                                    isLiked = !it.isLiked,
                                    likesCount = if (!it.isLiked) {
                                        it.likesCount + 1
                                    } else {
                                        maxOf(0, it.likesCount - 1)
                                    }
                                )
                            } else it
                        }
                    },
                    onDeleteClick = { commentId ->
                        comments = comments.filter { it.id != commentId }
                    }
                )
            }

            // Add some bottom padding
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        // Comment Input
        CommentInput(
            text = newCommentText,
            onTextChange = { newCommentText = it },
            onSendClick = {
                if (newCommentText.isNotBlank()) {
                    val newComment = Comment(
                        id = "comment_${System.currentTimeMillis()}",
                        userId = "current_user",
                        username = "current_user",
                        displayName = "You",
                        profileImageRes = R.drawable.ic_launcher,
                        content = newCommentText,
                        createdAt = System.currentTimeMillis(),
                        isOwnComment = true
                    )
                    comments = listOf(newComment) + comments
                    newCommentText = ""
                    focusManager.clearFocus()
                }
            },
            modifier = Modifier.focusRequester(commentFocusRequester)
        )
    }

    if (uiState.showDeleteDialog) {
        DeletePostDialog(
            onDismiss = { uiState = uiState.copy(showDeleteDialog = false) },
            onConfirm = {
                onDeletePost(postId ?: "")
                uiState = uiState.copy(showDeleteDialog = false)
                onBackClick()
            }
        )
    }
}

// Helper functions for dummy data
private fun createDummyPost(postId: String): ISpriteWithMetaData {
    val currentTime = System.currentTimeMillis()

    return ISpriteWithMetaData(
        sprite = createDummySpriteData(postId, "Featured Artwork"),
        meta = SpriteMetaData(
            spriteId = postId,
            spriteName = "Featured Artwork",
            createdAt = currentTime - 3600000L, // 1 hour ago
            lastModifiedAt = currentTime - 3600000L
        )
    )
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

private fun createDummyComments(): List<Comment> {
    val currentTime = System.currentTimeMillis()

    return listOf(
        Comment(
            id = "comment_1",
            userId = "user_1",
            username = "artist_lover",
            displayName = "Art Lover",
            profileImageRes = R.drawable.ic_launcher,
            content = "This is absolutely amazing! The color palette is perfect 🎨",
            createdAt = currentTime - 1800000L, // 30 min ago
            likesCount = 12,
            isLiked = false
        ),
        Comment(
            id = "comment_2",
            userId = "user_2",
            username = "pixel_master",
            displayName = "Pixel Master",
            profileImageRes = R.drawable.ic_launcher,
            content = "Love the detail work! How long did this take you to create?",
            createdAt = currentTime - 3600000L, // 1 hour ago
            likesCount = 5,
            isLiked = true
        ),
        Comment(
            id = "comment_3",
            userId = "user_3",
            username = "creative_soul",
            displayName = "Creative Soul",
            profileImageRes = R.drawable.ic_launcher,
            content = "The symmetry is so satisfying! Tutorial please? 😍",
            createdAt = currentTime - 7200000L, // 2 hours ago
            likesCount = 8,
            isLiked = false
        ),
        Comment(
            id = "comment_4",
            userId = "user_4",
            username = "gamedev_joe",
            displayName = "Game Dev Joe",
            profileImageRes = R.drawable.ic_launcher,
            content = "This would look great as a game sprite! Mind if I use it as inspiration?",
            createdAt = currentTime - 10800000L, // 3 hours ago
            likesCount = 3,
            isLiked = false
        ),
        Comment(
            id = "comment_5",
            userId = "user_5",
            username = "retro_fan",
            displayName = "Retro Fan",
            profileImageRes = R.drawable.ic_launcher,
            content = "Getting serious 80s vibes from this! Awesome work 🔥",
            createdAt = currentTime - 14400000L, // 4 hours ago
            likesCount = 15,
            isLiked = true
        )
    )
}



@Preview(showBackground = true)
@Composable
private fun PostScreenPreview() {
    InstaSpriteTheme {
        PostScreen()
    }
}