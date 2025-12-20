package com.example.bakalarka.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.bakalarka.Bakalarka
import com.example.bakalarka.other_classes.AppViewModel
import com.example.bakalarka.other_classes.LineChartView
import com.example.bakalarka.other_classes.PrimaryButtonGenerator
import com.example.bakalarka.other_classes.SelectBox
import com.example.bakalarka.other_classes.SelectBoxMaterial
import com.example.bakalarka.ui.theme.BakalarkaTheme

@Composable
fun StatisticsScreen(viewModel: AppViewModel = viewModel()) {

    var selectedWorkout by remember { mutableStateOf("Pick your workout") }
    var selectedExercise by remember { mutableStateOf("Pick your exercise") }

    var StatsPicker by remember { mutableStateOf(true) }


    if(StatsPicker){
        Column(verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.background(MaterialTheme.colorScheme.background).fillMaxSize()
        )
        {
            Text(text = "Statistics Screen", fontSize = 30.sp,color = MaterialTheme.colorScheme.onBackground)

            SelectBoxMaterial(
                options = listOf("Beginner", "Intermediate", "Advanced"),
                selected = selectedWorkout,
                onSelected = { selectedWorkout = it }
            )
            if (selectedWorkout != "Pick your workout") {
                SelectBoxMaterial(
                    options = listOf("Squat", "Bench press", "Deadlift"),
                    selected = selectedExercise,
                    onSelected = { selectedExercise = it }
                )
            }
            if (selectedExercise != "Pick your exercise") {
                PrimaryButtonGenerator("Get statistics", onClick = { StatsPicker = false })
            }

        }
    }
    if(!StatsPicker) {
        Column(verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.background(MaterialTheme.colorScheme.background).fillMaxSize()
        ) {
            LineChartView(
                data = listOf(
                    1f to 80f,
                    2f to 82f,
                    3f to 85f
                ),
                descriptionText = "Bench Press Progress",
                yAxisSuffix = "kg"
            )

        }

    }

}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun StatisticsScreenPreview() {
    BakalarkaTheme(darkTheme = true, dynamicColor = false) {
        Bakalarka(navController = rememberNavController(),viewModel())
        StatisticsScreen()
    }
}

