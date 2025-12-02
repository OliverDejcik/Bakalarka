package com.example.bakalarka


sealed class Screens(val route: String) {
    data object Main : Screens("main")
    data object Home : Screens("home")
    data object Login : Screens("login")
    data object Register : Screens("register")
    data object Training : Screens("training")
    data object AddTraining : Screens("add_training")
    data object Statistics : Screens("statistics")

    data object Profile : Screens("profile")

    data object Settings : Screens("settings")

}