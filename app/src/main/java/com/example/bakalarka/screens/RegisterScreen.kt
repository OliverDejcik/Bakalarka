package com.example.bakalarka.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.bakalarka.R
import com.example.bakalarka.animations.LoadingAnimation
import com.example.bakalarka.other_classes.AppViewModel
import com.example.bakalarka.other_classes.OutlinedTextFieldGenerator
import com.example.bakalarka.other_classes.PrimaryButtonGenerator
import com.example.bakalarka.other_classes.Screens

@Composable
fun RegisterScreen(navController: NavHostController, viewModel: AppViewModel = viewModel()) {

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    val isLoading = viewModel.isLoading

    val context = LocalContext.current

    // Sledujeme stav registrácie z AppViewModelu
    val registrationState by viewModel.registrationSuccess

    // Tento blok sa spustí vždy, keď sa zmení hodnota registrationState
    LaunchedEffect(registrationState) {
        when (registrationState) {
            true -> { // Úspech
                Toast.makeText(context, R.string.register_screen_popup_success, Toast.LENGTH_SHORT).show()
                // Presmerujeme na Login a vymažeme back stack
                navController.navigate(Screens.Home.route) {
                    popUpTo(0)
                }
                viewModel.resetRegistrationState() // Resetujeme stav
            }
            false -> { // Neúspech
                Toast.makeText(context, R.string.register_screen_popup_failed, Toast.LENGTH_LONG).show()
                viewModel.resetRegistrationState() // Resetujeme stav
            }
            null -> {
                // Nerobíme nič, čakáme na akciu
            }
        }
    }

    Column(verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        if(isLoading){
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                LoadingAnimation()
            }
        }
        Text(text = stringResource(R.string.register_screen_title), fontSize = 30.sp, color = MaterialTheme.colorScheme.onBackground)

        OutlinedTextFieldGenerator(username, {username = it}, stringResource(R.string.outlined_text_field_label_username), false, KeyboardType.Text, leadingIcon = Icons.Default.Person)
        OutlinedTextFieldGenerator(email, {email = it}, stringResource(R.string.outlined_text_field_label_email), false, KeyboardType.Email, leadingIcon = Icons.Default.Email)
        OutlinedTextFieldGenerator(password, {password = it}, stringResource(R.string.outlined_text_field_label_password), true, KeyboardType.Password, leadingIcon = Icons.Default.Lock)
        OutlinedTextFieldGenerator(confirmPassword, {confirmPassword = it}, stringResource(R.string.outlined_text_field_label_confirm_password), true, KeyboardType.Password)

        PrimaryButtonGenerator(text = stringResource(R.string.button_register), onClick = {
            val uName = username.trim()
            val mail = email.trim()

            // 1. Validácia vstupov
            if (username.isBlank() || email.isBlank() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(context, R.string.form_error_blank, Toast.LENGTH_SHORT).show()
                return@PrimaryButtonGenerator
            }

            // 2. Kontrola medzier
            if (username.contains(" ")) {
                Toast.makeText(context, R.string.form_error_username_space, Toast.LENGTH_SHORT).show()
                return@PrimaryButtonGenerator
            }
            if (email.contains(" ")) {
                Toast.makeText(context, R.string.form_error_email_space, Toast.LENGTH_SHORT).show()
                return@PrimaryButtonGenerator
            }
            if (password.contains(" ")) {
                Toast.makeText(context, R.string.form_error_password_space, Toast.LENGTH_SHORT).show()
                return@PrimaryButtonGenerator
            }


            // 3. Validácia formátu emailu
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
                Toast.makeText(context, R.string.form_error_email_invalid, Toast.LENGTH_SHORT).show()
                return@PrimaryButtonGenerator
            }

            if (password.length < 8) {
                Toast.makeText(context, R.string.form_error_password_length, Toast.LENGTH_SHORT).show()
                return@PrimaryButtonGenerator
            }

            if (!password.any { it.isDigit() }) {
                Toast.makeText(context, R.string.form_error_password_digit, Toast.LENGTH_SHORT).show()
                return@PrimaryButtonGenerator
            }


            // 4. Zhoda hesiel
            if (password != confirmPassword) {
                Toast.makeText(context, R.string.form_error_password_match, Toast.LENGTH_SHORT).show()
                return@PrimaryButtonGenerator
            }

            // 5. Ak je všetko v poriadku, zavoláme funkciu z ViewModelu
            viewModel.registerUser(name = uName, pass = password, email = mail)
        })
    }
}
