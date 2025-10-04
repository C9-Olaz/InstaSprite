package com.olaz.instasprite.ui.screens.settingscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.olaz.instasprite.ui.screens.settingscreen.composable.SettingItem
import com.olaz.instasprite.ui.theme.CatppuccinUI

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(onBackClick: () -> Unit = {}) {
    var isDarkThemeEnabled by remember { mutableStateOf(false) }
    var selectedLanguage by remember { mutableStateOf("English") }
    var showLanguageDialog by remember { mutableStateOf(false) }

    val languages = listOf("English", "Spanish", "French", "German", "Chinese", "Japanese", "Vietnamese")

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = CatppuccinUI.TextColorLight
                        )
                    }
                },
                title = {
                    Text(
                        "Settings",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = CatppuccinUI.TextColorLight
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CatppuccinUI.TopBarColor,
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(CatppuccinUI.BackgroundColor)
                .verticalScroll(rememberScrollState())
        ) {
            // Dark Theme Setting
            SettingItem(
                icon = Icons.Default.Warning,
                title = "Dark Theme",
                subtitle = "Enable dark mode",
                trailing = {
                    Switch(
                        checked = isDarkThemeEnabled,
                        onCheckedChange = { isDarkThemeEnabled = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = CatppuccinUI.AccentButtonColor,
                            checkedTrackColor = CatppuccinUI.Foreground2Color,
                            uncheckedThumbColor = CatppuccinUI.Subtext0Color,
                            uncheckedTrackColor = CatppuccinUI.Foreground1Color
                        )
                    )
                }
            )

            HorizontalDivider(
                color = CatppuccinUI.Foreground1Color,
                thickness = 1.dp
            )

            // Language Setting
            SettingItem(
                icon = Icons.Default.Info,
                title = "Language",
                subtitle = selectedLanguage,
                onClick = { showLanguageDialog = true },
                trailing = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Select Language",
                        tint = CatppuccinUI.Subtext0Color,
                        modifier = Modifier.graphicsLayer(rotationZ = 180f)
                    )
                }
            )

            HorizontalDivider(
                color = CatppuccinUI.Foreground1Color,
                thickness = 1.dp
            )
        }

        // Language Selection Dialog
        if (showLanguageDialog) {
            AlertDialog(
                onDismissRequest = { showLanguageDialog = false },
                title = {
                    Text(
                        "Select Language",
                        fontWeight = FontWeight.Bold,
                        color = CatppuccinUI.TextColorLight
                    )
                },
                text = {
                    Column {
                        languages.forEach { language ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        selectedLanguage = language
                                        showLanguageDialog = false
                                    }
                                    .padding(vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = selectedLanguage == language,
                                    onClick = {
                                        selectedLanguage = language
                                        showLanguageDialog = false
                                    },
                                    colors = RadioButtonDefaults.colors(
                                        selectedColor = CatppuccinUI.SelectedColor,
                                        unselectedColor = CatppuccinUI.Subtext0Color
                                    )
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = language,
                                    color = CatppuccinUI.TextColorLight
                                )
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = { showLanguageDialog = false }
                    ) {
                        Text("Cancel", color = CatppuccinUI.DismissButtonColor)
                    }
                },
                containerColor = CatppuccinUI.DialogColor
            )
        }
    }
}



@Preview(showBackground = true)
@Composable
private fun SettingScreenPreview() {
    SettingScreen(onBackClick = {})
}