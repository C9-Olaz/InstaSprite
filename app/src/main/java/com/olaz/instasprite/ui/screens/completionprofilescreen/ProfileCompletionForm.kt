package com.olaz.instasprite.ui.screens.completionprofilescreen
import android.util.Patterns
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.olaz.instasprite.data.network.model.EditProfileResponse
import com.olaz.instasprite.ui.components.CustomTextField
import com.olaz.instasprite.ui.components.AvatarSelectionComponent
import com.olaz.instasprite.ui.theme.CatppuccinUI
import androidx.compose.ui.platform.LocalContext

private fun isValidEmail(email: String): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

@Composable
fun ProfileCompletionForm(
    errorMessage: String,
    isLoading: Boolean,
    profileData: EditProfileResponse?,
    selectedImageUri: android.net.Uri?,
    isUploadingImage: Boolean,
    imageUploadError: String?,
    onUpdateClick: (String, String, String?, String) -> Unit,
    onErrorChanged: (String) -> Unit,
    onImageSelected: (android.net.Uri?) -> Unit,
    onUploadImage: () -> Unit,
    onClearImageError: () -> Unit
) {
    var username by remember { mutableStateOf(profileData?.memberUsername ?: "") }
    var name by remember { mutableStateOf(profileData?.memberName ?: "") }
    var introduce by remember { mutableStateOf(profileData?.memberIntroduce ?: "") }
    var email by remember { mutableStateOf(profileData?.memberEmail ?: "") }
    val context = LocalContext.current

    LaunchedEffect(profileData) {
        profileData?.let {
            username = it.memberUsername
            name = it.memberName
            introduce = it.memberIntroduce ?: ""
            email = it.memberEmail
        }
    }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Complete Your Profile",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = CatppuccinUI.TextColorLight,
            textAlign = TextAlign.Center
        )

        Text(
            text = "Let's set up your profile information",
            fontSize = 16.sp,
            color = CatppuccinUI.TextColorLight.copy(alpha = 0.7f),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        AvatarSelectionComponent(
            selectedImageUri = selectedImageUri,
            onImageSelected = { uri ->
                onImageSelected(uri)
                onErrorChanged("")
                onClearImageError()
            },
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        if (imageUploadError != null) {
            Text(
                text = imageUploadError,
                color = MaterialTheme.colorScheme.error,
                fontSize = 14.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        CustomTextField(
            value = username,
            onValueChange = {
                username = it
                onErrorChanged("")
            },
            label = "Username",
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next
        )

        Spacer(modifier = Modifier.height(16.dp))

        CustomTextField(
            value = name,
            onValueChange = {
                name = it
                onErrorChanged("")
            },
            label = "Full Name",
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Email Field
        CustomTextField(
            value = email,
            onValueChange = {
                email = it
                onErrorChanged("")
            },
            label = "Email",
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        )

        Spacer(modifier = Modifier.height(16.dp))

        CustomTextField(
            value = introduce,
            onValueChange = {
                introduce = it
                onErrorChanged("")
            },
            label = "Introduction (Optional)",
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done,
            maxLines = 3
        )

        // Error Message
        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                fontSize = 14.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                when {
                    username.isBlank() -> onErrorChanged("Please enter a username")
                    username.length < 4 || username.length > 12 -> onErrorChanged("Username must be between 4 and 12 characters")
                    name.isBlank() -> onErrorChanged("Please enter your full name")
                    name.length < 2 || name.length > 12 -> onErrorChanged("Name must be between 2 and 12 characters")
                    email.isBlank() -> onErrorChanged("Please enter your email")
                    !isValidEmail(email) -> onErrorChanged("Please enter a valid email")
                    else -> {
                        // Upload image first if selected, then update profile
                        if (selectedImageUri != null) {
                            onUploadImage()
                        }
                        onUpdateClick(username, name, introduce.ifBlank { null }, email)
                    }
                }
            },
            enabled = !isLoading && !isUploadingImage,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = CatppuccinUI.BottomBarColor,
                contentColor = CatppuccinUI.TextColorLight
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            if (isLoading || isUploadingImage) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = CatppuccinUI.TextColorLight,
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = if (isUploadingImage) "Uploading Image..." else "Complete Profile",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

