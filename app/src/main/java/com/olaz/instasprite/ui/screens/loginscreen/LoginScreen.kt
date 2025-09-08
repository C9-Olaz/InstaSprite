import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.olaz.instasprite.R
import com.olaz.instasprite.ui.theme.CatppuccinUI
import com.olaz.instasprite.ui.theme.InstaSpriteTheme
import com.olaz.instasprite.utils.UiUtils

enum class AuthMode {
    LOGIN, REGISTER
}

@Composable
fun LoginRegisterScreen(
    onLoginClick: (String, String) -> Unit = { _, _ -> },
    onRegisterClick: (String, String, String, String) -> Unit = { _, _, _, _ -> },
    onForgotPasswordClick: () -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    UiUtils.SetStatusBarColor(CatppuccinUI.BackgroundColorDarker)
    UiUtils.SetNavigationBarColor(CatppuccinUI.BackgroundColorDarker)

    var authMode by remember { mutableStateOf(AuthMode.LOGIN) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CatppuccinUI.BackgroundColorDarker)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // App Logo
            Image(
                painter = painterResource(id = R.drawable.ic_launcher),
                contentDescription = "App Logo",
                modifier = Modifier.size(100.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Animated Content Based on Mode
            AnimatedContent(
                targetState = authMode,
                transitionSpec = {
                    slideInHorizontally(
                        initialOffsetX = { if (targetState == AuthMode.REGISTER) 300 else -300 },
                        animationSpec = tween(300)
                    ) + fadeIn(animationSpec = tween(300)) togetherWith
                            slideOutHorizontally(
                                targetOffsetX = { if (targetState == AuthMode.REGISTER) -300 else 300 },
                                animationSpec = tween(300)
                            ) + fadeOut(animationSpec = tween(300))
                },
                label = "auth_content"
            ) { mode ->
                when (mode) {
                    AuthMode.LOGIN -> {
                        LoginForm(
                            errorMessage = errorMessage,
                            isLoading = isLoading,
                            onLoginClick = { email, password ->
                                isLoading = true
                                errorMessage = ""
                                onLoginClick(email, password)
                            },
                            onForgotPasswordClick = onForgotPasswordClick,
                            onErrorChanged = { errorMessage = it },
                            onSwitchToRegister = {
                                authMode = AuthMode.REGISTER
                                errorMessage = ""
                            }
                        )
                    }
                    AuthMode.REGISTER -> {
                        RegisterForm(
                            errorMessage = errorMessage,
                            isLoading = isLoading,
                            onRegisterClick = { username, email, password, confirmPassword ->
                                isLoading = true
                                errorMessage = ""
                                onRegisterClick(username, email, password, confirmPassword)
                            },
                            onErrorChanged = { errorMessage = it },
                            onSwitchToLogin = {
                                authMode = AuthMode.LOGIN
                                errorMessage = ""
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LoginForm(
    errorMessage: String,
    isLoading: Boolean,
    onLoginClick: (String, String) -> Unit,
    onForgotPasswordClick: () -> Unit,
    onErrorChanged: (String) -> Unit,
    onSwitchToRegister: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Welcome Text
        Text(
            text = "Welcome Back",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = CatppuccinUI.TextColorLight,
            textAlign = TextAlign.Center
        )

        Text(
            text = "Sign in to continue",
            fontSize = 16.sp,
            color = CatppuccinUI.TextColorLight.copy(alpha = 0.7f),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Email Field
        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                onErrorChanged("")
            },
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            ),
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = CatppuccinUI.BottomBarColor,
                unfocusedBorderColor = CatppuccinUI.TextColorLight.copy(alpha = 0.3f),
                focusedLabelColor = CatppuccinUI.BottomBarColor,
                unfocusedLabelColor = CatppuccinUI.TextColorLight.copy(alpha = 0.7f),
                focusedTextColor = CatppuccinUI.TextColorLight,
                unfocusedTextColor = CatppuccinUI.TextColorLight,
                cursorColor = CatppuccinUI.BottomBarColor
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Password Field
        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                onErrorChanged("")
            },
            label = { Text("Password") },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    if (email.isNotBlank() && password.isNotBlank()) {
                        onLoginClick(email, password)
                    }
                }
            ),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.Done else Icons.Default.Clear,
                        contentDescription = if (passwordVisible) "Hide password" else "Show password",
                        tint = CatppuccinUI.TextColorLight.copy(alpha = 0.7f)
                    )
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = CatppuccinUI.BottomBarColor,
                unfocusedBorderColor = CatppuccinUI.TextColorLight.copy(alpha = 0.3f),
                focusedLabelColor = CatppuccinUI.BottomBarColor,
                unfocusedLabelColor = CatppuccinUI.TextColorLight.copy(alpha = 0.7f),
                focusedTextColor = CatppuccinUI.TextColorLight,
                unfocusedTextColor = CatppuccinUI.TextColorLight,
                cursorColor = CatppuccinUI.BottomBarColor
            ),
            modifier = Modifier.fillMaxWidth()
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

        // Login Button
        Button(
            onClick = {
                when {
                    email.isBlank() -> onErrorChanged("Please enter your email")
                    password.isBlank() -> onErrorChanged("Please enter your password")
                    else -> onLoginClick(email, password)
                }
            },
            enabled = !isLoading,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = CatppuccinUI.BottomBarColor,
                contentColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = "Sign In",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // Forgot Password
        TextButton(
            onClick = onForgotPasswordClick,
            colors = ButtonDefaults.textButtonColors(
                contentColor = CatppuccinUI.BottomBarColor
            )
        ) {
            Text(
                text = "Forgot Password?",
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        HorizontalDivider(modifier = Modifier.height(8.dp))

        // Don't have account - Register
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Don't have an account?",
                fontSize = 14.sp,
                color = CatppuccinUI.TextColorLight.copy(alpha = 0.7f)
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onSwitchToRegister,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.textButtonColors(
                    containerColor = CatppuccinUI.BottomBarColor,
                    contentColor = CatppuccinUI.BottomBarColor
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(
                    text = "Sign Up",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun RegisterForm(
    errorMessage: String,
    isLoading: Boolean,
    onRegisterClick: (String, String, String, String) -> Unit,
    onErrorChanged: (String) -> Unit,
    onSwitchToLogin: () -> Unit
) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var acceptTerms by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Welcome Text
        Text(
            text = "Create Account",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = CatppuccinUI.TextColorLight,
            textAlign = TextAlign.Center
        )

        Text(
            text = "Join our creative community",
            fontSize = 16.sp,
            color = CatppuccinUI.TextColorLight.copy(alpha = 0.7f),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Username Field
        OutlinedTextField(
            value = username,
            onValueChange = {
                username = it
                onErrorChanged("")
            },
            label = { Text("Username") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            ),
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = CatppuccinUI.BottomBarColor,
                unfocusedBorderColor = CatppuccinUI.TextColorLight.copy(alpha = 0.3f),
                focusedLabelColor = CatppuccinUI.BottomBarColor,
                unfocusedLabelColor = CatppuccinUI.TextColorLight.copy(alpha = 0.7f),
                focusedTextColor = CatppuccinUI.TextColorLight,
                unfocusedTextColor = CatppuccinUI.TextColorLight,
                cursorColor = CatppuccinUI.BottomBarColor
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Email Field
        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                onErrorChanged("")
            },
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            ),
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = CatppuccinUI.BottomBarColor,
                unfocusedBorderColor = CatppuccinUI.TextColorLight.copy(alpha = 0.3f),
                focusedLabelColor = CatppuccinUI.BottomBarColor,
                unfocusedLabelColor = CatppuccinUI.TextColorLight.copy(alpha = 0.7f),
                focusedTextColor = CatppuccinUI.TextColorLight,
                unfocusedTextColor = CatppuccinUI.TextColorLight,
                cursorColor = CatppuccinUI.BottomBarColor
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Password Field
        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                onErrorChanged("")
            },
            label = { Text("Password") },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            ),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.Check else Icons.Default.Clear,
                        contentDescription = if (passwordVisible) "Hide password" else "Show password",
                        tint = CatppuccinUI.TextColorLight.copy(alpha = 0.7f)
                    )
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = CatppuccinUI.BottomBarColor,
                unfocusedBorderColor = CatppuccinUI.TextColorLight.copy(alpha = 0.3f),
                focusedLabelColor = CatppuccinUI.BottomBarColor,
                unfocusedLabelColor = CatppuccinUI.TextColorLight.copy(alpha = 0.7f),
                focusedTextColor = CatppuccinUI.TextColorLight,
                unfocusedTextColor = CatppuccinUI.TextColorLight,
                cursorColor = CatppuccinUI.BottomBarColor
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Confirm Password Field
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = {
                confirmPassword = it
                onErrorChanged("")
            },
            label = { Text("Confirm Password") },
            visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus() }
            ),
            trailingIcon = {
                IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                    Icon(
                        imageVector = if (confirmPasswordVisible) Icons.Default.Check else Icons.Default.Clear,
                        contentDescription = if (confirmPasswordVisible) "Hide password" else "Show password",
                        tint = CatppuccinUI.TextColorLight.copy(alpha = 0.7f)
                    )
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = CatppuccinUI.BottomBarColor,
                unfocusedBorderColor = CatppuccinUI.TextColorLight.copy(alpha = 0.3f),
                focusedLabelColor = CatppuccinUI.BottomBarColor,
                unfocusedLabelColor = CatppuccinUI.TextColorLight.copy(alpha = 0.7f),
                focusedTextColor = CatppuccinUI.TextColorLight,
                unfocusedTextColor = CatppuccinUI.TextColorLight,
                cursorColor = CatppuccinUI.BottomBarColor
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Terms and Conditions
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = acceptTerms,
                onCheckedChange = {
                    acceptTerms = it
                    onErrorChanged("")
                },
                colors = CheckboxDefaults.colors(
                    checkedColor = CatppuccinUI.BottomBarColor,
                    uncheckedColor = CatppuccinUI.TextColorLight.copy(alpha = 0.6f),
                    checkmarkColor = Color.White
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "I agree to the Terms of Service and Privacy Policy",
                fontSize = 14.sp,
                color = CatppuccinUI.TextColorLight.copy(alpha = 0.8f),
                modifier = Modifier.weight(1f)
            )
        }

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

        // Register Button
        Button(
            onClick = {
                when {
                    username.isBlank() -> onErrorChanged("Please enter a username")
                    email.isBlank() -> onErrorChanged("Please enter your email")
                    !isValidEmail(email) -> onErrorChanged("Please enter a valid email")
                    password.isBlank() -> onErrorChanged("Please enter a password")
                    password.length < 6 -> onErrorChanged("Password must be at least 6 characters")
                    confirmPassword != password -> onErrorChanged("Passwords do not match")
                    !acceptTerms -> onErrorChanged("Please accept the terms and conditions")
                    else -> onRegisterClick(username, email, password, confirmPassword)
                }
            },
            enabled = !isLoading,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = CatppuccinUI.BottomBarColor,
                contentColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = "Sign Up",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        HorizontalDivider(modifier = Modifier.height(8.dp))

        // Already have account - Login
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Already have an account?",
                fontSize = 14.sp,
                color = CatppuccinUI.TextColorLight.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onSwitchToLogin,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.textButtonColors(
                    containerColor = CatppuccinUI.BottomBarColor,
                    contentColor = CatppuccinUI.BottomBarColor
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(
                    text = "Sign In",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

// Helper function for email validation
private fun isValidEmail(email: String): Boolean {
    return email.contains("@") && email.contains(".")
}

@Preview(showBackground = true)
@Composable
private fun LoginRegisterScreenPreview() {
    InstaSpriteTheme {
        LoginRegisterScreen()
    }
}