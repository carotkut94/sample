import androidx.compose.runtime.Composable

actual fun getPlatformName(): String = "Android"

@Composable fun LoginView(authState:(AuthState)->Unit) = LoginScreen(authState)
