package com.example.bakalarka.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.bakalarka.Bakalarka
import com.example.bakalarka.other_classes.AppViewModel
import com.example.bakalarka.other_classes.PrimaryButtonGenerator
import com.example.bakalarka.other_classes.TextGenerator
import com.example.bakalarka.ui.theme.BakalarkaTheme
import androidx.compose.ui.text.style.TextAlign.Companion.Center




@Composable
fun HomeScreen(viewModel: AppViewModel = viewModel()) {
    Column(verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.background(MaterialTheme.colorScheme.background).fillMaxSize().padding(horizontal = 16.dp)
    )
    {
        TextGenerator("Welcome to your workout tracking app!",MaterialTheme.colorScheme.onBackground,"subtitle",true, textAlign = Center, modifier = Modifier.padding(bottom = 16.dp))

        TextGenerator(text = "Start by creating your first workout using the plus (+) button at the bottom of the screen.\n" +
                "Once your workout is created, you can log your training sessions and record your performance by tapping the dumbbell icon and selecting your workout.\n" +
                "\n" +
                "Finally, keep track of your progress in the Statistics section, accessible via the chart icon at the bottom of the screen.",color = MaterialTheme.colorScheme.onBackground, "small", false, textAlign = Center)




    }
}



