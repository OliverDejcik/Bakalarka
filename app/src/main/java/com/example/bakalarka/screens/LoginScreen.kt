package com.example.bakalarka.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.bakalarka.OutlinedTextFieldGenerator
import com.example.bakalarka.PrimaryButtonGenerator
import com.example.bakalarka.Screens
import com.example.bakalarka.TextGenerator

@Composable
fun LoginScreen(navController: NavHostController) {

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }





    Column(verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.background(MaterialTheme.colorScheme.background).fillMaxSize()
    )
    {
        TextGenerator("Login Screen", MaterialTheme.colorScheme.onBackground, "title")

        OutlinedTextFieldGenerator(value = username,
            onValueChange = {username = it},
            label = "username",
            isPassword = false,
            keyboardType = KeyboardType.Text,
            leadingIcon = Icons.Default.Person
        )


        OutlinedTextFieldGenerator(
            password,
            { password = it },
            "password",
            isPassword = true,
            keyboardType = KeyboardType.Password,
            leadingIcon = Icons.Default.Lock
        )

        PrimaryButtonGenerator(text = "Login", onClick = {navController.navigate(Screens.Home.route) { launchSingleTop = true }})
    }
}
