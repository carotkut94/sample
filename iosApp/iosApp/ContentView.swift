import UIKit
import SwiftUI
import shared

struct ComposeView: UIViewControllerRepresentable {
    @Binding var uiAuthState: AuthState
    func makeUIViewController(context: Context) -> UIViewController {
        Main_iosKt.MainViewController(authState:{ authState in
            uiAuthState = authState
        })
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {
        
    }
    

}

struct ContentView: View {
    @State var screenState = Screen.Launch
    @State var authState : AuthState =  AuthState.Failed()
    var body: some View {
        if(screenState == Screen.Launch){
            Launch(uiAuthState: $authState.onChange({ authState in
                if(self.authState is AuthState.Success){
                    screenState = Screen.Home
                }
            }))
        }else{
            HomeScreen()
            
        }
    }
}

enum Screen {
    case Launch
    case Home
}

struct Launch: View {
    @State var shouldPresentLoginScreen = false
    @Binding var uiAuthState: AuthState
    
    var body: some View {
        VStack{
            Text("Welcome to App (Swift UI)")
            Button("Login") {shouldPresentLoginScreen.toggle()}.sheet(isPresented: $shouldPresentLoginScreen) {
                print("Sheet dismissed!")
            } content: {
                ComposeView(uiAuthState: $uiAuthState.onChange({ newState in
                    shouldPresentLoginScreen = false
                }))
                        .ignoresSafeArea(.keyboard)
            }
            Button("Contunue as Guest?") {}
        }
    }
}


extension Binding {
    func onChange(_ handler: @escaping (Value) -> Void) -> Binding<Value> {
        Binding(
            get: { self.wrappedValue },
            set: { newValue in
                self.wrappedValue = newValue
                handler(newValue)
            }
        )
    }
}




struct HomeScreen: View {
    var body: some View {
        VStack{
            Text("Welcome to App, I am from Swift UI World")
            Button("I am logged in") {}
        }
    }
}



