package com.example.bakalarka.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.bakalarka.other_classes.AppViewModel
import com.example.bakalarka.other_classes.OutlinedTextFieldGenerator
import com.example.bakalarka.other_classes.PrimaryButtonGenerator
import com.example.bakalarka.other_classes.Screens
import com.example.bakalarka.other_classes.SecondaryButtonGenerator
import com.example.bakalarka.other_classes.TextGenerator
import com.example.bakalarka.ui.theme.BakalarkaTheme

@Composable
fun LoginScreen(navController: NavHostController, viewModel: AppViewModel = viewModel()) {

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }





    Column(verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.background(MaterialTheme.colorScheme.background).fillMaxSize()
    )
    {
        Column(verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.background(MaterialTheme.colorScheme.background).fillMaxWidth()
        ) {
            TextGenerator("Login", MaterialTheme.colorScheme.onBackground, "title")

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

        Row(horizontalArrangement = Arrangement.Center,verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()){
            Box(modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                TextGenerator("No account?", MaterialTheme.colorScheme.onBackground, "label")
            }

            Box(modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                SecondaryButtonGenerator(text = "Register", onClick = {navController.navigate(Screens.Register.route) { launchSingleTop = true }})
            }
        }

    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun LoginScreenPreview() {
    BakalarkaTheme(darkTheme = true, dynamicColor = false) {
        LoginScreen(navController = rememberNavController())
    }
}
