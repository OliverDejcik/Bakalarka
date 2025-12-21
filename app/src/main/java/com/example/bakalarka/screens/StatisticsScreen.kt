package com.example.bakalarka.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.bakalarka.Bakalarka
import com.example.bakalarka.data.Exercise
import com.example.bakalarka.data.Training
import com.example.bakalarka.other_classes.AppViewModel
import com.example.bakalarka.other_classes.LineChartView
import com.example.bakalarka.other_classes.PrimaryButtonGenerator
import com.example.bakalarka.other_classes.SelectBoxMaterial
import com.example.bakalarka.supabase.CurrentUserHolder
import com.example.bakalarka.ui.theme.BakalarkaTheme

@Composable
fun StatisticsScreen(viewModel: AppViewModel = viewModel()) {

    var selectedWorkout by remember { mutableStateOf("Pick your workout") }
    var selectedExercise by remember { mutableStateOf("Pick your exercise") }

    var selectedExerciseId by remember { mutableStateOf(0) }


    val userId = CurrentUserHolder.currentUser?.id
    val listTrainings by viewModel.trainings
    val listExercises by viewModel.exercises
    val listWorkoutExercises by viewModel.workoutExercises
    val chartData by viewModel.chartData




    // --- OPRAVENÁ A DOPLNENÁ ČASŤ ---

    // LaunchedEffect sa spustí iba raz, keď sa Composable prvýkrát zobrazí (alebo keď sa zmení userId)
    LaunchedEffect(userId) {
        if (userId != null) {
            viewModel.loadTrainingsByUserId(userId)
        }
    }







    var StatsPicker by remember { mutableStateOf(true) }


    if(StatsPicker){
        Column(verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize()
        )
        {
            Text(text = "Statistics Screen", fontSize = 30.sp,color = MaterialTheme.colorScheme.onBackground)

            SelectBoxMaterial(
                options = listTrainings.map {it.name},
                selected = selectedWorkout,
                onSelected = { selectedWorkout = it
                    listTrainings.find { training -> training.name == selectedWorkout }?.let { training ->
                        viewModel.loadExercisesByTrainingId(training.id)
                    }
                }
            )
            if (selectedWorkout != "Pick your workout") {
                SelectBoxMaterial(
                    options = listExercises.map { it.name },
                    selected = selectedExercise,
                    onSelected = { selectedExercise = it
                        listExercises.find { exercise -> exercise.name == selectedExercise }
                            ?.let { exercise ->
                                selectedExerciseId = exercise.id
                            }
                    }
                )
            }
            if (selectedExercise != "Pick your exercise") {
                PrimaryButtonGenerator("Get statistics", onClick = { StatsPicker = false
                viewModel.loadWorkoutExercisesByExerciseId(selectedExerciseId)
                })
            }

        }
    }
    if(!StatsPicker) {
        Column(verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize()
        ) {
            LaunchedEffect(listWorkoutExercises) {
                viewModel.build30Day1RMChart(listWorkoutExercises)
            }









            LineChartView(
                chartData,
                "Bench Press Progress",
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

