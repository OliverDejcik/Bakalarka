package com.example.bakalarka.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.bakalarka.R
import com.example.bakalarka.animations.LoadingAnimation
import com.example.bakalarka.other_classes.AppViewModel
import com.example.bakalarka.other_classes.OutlinedTextFieldGenerator
import com.example.bakalarka.other_classes.PrimaryButtonGenerator
import com.example.bakalarka.other_classes.Screens
import com.example.bakalarka.other_classes.SecondaryButtonGenerator
import com.example.bakalarka.other_classes.TextGenerator
import com.example.bakalarka.supabase.verifyUser
import com.example.bakalarka.ui.theme.BakalarkaTheme
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(navController: NavHostController, viewModel: AppViewModel = viewModel()) {

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val composableScope = rememberCoroutineScope()
    val context = LocalContext.current

    val isLoading = viewModel.isLoading


    if(isLoading){
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
            LoadingAnimation()
        }

    }else{
        Column(verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.background(MaterialTheme.colorScheme.background).fillMaxSize()
        )
        {
            Column(verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.background(MaterialTheme.colorScheme.background).fillMaxWidth()
            ) {
                TextGenerator(stringResource(R.string.login_screen_title), MaterialTheme.colorScheme.onBackground, "title")

                OutlinedTextFieldGenerator(value = username,
                    onValueChange = {username = it},
                    label = stringResource(R.string.outlined_text_field_label_username),
                    isPassword = false,
                    keyboardType = KeyboardType.Text,
                    leadingIcon = Icons.Default.Person
                )


                OutlinedTextFieldGenerator(
                    password,
                    { password = it },
                    stringResource(R.string.outlined_text_field_label_password),
                    isPassword = true,
                    keyboardType = KeyboardType.Password,
                    leadingIcon = Icons.Default.Lock
                )


                PrimaryButtonGenerator(text = stringResource(R.string.button_login), onClick = {val email = username.trim()
                    val pass = password

                    if (email.isBlank() || pass.isEmpty()) {
                        Toast.makeText(context, R.string.form_error_blank, Toast.LENGTH_SHORT).show()
                        return@PrimaryButtonGenerator
                    }



                    // Voláme funkciu vo Viewmodeli
                    viewModel.login(email, pass) { loginSuccessful ->
                        if (loginSuccessful) {
                            Toast.makeText(context, R.string.login_screen_popup_success, Toast.LENGTH_SHORT).show()
                            navController.navigate(Screens.Home.route) {
                                popUpTo(Screens.Login.route) { inclusive = true }
                                launchSingleTop = true
                            }
                        } else {
                            Toast.makeText(context, R.string.login_screen_popup_failed, Toast.LENGTH_SHORT).show()
                        }
                    }
                })
            }

            Row(horizontalArrangement = Arrangement.Center,verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()){
                Box(modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    TextGenerator(stringResource(R.string.login_screen_no_account), MaterialTheme.colorScheme.onBackground, "label")
                }

                Box(modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    SecondaryButtonGenerator(text = stringResource(R.string.button_register), onClick = {navController.navigate(Screens.Register.route) { launchSingleTop = true }})
                }
            }

        }
    }

}


