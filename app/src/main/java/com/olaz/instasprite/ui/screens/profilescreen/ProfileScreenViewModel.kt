//package com.olaz.instasprite.ui.screens.profilescreen
//
//import android.content.Context
//import android.content.Intent
//import android.net.Uri
//import android.util.Log
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableIntStateOf
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.setValue
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.olaz.instasprite.DrawingActivity
//import com.olaz.instasprite.R
//import com.olaz.instasprite.data.model.ISpriteData
//import com.olaz.instasprite.data.model.ISpriteWithMetaData
//import com.olaz.instasprite.data.repository.ISpriteDatabaseRepository
//import com.olaz.instasprite.data.repository.SortSettingRepository
//import com.olaz.instasprite.data.repository.StorageLocationRepository
//import com.olaz.instasprite.domain.usecase.SaveFileUseCase
//import com.olaz.instasprite.ui.screens.homescreen.SpriteListOrder
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.SharingStarted
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.flow.stateIn
//import kotlinx.coroutines.launch
//
//data class ProfileScreenState(
//    val isProfileOwned: Boolean = true,
//    val showEditProfileDialog: Boolean = false,
//    val showBackgroundSelectorDialog: Boolean = false,
//    val showPostOptionsDialog: Boolean = false,
//    val showCreateCanvasDialog: Boolean = false,
//    val showSelectSortOptionDialog: Boolean = false,
//    val showSearchBar: Boolean = false,
//    val showImagePager: Boolean = false,
//    val isLoading: Boolean = false,
//    val errorMessage: String? = null
//)
//
//data class UserProfile(
//    val id: String = "",
//    val username: String = "johndoe",
//    val displayName: String = "John Doe",
//    val bio: String = "Digital artist & creative enthusiast 🎨\nLove sharing my journey through art",
//    // Updated to use drawable resource IDs instead of URIs
//    val profileImageRes: Int = R.drawable.ic_launcher,
//    val backgroundImageRes: Int? = null,
//    // Keeping URIs for backward compatibility, but these won't be used in the UI
//    val profileImageUri: Uri? = null,
//    val backgroundImageUri: Uri? = null,
//    val backgroundGradient: List<androidx.compose.ui.graphics.Color>? = null,
//    val postsCount: Int = 0,
//    val followersCount: Int = 0,
//    val followingCount: Int = 0,
//    val isFollowing: Boolean = false,
//    val isOwnProfile: Boolean = true,
//    val joinDate: Long = System.currentTimeMillis()
//)
//
//data class EditProfileData(
//    val displayName: String,
//    val bio: String,
//    val profileImageRes: Int // Changed from Uri to drawable resource ID
//)
//
//class ProfileScreenViewModel(
//    private val spriteDatabaseRepository: ISpriteDatabaseRepository,
//    private val userId: String? = null, // If null, shows current user's profile
//    private val storageLocationRepository: StorageLocationRepository
//
//) : ViewModel() {
//    private val saveFileUseCase = SaveFileUseCase()
//
//    private val _uiState = MutableStateFlow(ProfileScreenState())
//    val uiState: StateFlow<ProfileScreenState> = _uiState.asStateFlow()
//
//    private val _userProfile = MutableStateFlow(UserProfile())
//    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()
//
//    // Get user's sprites as posts
//    val sprites: StateFlow<List<ISpriteWithMetaData>> =
//        spriteDatabaseRepository.getAllSpritesWithMeta()
//            .stateIn(
//                viewModelScope,
//                SharingStarted.WhileSubscribed(5000),
//                emptyList()
//            )
//
//    // Add properties to match HomeScreenViewModel
//    var searchQuery by mutableStateOf("")
//        private set
//
//    private val _lastSavedLocation = MutableStateFlow<Uri?>(null)
//
//    var lastEditedSpriteId by mutableStateOf<String?>(null)
//    var spriteList by mutableStateOf(emptyList<ISpriteWithMetaData>())
//    var currentSelectedSpriteIndex by mutableIntStateOf(0)
//    var lastSpriteSeenInPager by mutableStateOf<ISpriteData?>(null)
//
//    var selectedPostId by mutableStateOf<String?>(null)
//        private set
//
//    var editProfileData by mutableStateOf(
//        EditProfileData(
//            displayName = "",
//            bio = "",
//            profileImageRes = R.drawable.ic_launcher
//        )
//    )
//        private set
//
//    // Array of available profile images (you can expand this list)
//    private val availableProfileImages = arrayOf(
//        R.drawable.ic_launcher,
//    )
//
//    // Array of available background images (you can expand this list)
//    private val availableBackgroundImages = arrayOf(
//        R.drawable.ic_launcher,
//        // Add more background drawable resources as needed
//        // R.drawable.background_1,
//        // R.drawable.background_2,
//        // etc.
//    )
//
//    init {
//        loadUserProfile()
//    }
//
//    private fun loadUserProfile() {
//        viewModelScope.launch {
//            _uiState.value = _uiState.value.copy(isLoading = true)
//
//            try {
//                // In a real app, you would load user data from repository
//                // For now, we'll use default data
//                val profile = if (userId == null) {
//                    // Current user's profile
//                    UserProfile(
//                        id = "current_user",
//                        username = "current_user",
//                        displayName = "Current User",
//                        bio = "Welcome to my profile!",
//                        profileImageRes = R.drawable.ic_launcher,
//                        isOwnProfile = true
//                    )
//                } else {
//                    // Other user's profile
//                    UserProfile(
//                        id = userId,
//                        username = "other_user",
//                        displayName = "Other User",
//                        bio = "Another user's profile",
//                        profileImageRes = R.drawable.ic_launcher,
//                        isOwnProfile = false,
//                        isFollowing = false
//                    )
//                }
//
//                _userProfile.value = profile
//                editProfileData = EditProfileData(
//                    displayName = profile.displayName,
//                    bio = profile.bio,
//                    profileImageRes = profile.profileImageRes
//                )
//
//            } catch (e: Exception) {
//                _uiState.value = _uiState.value.copy(
//                    errorMessage = "Failed to load profile: ${e.message}"
//                )
//                Log.e("ProfileViewModel", "Failed to load user profile", e)
//            } finally {
//                _uiState.value = _uiState.value.copy(isLoading = false)
//            }
//        }
//    }
//
//    // UI State Toggle Functions (matching HomeScreenViewModel)
//    fun toggleCreateCanvasDialog() {
//        _uiState.value = _uiState.value.copy(
//            showCreateCanvasDialog = !_uiState.value.showCreateCanvasDialog
//        )
//    }
//
//    fun toggleSelectSortOptionDialog() {
//        _uiState.value = _uiState.value.copy(
//            showSelectSortOptionDialog = !_uiState.value.showSelectSortOptionDialog
//        )
//    }
//
//    fun toggleSearchBar() {
//        _uiState.value = _uiState.value.copy(
//            showSearchBar = !_uiState.value.showSearchBar
//        )
//    }
//
//    fun toggleImagePager(selectedSprite: ISpriteData?) {
//        _uiState.value = _uiState.value.copy(
//            showImagePager = !_uiState.value.showImagePager
//        )
//        currentSelectedSpriteIndex = spriteList.map { it.sprite }.indexOf(selectedSprite)
//    }
//
//    fun toggleEditProfileDialog() {
//        _uiState.value = _uiState.value.copy(
//            showEditProfileDialog = !_uiState.value.showEditProfileDialog
//        )
//    }
//
//    fun toggleBackgroundSelectorDialog() {
//        _uiState.value = _uiState.value.copy(
//            showBackgroundSelectorDialog = !_uiState.value.showBackgroundSelectorDialog
//        )
//    }
//
//    fun togglePostOptionsDialog(postId: String? = null) {
//        selectedPostId = postId
//        _uiState.value = _uiState.value.copy(
//            showPostOptionsDialog = !_uiState.value.showPostOptionsDialog
//        )
//    }
//
//    suspend fun getLastSavedLocation(): Uri? {
//        _lastSavedLocation.value = storageLocationRepository.getLastSavedLocation()
//        return _lastSavedLocation.value
//    }
//
//    fun setLastSavedLocation(uri: Uri) {
//        _lastSavedLocation.value = uri
//        viewModelScope.launch {
//            storageLocationRepository.setLastSavedLocation(uri)
//        }
//    }
//
//
//    fun saveImage(
//        context: Context,
//        ispriteData: ISpriteData,
//        folderUri: Uri,
//        fileName: String,
//        scalePercent: Int = 100
//    ): Boolean {
//        val result = saveFileUseCase.saveImageFile(
//            context,
//            ispriteData,
//            scalePercent,
//            folderUri,
//            fileName
//        )
//
//        result.fold(
//            onSuccess = { return true },
//            onFailure = { exception ->
//                Log.e("SaveFile", "Failed to save file", exception)
//                return false
//            }
//        )
//    }
//
//    // Sprite Operations (matching HomeScreenViewModel)
//    fun deleteSpriteById(spriteId: String) {
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                spriteDatabaseRepository.deleteSpriteById(spriteId)
//            } catch (e: Exception) {
//                Log.e("ProfileScreenViewModel", "Error deleting sprite", e)
//            }
//        }
//    }
//
//    fun deleteSpriteByIdDelay(spriteId: String, duration: Long) {
//        viewModelScope.launch(Dispatchers.IO) {
//            delay(duration)
//            deleteSpriteById(spriteId)
//        }
//    }
//
//    fun openDrawingActivity(context: Context, sprite: ISpriteData) {
//        lastEditedSpriteId = sprite.id
//        val intent = Intent(context, DrawingActivity::class.java)
//        intent.putExtra(DrawingActivity.EXTRA_SPRITE_ID, sprite.id)
//        context.startActivity(intent)
//    }
//
//    fun renameSprite(spriteId: String, newName: String) {
//        viewModelScope.launch(Dispatchers.IO) {
//            spriteDatabaseRepository.changeName(spriteId, newName)
//        }
//    }
//
//
//    fun updateSearchQuery(query: String) {
//        searchQuery = query
//    }
//
//    // Profile-specific methods
//    fun updateEditProfileData(
//        displayName: String? = null,
//        bio: String? = null,
//        profileImageRes: Int? = null
//    ) {
//        editProfileData = editProfileData.copy(
//            displayName = displayName ?: editProfileData.displayName,
//            bio = bio ?: editProfileData.bio,
//            profileImageRes = profileImageRes ?: editProfileData.profileImageRes
//        )
//    }
//
//    fun saveProfileChanges() {
//        viewModelScope.launch {
//            _uiState.value = _uiState.value.copy(isLoading = true)
//
//            try {
//                // In a real app, you would save to repository/API
//                _userProfile.value = _userProfile.value.copy(
//                    displayName = editProfileData.displayName,
//                    bio = editProfileData.bio,
//                    profileImageRes = editProfileData.profileImageRes
//                )
//
//                toggleEditProfileDialog()
//            } catch (e: Exception) {
//                _uiState.value = _uiState.value.copy(
//                    errorMessage = "Failed to save changes: ${e.message}"
//                )
//                Log.e("ProfileViewModel", "Failed to save profile changes", e)
//            } finally {
//                _uiState.value = _uiState.value.copy(isLoading = false)
//            }
//        }
//    }
//
//    // Updated to use drawable resource instead of URI
//    fun setProfileImage(drawableRes: Int) {
//        viewModelScope.launch {
//            try {
//                _userProfile.value = _userProfile.value.copy(
//                    profileImageRes = drawableRes
//                )
//
//                // Update edit profile data as well
//                editProfileData = editProfileData.copy(
//                    profileImageRes = drawableRes
//                )
//            } catch (e: Exception) {
//                _uiState.value = _uiState.value.copy(
//                    errorMessage = "Failed to set profile image: ${e.message}"
//                )
//                Log.e("ProfileViewModel", "Failed to set profile image", e)
//            }
//        }
//    }
//
//    // Updated to use drawable resource instead of URI
//    fun setBackgroundImage(drawableRes: Int) {
//        viewModelScope.launch {
//            try {
//                _userProfile.value = _userProfile.value.copy(
//                    backgroundImageRes = drawableRes,
//                    backgroundGradient = null // Clear gradient when setting image
//                )
//            } catch (e: Exception) {
//                _uiState.value = _uiState.value.copy(
//                    errorMessage = "Failed to set background: ${e.message}"
//                )
//                Log.e("ProfileViewModel", "Failed to set background image", e)
//            }
//        }
//    }
//
//    fun setBackgroundGradient(colors: List<androidx.compose.ui.graphics.Color>) {
//        viewModelScope.launch {
//            try {
//                _userProfile.value = _userProfile.value.copy(
//                    backgroundGradient = colors,
//                    backgroundImageRes = null // Clear image when setting gradient
//                )
//            } catch (e: Exception) {
//                _uiState.value = _uiState.value.copy(
//                    errorMessage = "Failed to set background: ${e.message}"
//                )
//                Log.e("ProfileViewModel", "Failed to set background gradient", e)
//            }
//        }
//    }
//
//    fun toggleFollowUser() {
//        if (_userProfile.value.isOwnProfile) return
//
//        viewModelScope.launch {
//            try {
//                val currentProfile = _userProfile.value
//                val newFollowState = !currentProfile.isFollowing
//
//                // In a real app, you would make API call here
//                _userProfile.value = currentProfile.copy(
//                    isFollowing = newFollowState,
//                    followersCount = if (newFollowState) {
//                        currentProfile.followersCount + 1
//                    } else {
//                        maxOf(0, currentProfile.followersCount - 1)
//                    }
//                )
//            } catch (e: Exception) {
//                _uiState.value = _uiState.value.copy(
//                    errorMessage = "Failed to update follow status: ${e.message}"
//                )
//                Log.e("ProfileViewModel", "Failed to toggle follow", e)
//            }
//        }
//    }
//
//    fun deletePost(postId: String) {
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                // Delete the sprite from database
//                spriteDatabaseRepository.deleteSpriteById(postId)
//
//                // Update posts count
//                _userProfile.value = _userProfile.value.copy(
//                    postsCount = maxOf(0, _userProfile.value.postsCount - 1)
//                )
//            } catch (e: Exception) {
//                _uiState.value = _uiState.value.copy(
//                    errorMessage = "Failed to delete post: ${e.message}"
//                )
//                Log.e("ProfileViewModel", "Failed to delete post", e)
//            }
//        }
//    }
//
//    fun sharePost(context: Context, postId: String) {
//        // Implementation for sharing a post
//        // This would typically create a share intent
//        viewModelScope.launch {
//            try {
//                // In a real app, you might generate a shareable link
//                Log.d("ProfileViewModel", "Sharing post: $postId")
//            } catch (e: Exception) {
//                _uiState.value = _uiState.value.copy(
//                    errorMessage = "Failed to share post: ${e.message}"
//                )
//            }
//        }
//    }
//
//    fun shareProfile(context: Context) {
//        viewModelScope.launch {
//            try {
//                // In a real app, you would create a profile share intent
//                Log.d("ProfileViewModel", "Sharing profile: ${_userProfile.value.username}")
//            } catch (e: Exception) {
//                _uiState.value = _uiState.value.copy(
//                    errorMessage = "Failed to share profile: ${e.message}"
//                )
//            }
//        }
//    }
//
//    fun savePostAsImage(
//        context: Context,
//        spriteData: ISpriteData,
//        folderUri: Uri,
//        fileName: String,
//        scalePercent: Int = 100
//    ): Boolean {
//        val result = saveFileUseCase.saveImageFile(
//            context,
//            spriteData,
//            scalePercent,
//            folderUri,
//            fileName
//        )
//
//        return result.fold(
//            onSuccess = { true },
//            onFailure = { exception ->
//                _uiState.value = _uiState.value.copy(
//                    errorMessage = "Failed to save image: ${exception.message}"
//                )
//                Log.e("ProfileViewModel", "Failed to save post as image", exception)
//                false
//            }
//        )
//    }
//
//    // Getter functions for available resources (useful for selection dialogs)
//    fun getAvailableProfileImages(): Array<Int> = availableProfileImages
//    fun getAvailableBackgroundImages(): Array<Int> = availableBackgroundImages
//
//    fun clearError() {
//        _uiState.value = _uiState.value.copy(errorMessage = null)
//    }
//
//    fun refreshProfile() {
//        loadUserProfile()
//    }
//
//    // Legacy URI support methods (kept for backward compatibility but not used in UI)
//    @Deprecated("Use setProfileImage(drawableRes: Int) instead")
//    fun setBackgroundImage(uri: Uri) {
//        // This method is kept for backward compatibility but won't affect the UI
//        // since the UI now uses drawable resources
//        Log.w(
//            "ProfileViewModel",
//            "setBackgroundImage(uri) is deprecated. Use drawable resources instead."
//        )
//    }
//}