package com.example.bakalarka.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.bakalarka.other_classes.AppViewModel
import com.example.bakalarka.other_classes.OutlinedTextFieldGenerator
import com.example.bakalarka.other_classes.PrimaryButtonGenerator
import com.example.bakalarka.other_classes.Screens
import com.example.bakalarka.ui.theme.BakalarkaTheme

@Composable
fun RegisterScreen(navController: NavHostController, viewModel: AppViewModel = viewModel()) {

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    val context = LocalContext.current

    // Sledujeme stav registrácie z AppViewModelu
    val registrationState by viewModel.registrationSuccess

    // Tento blok sa spustí vždy, keď sa zmení hodnota registrationState
    LaunchedEffect(registrationState) {
        when (registrationState) {
            true -> { // Úspech
                Toast.makeText(context, "Registration successful!", Toast.LENGTH_SHORT).show()
                // Presmerujeme na Login a vymažeme back stack
                navController.navigate(Screens.Login.route) {
                    popUpTo(Screens.Register.route) { inclusive = true }
                }
                viewModel.resetRegistrationState() // Resetujeme stav
            }
            false -> { // Neúspech
                Toast.makeText(context, "Registration failed. Please try again.", Toast.LENGTH_LONG).show()
                viewModel.resetRegistrationState() // Resetujeme stav
            }
            null -> {
                // Nerobíme nič, čakáme na akciu
            }
        }
    }

    Column(verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.background(MaterialTheme.colorScheme.background).fillMaxSize()
    ) {
        Text(text = "Register Screen", fontSize = 30.sp,color = MaterialTheme.colorScheme.onBackground)

        OutlinedTextFieldGenerator(username, {username = it}, "username", false, KeyboardType.Text, leadingIcon = Icons.Default.Person)
        OutlinedTextFieldGenerator(email, {email = it}, "email", false, KeyboardType.Email, leadingIcon = Icons.Default.Email)
        OutlinedTextFieldGenerator(password, {password = it}, "password", true, KeyboardType.Password, leadingIcon = Icons.Default.Lock)
        OutlinedTextFieldGenerator(confirmPassword, {confirmPassword = it}, "confirm password", true, KeyboardType.Password)

        PrimaryButtonGenerator(text = "Register", onClick = {
            val uName = username.trim()
            val mail = email.trim()

            // 1. Validácia vstupov
            if (uName.isBlank() || mail.isBlank() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(context, "Please fill in all fields.", Toast.LENGTH_SHORT).show()
                return@PrimaryButtonGenerator // UKONČÍ onClick
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
                Toast.makeText(context, "Please enter a valid email address.", Toast.LENGTH_SHORT).show()
                return@PrimaryButtonGenerator // UKONČÍ onClick
            }

            if (password != confirmPassword) {
                Toast.makeText(context, "Passwords do not match.", Toast.LENGTH_SHORT).show()
                return@PrimaryButtonGenerator // UKONČÍ onClick
            }

            // 2. Ak je všetko v poriadku, zavoláme funkciu z ViewModelu
            viewModel.registerUser(name = uName, pass = password, email = mail)
        })
    }
}


@Preview(showSystemUi = true, showBackground = true)
@Composable
fun RegisterScreenPreview() {
    BakalarkaTheme(darkTheme = true, dynamicColor = false) {
        RegisterScreen(navController = rememberNavController())
    }
}
