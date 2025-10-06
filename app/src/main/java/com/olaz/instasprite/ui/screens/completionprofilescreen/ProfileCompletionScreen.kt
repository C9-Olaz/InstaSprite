package com.olaz.instasprite.ui.screens.completionprofilescreen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileCompletionScreen(
    onProfileCompleted: () -> Unit,
    viewModel: ProfileCompletionViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current


    LaunchedEffect(Unit) {
        viewModel.loadProfileData()
    }


    LaunchedEffect(uiState.isProfileUpdated) {
        if (uiState.isProfileUpdated) {
            // Show success toast
            Toast.makeText(
                context,
                "Profile updated successfully!",
                Toast.LENGTH_SHORT
            ).show()
            onProfileCompleted()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Complete Profile") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            if (uiState.isLoading && uiState.profileData == null) {
                CircularProgressIndicator()
            } else {
                ProfileCompletionForm(
                    errorMessage = uiState.errorMessage ?: "",
                    isLoading = uiState.isLoading,
                    profileData = uiState.profileData,
                    onUpdateClick = { username, name, introduce, email ->
                        viewModel.updateProfile(username, name, introduce, email)
                    },
                    onErrorChanged = { error ->
                        viewModel.clearError()
                    }
                )
            }
        }
    }
}
