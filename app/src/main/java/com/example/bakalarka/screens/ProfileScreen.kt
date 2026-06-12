package com.example.bakalarka.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.bakalarka.Bakalarka
import com.example.bakalarka.animations.LoadingAnimation
import com.example.bakalarka.other_classes.AppViewModel
import com.example.bakalarka.other_classes.OutlinedTextFieldGenerator
import com.example.bakalarka.other_classes.PrimaryButtonGenerator
import com.example.bakalarka.other_classes.Screens
import com.example.bakalarka.other_classes.SecondaryButtonGenerator
import com.example.bakalarka.other_classes.TextGenerator
import com.example.bakalarka.supabase.CurrentUserHolder
import com.example.bakalarka.supabase.changeUserPassword
import com.example.bakalarka.supabase.verifyUser
import com.example.bakalarka.supabase.verifyUserByIdAndPass
import com.example.bakalarka.ui.theme.BakalarkaTheme
import kotlinx.coroutines.launch
import com.example.bakalarka.R

@Composable
fun ProfileScreen(navController: NavHostController, viewModel: AppViewModel = viewModel()) {

    var isPasswordChangeActive by remember { mutableStateOf(false) }
    var isEmailChangeActive by remember { mutableStateOf(false) }
    var isUsernameChangeActive by remember { mutableStateOf(false) }

    var oldPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var newPasswordConfirm by remember { mutableStateOf("") }

    var newEmail by remember { mutableStateOf("") }

    var newUsername by remember { mutableStateOf("") }

    val composableScope = rememberCoroutineScope()

    var userId by remember { mutableStateOf(0) }
    userId = CurrentUserHolder.currentUser?.id ?: 0

    val isLoading = viewModel.isLoading


    val context = LocalContext.current


    Column(verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.background(MaterialTheme.colorScheme.background).fillMaxSize()
    )
    {
        TextGenerator(stringResource(R.string.profile_screen_title), MaterialTheme.colorScheme.onBackground, "title")

        if (!isUsernameChangeActive && !isEmailChangeActive && !isPasswordChangeActive){
            PrimaryButtonGenerator(stringResource(R.string.button_change_username), onClick = {
                isUsernameChangeActive = true
                isPasswordChangeActive = false
                isEmailChangeActive = false
            })
            PrimaryButtonGenerator(stringResource(R.string.button_change_email), onClick = {
                isEmailChangeActive = true
                isPasswordChangeActive = false
                isUsernameChangeActive = false
            })
            PrimaryButtonGenerator(stringResource(R.string.button_change_password), onClick = {
                isPasswordChangeActive = true
                isUsernameChangeActive = false
                isEmailChangeActive = false
            })

            SecondaryButtonGenerator(stringResource(R.string.button_logout), onClick = {
                CurrentUserHolder.logout()
                navController.navigate(Screens.Login.route) {
                    popUpTo(0)
                }
            })
        }


        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                LoadingAnimation()
            }
        }

        if(isUsernameChangeActive){
            TextGenerator(stringResource(R.string.profile_screen_change_username), MaterialTheme.colorScheme.onBackground, "subtitle")

            OutlinedTextFieldGenerator(newUsername, { newUsername = it }, stringResource(R.string.outlined_text_field_label_username), isPassword = false, leadingIcon = Icons.Default.Person)

            SecondaryButtonGenerator(stringResource(R.string.button_change_username), onClick = {

                if (newUsername.isBlank()) {
                    Toast.makeText(context, R.string.form_error_blank, Toast.LENGTH_SHORT).show()
                    return@SecondaryButtonGenerator
                }
                if (newUsername.contains(" ")) {
                    Toast.makeText(context, R.string.form_error_blank, Toast.LENGTH_SHORT)
                        .show()
                    return@SecondaryButtonGenerator
                }
                composableScope.launch {
                    viewModel.changeUsername(userId, newUsername)
                    isUsernameChangeActive = false
                    newUsername = ""
                }


            }
            )
            SecondaryButtonGenerator(stringResource(R.string.button_back), {
                isUsernameChangeActive = false
                newUsername = ""
            })


        }
        if(isEmailChangeActive){
            TextGenerator(stringResource(R.string.profile_screen_change_email), MaterialTheme.colorScheme.onBackground, "subtitle")

            OutlinedTextFieldGenerator(newEmail, { newEmail = it }, stringResource(R.string.outlined_text_field_label_email), isPassword = false, leadingIcon = Icons.Default.Mail)
            SecondaryButtonGenerator(stringResource(R.string.button_change_email), onClick = {

                if (newEmail.isBlank()) {
                    Toast.makeText(context,R.string.form_error_blank, Toast.LENGTH_SHORT).show()
                    return@SecondaryButtonGenerator
                }
                if (newEmail.contains(" ")) {
                    Toast.makeText(context, R.string.form_error_blank, Toast.LENGTH_SHORT).show()
                    return@SecondaryButtonGenerator
                }
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(newEmail).matches()) {
                    Toast.makeText(context, R.string.form_error_email_invalid, Toast.LENGTH_SHORT).show()
                    return@SecondaryButtonGenerator
                }
                composableScope.launch {
                    viewModel.changeEmail(userId, newEmail)
                    isEmailChangeActive = false
                    newEmail = ""
                }


            }
            )

            SecondaryButtonGenerator("Back", {
                isEmailChangeActive = false
                newEmail = ""
            })
        }

        if (isPasswordChangeActive) {
            TextGenerator(stringResource(R.string.profile_screen_change_password), MaterialTheme.colorScheme.onBackground, "subtitle")

            OutlinedTextFieldGenerator(oldPassword, { oldPassword = it }, stringResource(R.string.outlined_text_field_label_current_password), isPassword = true, leadingIcon = Icons.Default.Lock)
            OutlinedTextFieldGenerator(newPassword, { newPassword = it }, stringResource(R.string.outlined_text_field_label_enter_new_password), isPassword = true, leadingIcon = Icons.Default.Lock)
            OutlinedTextFieldGenerator(newPasswordConfirm, { newPasswordConfirm = it }, stringResource(R.string.outlined_text_field_label_enter_new_password_confirm), isPassword = true, leadingIcon = Icons.Default.Lock)

            SecondaryButtonGenerator(stringResource(R.string.button_change_password), onClick = {
                if (newPassword != newPasswordConfirm) {
                    Toast.makeText(context, R.string.form_error_password_match, Toast.LENGTH_SHORT).show()
                }else if (oldPassword == newPassword) {
                    Toast.makeText(context, R.string.form_error_old_new_password_error, Toast.LENGTH_SHORT).show()
                }else{
                    composableScope.launch {

                        val userVerifySuccessful = verifyUserByIdAndPass(userId = userId, password = oldPassword)

                        if (userVerifySuccessful) {
                            changeUserPassword(userId, newPassword)
                            Toast.makeText(context, R.string.profile_screen_popup_change_password_successful, Toast.LENGTH_SHORT).show()
                            isPasswordChangeActive = false
                            oldPassword = ""
                            newPassword = ""
                            newPasswordConfirm = ""
                        } else {
                            Toast.makeText(context, R.string.profile_screen_popup_change_password_failed, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            })

            SecondaryButtonGenerator(stringResource(R.string.button_back), {
                isPasswordChangeActive = false
                oldPassword = ""
                newPassword = ""
                newPasswordConfirm = ""
            })


        }
    }
}

