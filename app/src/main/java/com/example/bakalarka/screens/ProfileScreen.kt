package com.example.bakalarka.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.bakalarka.Bakalarka
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

@Composable
fun ProfileScreen(viewModel: AppViewModel = viewModel()) {

    var isPasswordChangeActive by remember { mutableStateOf(false) }

    var oldPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var newPasswordConfirm by remember { mutableStateOf("") }

    val composableScope = rememberCoroutineScope()

    var userId by remember { mutableStateOf(0) }
    userId = CurrentUserHolder.currentUser?.id ?: 0


    val context = LocalContext.current


    Column(verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.background(MaterialTheme.colorScheme.background).fillMaxSize()
    )
    {
        TextGenerator("Profile screen", MaterialTheme.colorScheme.onBackground, "title")
        PrimaryButtonGenerator("Change password", onClick = {isPasswordChangeActive = true })
        if (isPasswordChangeActive) {
            OutlinedTextFieldGenerator(oldPassword, { oldPassword = it }, "Your current password", isPassword = true, leadingIcon = Icons.Default.Lock)
            OutlinedTextFieldGenerator(newPassword, { newPassword = it }, "New password", isPassword = true, leadingIcon = Icons.Default.Lock)
            OutlinedTextFieldGenerator(newPasswordConfirm, { newPasswordConfirm = it }, "Confirm new password", isPassword = true, leadingIcon = Icons.Default.Lock)

            SecondaryButtonGenerator("Change password", onClick = {
                if (newPassword != newPasswordConfirm) {
                    Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                }else if (oldPassword == newPassword) {
                    Toast.makeText(context, "New password cannot be the same as the old one", Toast.LENGTH_SHORT).show()
                }else{
                    composableScope.launch {

                        val userVerifySuccessful = verifyUserByIdAndPass(userId = userId, password = oldPassword)

                        if (userVerifySuccessful) {
                            changeUserPassword(userId, newPassword)
                            Toast.makeText(context, "Password changed successfully", Toast.LENGTH_SHORT).show()
                            isPasswordChangeActive = false
                            oldPassword = ""
                            newPassword = ""
                            newPasswordConfirm = ""
                        } else {
                            Toast.makeText(context, "Invalid password", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            })
        }

    }
}

