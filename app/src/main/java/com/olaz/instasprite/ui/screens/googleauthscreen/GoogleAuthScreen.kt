import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.olaz.instasprite.R
import com.olaz.instasprite.ui.screens.googleauthscreen.component.GoogleSignInButton
import com.olaz.instasprite.ui.screens.googleauthscreen.GoogleAuthViewModel
import com.olaz.instasprite.ui.screens.completionprofilescreen.ProfileCompletionScreen
import com.olaz.instasprite.ui.screens.completionprofilescreen.ProfileCompletionViewModel
import com.olaz.instasprite.ui.theme.CatppuccinUI
import com.olaz.instasprite.ui.theme.InstaSpriteTheme
import com.olaz.instasprite.utils.GoogleSignInUtils
import com.olaz.instasprite.utils.TokenUtils
import com.olaz.instasprite.utils.UiUtils

@Composable
fun GoogleAuthScreen(
    modifier: Modifier, 
    viewModel: GoogleAuthViewModel,
    onLoginSuccess: () -> Unit = {}
) {
    UiUtils.SetStatusBarColor(CatppuccinUI.BackgroundColorDarker)
    UiUtils.SetNavigationBarColor(CatppuccinUI.BackgroundColorDarker)

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val vm = viewModel
    val uiState by vm.uiState.collectAsState()
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showProfileCompletion by remember { mutableStateOf(false) }
    
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) { result ->
            isLoading = false
        }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CatppuccinUI.BackgroundColorDarker)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // App Logo
            Image(
                painter = painterResource(id = R.drawable.ic_launcher),
                contentDescription = "App Logo",
                modifier = Modifier.size(100.dp)
            )

            Spacer(modifier = Modifier.height(48.dp))

            // App Title
            Text(
                text = "InstaSprite",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = CatppuccinUI.TextColorLight,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Subtitle
            Text(
                text = "Create pixel art with ease",
                fontSize = 16.sp,
                color = CatppuccinUI.TextColorLight.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Error message display
            errorMessage?.let { error ->
                Text(
                    text = error,
                    color = CatppuccinUI.TextColorLight.copy(alpha = 0.8f),
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Google Sign-In Button
            GoogleSignInButton(
                onClick = {
                    isLoading = true
                    errorMessage = null
                    GoogleSignInUtils.doGoogleSignIn(
                        context = context, 
                        scope = scope, 
                        launcher = launcher, 
                        login = { userName, idToken ->
                            Toast.makeText(context, "Welcome, $userName!", Toast.LENGTH_LONG).show()
                            vm.loginWithGoogleIdToken(idToken)
                        },
                        onError = { error ->
                            isLoading = false
                            errorMessage = error
                        }
                    )
                },
                isLoading = isLoading || uiState.isLoading,
                modifier = Modifier.fillMaxWidth()
            )

            LaunchedEffect(uiState.jwt, uiState.isFirstTime) {
                uiState.jwt?.let {
                    isLoading = false
                    // Show profile completion only for first-time users
                    showProfileCompletion = uiState.isFirstTime
                    if (!uiState.isFirstTime) {
                        // For returning users, go directly to home
                        onLoginSuccess()
                    }
                }
            }

            LaunchedEffect(uiState.errorMessage) {
                uiState.errorMessage?.let { err ->
                    isLoading = false
                    errorMessage = err
                }
            }
        }
    }


    if (showProfileCompletion) {
        ProfileCompletionScreen(
            onProfileCompleted = {
                showProfileCompletion = false
                onLoginSuccess()
            },
            viewModel = ProfileCompletionViewModel()
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun GoogleAuthScreenPreview() {
    InstaSpriteTheme {
        GoogleAuthScreen(
            modifier = Modifier,
            viewModel = GoogleAuthViewModel(
                tokenUtils = TokenUtils(LocalContext.current)
            )
        )
    }
}