import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalResourceApi::class)
@Composable
fun LoginScreen(authState:(AuthState)->Unit) {
    MaterialTheme {
        val l = remember { FakeVC(authState) }

        val viewState by l.screenState().collectAsState()

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
            Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                Row{
                    IconButton(onClick = {authState(AuthState.Cancelled)}){
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }
                Text("Login", style = MaterialTheme.typography.h1)
                TextField(
                    viewState.userName,
                    onValueChange = l::updateUserName
                )
                Spacer(modifier = Modifier.height(16.dp))
                TextField(
                    viewState.password,
                    onValueChange = l::updatePassword
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = l::doLogin, colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)){
                    Text("Login", color = Color.White)
                }
            }
            if(viewState.loading){
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = Color.Red)
                }
            }
        }

        DisposableEffect(Unit){
            onDispose {
                l.clear()
            }
        }
    }
}

class FakeVC(val authState: (AuthState) -> Unit) : ViewController(){
    data class ViewState(val loading:Boolean = false, val userName:String = "", val password: String="")

    private val screenState = MutableStateFlow(ViewState())
    fun screenState() = screenState.asStateFlow()

    fun updateUserName(text:String) {
        screenState.value = screenState.value.copy(userName = text)
    }

    fun updatePassword(text:String) {
        screenState.value = screenState.value.copy(password = text)
    }

    fun doLogin(){
        coroutineScope.launch {
            screenState.value = screenState.value.copy(loading = true)
            withContext(Dispatchers.Default){
                delay(3000)
            }
            screenState.value = screenState.value.copy(loading = false)
            authState(AuthState.Success)
        }

    }
}

open class ViewController : Clearable {
    protected val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    override fun clear() {
        coroutineScope.coroutineContext.cancel()
    }
}

interface Clearable {
    fun clear()
}
expect fun getPlatformName(): String

sealed class AuthState {
 object Failed:AuthState()
 object Success:AuthState()
 object Cancelled:AuthState()

}