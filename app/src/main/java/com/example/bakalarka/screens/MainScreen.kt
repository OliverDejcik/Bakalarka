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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.bakalarka.other_classes.AppViewModel
import com.example.bakalarka.other_classes.PrimaryButtonGenerator
import com.example.bakalarka.other_classes.Screens
import com.example.bakalarka.other_classes.TextGenerator
import com.example.bakalarka.ui.theme.BakalarkaTheme

@Composable
fun MainScreen(navController: NavHostController,viewModel: AppViewModel = viewModel()) {
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

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun MainScreenPreview() {
    BakalarkaTheme(darkTheme = true, dynamicColor = false) {
        MainScreen(navController = rememberNavController())
    }
}
