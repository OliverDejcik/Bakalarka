package com.example.bakalarka.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bakalarka.other_classes.AppViewModel
import com.example.bakalarka.other_classes.TextGenerator
import androidx.compose.ui.text.style.TextAlign.Companion.Center
import com.example.bakalarka.R


@Composable
fun HomeScreen(viewModel: AppViewModel = viewModel()) {
    Column(verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.background(MaterialTheme.colorScheme.background).fillMaxSize().padding(horizontal = 16.dp)
    ) {
        TextGenerator(stringResource(R.string.home_welcome_title),MaterialTheme.colorScheme.onBackground,"subtitle",true, textAlign = Center, modifier = Modifier.padding(bottom = 16.dp))

        TextGenerator(text = stringResource(R.string.home_instructions_body),color = MaterialTheme.colorScheme.onBackground, "small", false, textAlign = Center)
    }

}



