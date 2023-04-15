package com.myapplication

import LoginView
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var navState: NavState by remember { mutableStateOf(NavState.LaunchScreen) }
            when(navState){
                NavState.LaunchScreen -> LaunchScreen(onLogin = {
                                                                navState = NavState.Login
                }, onContinueAsGuest = {})
                NavState.Login -> LoginView{
                    navState = when(it){
                        AuthState.Cancelled -> {
                            NavState.LaunchScreen
                        }

                        AuthState.Failed -> {
                            NavState.LaunchScreen
                        }

                        AuthState.Success -> {
                            NavState.HomeScreen
                        }
                    }
                }
                NavState.HomeScreen -> MainScreen()
            }

        }
    }
}

@Composable
fun LaunchScreen(
    onLogin:()->Unit,
    onContinueAsGuest:()->Unit
){
    Box(modifier = Modifier.fillMaxSize()){
        Column {
            Text("This is Main Screen")
            Button(onClick = onLogin){
                Text("Login")
            }
            Button(onClick = onContinueAsGuest){
                Text("Continue as Guest")
            }
        }
    }
}

@Composable
fun MainScreen(){
    Scaffold(topBar = {
        TopAppBar{
            Text("Discover")
        }
    }) {paddingValues ->
        Box(modifier = Modifier.padding(paddingValues).fillMaxSize()){
            Text("Here I can do anything!")
        }
    }
}

sealed class  NavState{
    object LaunchScreen:NavState()
    object Login:NavState()
    object  HomeScreen:NavState()
}