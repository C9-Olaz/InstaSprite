package com.olaz.instasprite.ui.screens.homescreen

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.olaz.instasprite.AuthActivity
import com.olaz.instasprite.R
import com.olaz.instasprite.SettingActivity
import com.olaz.instasprite.data.database.AppDatabase
import com.olaz.instasprite.data.repository.ISpriteDatabaseRepository
import com.olaz.instasprite.data.repository.SortSettingRepository
import com.olaz.instasprite.data.repository.StorageLocationRepository
import com.olaz.instasprite.ui.components.composable.JumpToTopButton
import com.olaz.instasprite.ui.screens.homescreen.dialog.CreateCanvasDialog
import com.olaz.instasprite.ui.screens.homescreen.dialog.SelectSortOptionDialog
import com.olaz.instasprite.ui.theme.CatppuccinUI
import com.olaz.instasprite.ui.theme.InstaSpriteTheme
import com.olaz.instasprite.utils.UiUtils

@Composable
fun HomeScreen(viewModel: HomeScreenViewModel) {
    UiUtils.SetStatusBarColor(CatppuccinUI.TopBarColor)
    UiUtils.SetNavigationBarColor(CatppuccinUI.BottomBarColor)

    val uiState by viewModel.uiState.collectAsState()

    if (uiState.showCreateCanvasDialog) {
        CreateCanvasDialog(
            onDismiss = {
                viewModel.toggleCreateCanvasDialog()
            },
        )
    }

    if (uiState.showSelectSortOptionDialog) {
        SelectSortOptionDialog(
            viewModel = viewModel,
            onDismiss = {
                viewModel.toggleSelectSortOptionDialog()
            },
        )
    }

    if (uiState.showImagePager) {
        ImagePagerOverlay(
            viewModel = viewModel,
            onDismiss = { lastSpriteSeen ->
                viewModel.toggleImagePager(null)
                viewModel.lastSpriteSeenInPager = lastSpriteSeen
            }
        )
    }

    val sprites by viewModel.sprites.collectAsState()

    val lazyListState = rememberLazyListState()
    val firstItemVisible by remember { derivedStateOf { lazyListState.firstVisibleItemIndex > 0 } }

    val drawerState = rememberDrawerState(DrawerValue.Closed)

    var loginState by remember { mutableStateOf(false) }
    val context = LocalContext.current


    Box {
        ModalNavigationDrawer(
            drawerContent = {
                ModalDrawerSheet (
                    drawerContainerColor = CatppuccinUI.TopBarColor
                ){
                    Column(
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        Row() {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_launcher),
                                contentDescription = "Logo",
                                tint = Color.Unspecified,
                                modifier = Modifier
                                    .padding(16.dp)
                                    .size(50.dp)
                            )
                            Column(
                            ) {
                                Text(
                                    fontSize = 16.sp,
                                    text = "InstaSprite",
                                    modifier = Modifier
                                        .padding(start = 16.dp, bottom = 8.dp, top = 8.dp)
                                )
                                Text(
                                    fontSize = 12.sp,
                                    text = "Create and explore arts !",
                                    modifier = Modifier
                                        .padding(start = 16.dp)

                                )
                            }
                        }
                        HorizontalDivider(
                            color = Color.Transparent,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )

                        if (loginState) {
                            IconButton(
                                onClick = { },
                                modifier = Modifier
//                                    .padding(start = 10.dp, top = 16.dp)
                                    .size(100.dp)
                                    .align(Alignment.CenterHorizontally)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AccountCircle,
                                    contentDescription = "Profile",
                                    tint = CatppuccinUI.TextColorLight,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }

                            Text(
                                "User Name",
                                modifier = Modifier
                                    .align (Alignment.CenterHorizontally)
//                                    .padding(start = 16.dp, bottom = 2.dp),
                                //                            style = MaterialTheme.typography.labelMedium
                            )
                            Text(
                                "@Usertag",
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier
                                    .align (Alignment.CenterHorizontally)
//                                    .padding(start = 16.dp, bottom = 16.dp),

                            )

                            NavigationDrawerItem(
                                label = {
                                    Row {
                                        Icon(
                                            imageVector = Icons.Default.Person,
                                            contentDescription = "Profile",
                                            tint = CatppuccinUI.TextColorLight,
                                            modifier = Modifier
                                                .padding(top = 6.dp)
                                                .size(20.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("Profile")
                                    }
                                },
                                selected = false,
                                onClick = { /* Handle click */ }
                            )
                        }

                        else {
                            NavigationDrawerItem(
                                label = {
                                    Row {
                                        Icon(
                                            imageVector = Icons.Default.Person,
                                            contentDescription = "Login/Register",
                                            tint = CatppuccinUI.TextColorLight,
                                            modifier = Modifier
                                                .padding(top = 6.dp)
                                                .size(20.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("Login/Register")
                                    }
                                },
                                selected = false,
                                onClick = {
                                    val intent = Intent(context, AuthActivity::class.java)
                                    context.startActivity(intent)
                                }
                            )
                        }

                        NavigationDrawerItem(
                            label = {
                                Row {
                                    Icon(
                                        imageVector = Icons.Default.Home,
                                        contentDescription = "Home",
                                        tint = CatppuccinUI.TextColorLight,
                                        modifier = Modifier
                                            .padding(top = 6.dp)
                                            .size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Home")
                                }
                            },
                            selected = false,
                            onClick = { /* Handle click */ }
                        )
                        NavigationDrawerItem(
                            label = {
                                Row {
                                    Icon(
                                        imageVector = Icons.Default.Create,
                                        contentDescription = "Gallery",
                                        tint = CatppuccinUI.TextColorLight,
                                        modifier = Modifier
                                            .padding(top = 6.dp)
                                            .size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Gallery")
                                }
                            },
                            selected = false,
                            onClick = { /* Handle click */ }
                        )
                        NavigationDrawerItem(
                            label = {
                                Row {
                                    Icon(
                                        imageVector = Icons.Default.Settings,
                                        contentDescription = "Setting",
                                        tint = CatppuccinUI.TextColorLight,
                                        modifier = Modifier
                                            .padding(top = 6.dp)
                                            .size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Setting")
                                }
                            },
                            selected = false,
                            onClick = {
                                val intent = Intent(context, SettingActivity::class.java)
                                context.startActivity(intent)
                            }
                        )
                        Spacer(modifier = Modifier.weight(1f))

                        // Bottom section with divider
                        HorizontalDivider(
                            color = CatppuccinUI.TextColorLight.copy(alpha = 0.3f),
                            modifier = Modifier.padding(vertical = 8.dp)
                        )

                        // About item
                        NavigationDrawerItem(
                            label = {
                                Row {
                                    Icon(
                                        imageVector = Icons.Default.Info,
                                        contentDescription = "About",
                                        tint = CatppuccinUI.TextColorLight,
                                        modifier = Modifier
                                            .padding(top = 6.dp)
                                            .size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("About")
                                }
                            },
                            selected = false,
                            onClick = { /* Handle click */ }
                        )

                        // Logout item (only show when logged in)
                        if (loginState) {
                            NavigationDrawerItem(
                                label = {
                                    Row {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Logout",
                                            tint = CatppuccinUI.TextColorLight,
                                            modifier = Modifier
                                                .padding(top = 6.dp)
                                                .size(20.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("Logout")
                                    }
                                },
                                selected = false,
                                onClick = { loginState = !loginState }
                            )
                        }

                        // Bottom padding
                        Spacer(modifier = Modifier.height(16.dp))

                    }
                }
            },

            drawerState = drawerState
        ) {
            Scaffold(
                topBar = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .background(CatppuccinUI.TopBarColor)
                            .height(56.dp)
                    ) {
                        Text(
                            text = "Home",
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .weight(1f)
                                .padding(10.dp)
                        )

                        Text(
                            text = "Discovery",
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .weight(1f)
                                .padding(10.dp)
                        )
                    }

                    AnimatedVisibility(
                        visible = uiState.showSearchBar,
                        enter = slideInVertically(initialOffsetY = { -it }),
                        exit = slideOutVertically(targetOffsetY = { -it }),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                    ) {
                        SearchBar(
                            viewModel = viewModel,
                        )
                    }
                },
                bottomBar = {
                    HomeBottomBar(
                        viewModel = viewModel,
                        lazyListState = lazyListState,
                        drawerState = drawerState,
                        modifier = Modifier.height(56.dp)
                    )
                },
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .padding(top = innerPadding.calculateTopPadding())
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .background(CatppuccinUI.BackgroundColorDarker)
                        .animateContentSize()
                ) {
                    SpriteList(
                        viewModel = viewModel,
                        spritesWithMetaData = sprites,
                        lazyListState = lazyListState,
                        modifier = Modifier.padding(horizontal = 10.dp)
                    )
                }
            }

            AnimatedVisibility(
                visible = firstItemVisible,
                enter = scaleIn(),
                exit = scaleOut(),
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 64.dp) // TopBar (56dp) + 8dp spacing
            ) {
                JumpToTopButton(
                    listState = lazyListState
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 21.dp)
                    .background(Color.Transparent),
                contentAlignment = Alignment.Center,
            ) {
                HomeFab(
                    onClick = { viewModel.toggleCreateCanvasDialog() },
                    lazyListState = lazyListState
                )
            }
        }
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    val context = LocalContext.current
    val database = AppDatabase.getInstance(context)
    val spriteDataRepository =
        ISpriteDatabaseRepository(database.spriteDataDao(), database.spriteMetaDataDao())
    val sortSettingRepository = SortSettingRepository(context)
    val storageLocationRepository = StorageLocationRepository(context)

    val viewModel = HomeScreenViewModel(
        spriteDatabaseRepository = spriteDataRepository,
        sortSettingRepository = sortSettingRepository,
        storageLocationRepository = storageLocationRepository
    )

    InstaSpriteTheme {
        HomeScreen(viewModel)
    }
}