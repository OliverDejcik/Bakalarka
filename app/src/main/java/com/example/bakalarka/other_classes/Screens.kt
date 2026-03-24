package com.example.bakalarka.other_classes


sealed class Screens(val route: String) {
    data object Home : Screens("home")
    data object Login : Screens("login")
    data object Register : Screens("register")
    data object Training : Screens("training")
    data object AddTraining : Screens("add_training")
    data object Statistics : Screens("statistics")

    data object Profile : Screens("profile")

    data object Settings : Screens("settings")

    data object Testing : Screens("testing")

}