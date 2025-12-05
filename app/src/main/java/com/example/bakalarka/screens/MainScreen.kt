package com.example.bakalarka.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.bakalarka.other_classes.PrimaryButtonGenerator
import com.example.bakalarka.other_classes.Screens
import com.example.bakalarka.other_classes.TextGenerator

@Composable
fun MainScreen(navController: NavHostController) {
    Column(verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.background(MaterialTheme.colorScheme.background).fillMaxSize()
    )
    {
        TextGenerator("Main Screen", MaterialTheme.colorScheme.onBackground, "title")

        PrimaryButtonGenerator(text = "Login", onClick = {navController.navigate(Screens.Login.route) { launchSingleTop = true }})

        PrimaryButtonGenerator(text = "Register", onClick = {navController.navigate(Screens.Register.route) { launchSingleTop = true }})


    }
}
