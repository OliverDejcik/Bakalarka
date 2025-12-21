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

    var selectedWorkoutId by remember { mutableStateOf(0) }
    var selectedExerciseId by remember { mutableStateOf(0) }


    val userId = CurrentUserHolder.currentUser?.id
    val listTrainings by viewModel.trainings
    val listExercises by viewModel.exercises


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
                        selectedWorkoutId = training.id
                    }
                }
            )
            if (selectedWorkout != "Pick your workout") {
                SelectBoxMaterial(
                    options = listExercises.map { it.name },
                    selected = selectedExercise,
                    onSelected = { selectedExercise = it
                        listExercises.find { exercise -> exercise.name == selectedWorkout }
                            ?.let { exercise ->
                                selectedExerciseId = exercise.id
                            }
                    }
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
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize()
        ) {


            LineChartView(
                data = listOf(
                    1f to 80f,
                    2f to 82f,
                    3f to 85f,
                    4f to 88f,
                    5f to 90f,
                    6f to 92f,
                    7f to 95f,
                    8f to 98f,
                    9f to 100f
                ),
                descriptionText = "Bench Press Progress",
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

