package com.example.bakalarka.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.bakalarka.Bakalarka
import com.example.bakalarka.other_classes.AppViewModel
import com.example.bakalarka.ui.theme.BakalarkaTheme

@Composable
fun StatisticsScreen(viewModel: AppViewModel = viewModel()) {
    Column(verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.background(MaterialTheme.colorScheme.background).fillMaxSize()
    )
    {
        Text(text = "Statistics Screen", fontSize = 30.sp,color = MaterialTheme.colorScheme.onBackground)
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun StatisticsScreenPreview() {
    BakalarkaTheme(darkTheme = true, dynamicColor = false) {
        Bakalarka(navController = rememberNavController(),viewModel())
    }
}

