import androidx.compose.ui.window.ComposeUIViewController

actual fun getPlatformName(): String = "iOS"

fun MainViewController(authState: (AuthState)->Unit) = ComposeUIViewController { LoginScreen(authState) }