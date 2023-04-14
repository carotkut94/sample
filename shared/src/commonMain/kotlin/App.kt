import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalResourceApi::class)
@Composable
fun App() {
    val l = FakeVC()
    MaterialTheme {
        LaunchedEffect(Unit){
            l.loadFakeData()
        }

        val viewState by l.screenState().collectAsState()

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
            Text(viewState.content)
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

class FakeVC : ViewController(){
    data class ViewState(val loading:Boolean = false, val content:String = "loading...")

    private val screenState = MutableStateFlow(ViewState())
    fun screenState() = screenState.asStateFlow()
    fun loadFakeData(){
        coroutineScope.launch {
            screenState.value = screenState.value.copy(loading = true)
            withContext(Dispatchers.Default){
                delay(3000)
            }
            screenState.value = screenState.value.copy(loading = false, content = "I am here after api call")
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