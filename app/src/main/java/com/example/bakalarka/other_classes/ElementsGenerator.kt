package com.example.bakalarka.other_classes

import android.R
import android.util.DisplayMetrics
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bakalarka.other_classes.ElementSizeProvider


@Composable
fun PrimaryButtonGenerator(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(onClick = {onClick()},modifier = modifier,colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.tertiary), shape = RoundedCornerShape(35)){
        TextGenerator(text, MaterialTheme.colorScheme.onTertiary, "button",false)
    }
}

@Composable
fun SecondaryButtonGenerator(text: String,   onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(onClick = {onClick()},modifier = modifier,colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary), shape = RoundedCornerShape(35)){
        TextGenerator(text, MaterialTheme.colorScheme.onSecondary, "label",false)
    }
}

@Composable
fun TextGenerator(
    text: String,
    color: Color,
    textType: String,
    bold: Boolean = false,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        fontSize = ElementSizeProvider.getSize(textType),
        fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal,
        color = color,
        modifier = modifier
    )
}


@Composable
fun OutlinedTextFieldGenerator(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isPassword: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    leadingIcon: ImageVector? = null,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = leadingIcon?.let { icon ->
            { Icon(imageVector = icon, contentDescription = null) }
        },
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        shape = RoundedCornerShape(25),
        modifier = modifier,
        colors = TextFieldDefaults.colors(
            focusedTextColor = MaterialTheme.colorScheme.onBackground,
            unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
            focusedLabelColor = MaterialTheme.colorScheme.onBackground,
            unfocusedLabelColor = MaterialTheme.colorScheme.onBackground,
            focusedIndicatorColor = MaterialTheme.colorScheme.onBackground,
            unfocusedIndicatorColor = MaterialTheme.colorScheme.onBackground,
            focusedContainerColor = MaterialTheme.colorScheme.background,
            unfocusedContainerColor = MaterialTheme.colorScheme.background,
        )
    )
}



