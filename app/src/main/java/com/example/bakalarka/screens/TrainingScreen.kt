package com.example.bakalarka.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bakalarka.other_classes.*
import com.example.bakalarka.supabase.CurrentUserHolder
import com.example.bakalarka.ui.theme.BakalarkaTheme
import kotlinx.coroutines.launch

@Composable
fun TrainingScreen(viewModel: AppViewModel = viewModel()) {

    var search by remember { mutableStateOf("") }

    val listTrainings by viewModel.trainings
    val listExercises by viewModel.exercises

    val userId = CurrentUserHolder.currentUser?.id
    var workoutId by remember { mutableStateOf<Int?>(null) }

    var searchbarColumn by remember { mutableStateOf(true) }
    var resultsColumn by remember { mutableStateOf(false) }
    var exercisesColumn by remember { mutableStateOf(false) }

    var currentExerciseIndex by remember { mutableStateOf(0) }

    val setDataSet = remember { mutableStateListOf<SetData>() }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    /* ================= SEARCH ================= */

    if (searchbarColumn) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            TextGenerator(
                "Search your training by name",
                MaterialTheme.colorScheme.onBackground,
                "subtitle"
            )

            OutlinedTextFieldGenerator(
                search,
                { search = it },
                "Search",
                false,
                KeyboardType.Text,
                Icons.Default.Search
            )

            PrimaryButtonGenerator(
                text = "Search",
                onClick = {
                    if (userId != null) {
                        viewModel.loadTrainingsByName(search, userId)
                        searchbarColumn = false
                        resultsColumn = true
                    } else {
                        Toast.makeText(
                            context,
                            "User not logged in",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            )
        }
    }

    /* ================= TRAINING LIST ================= */

    else if (resultsColumn) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            TextGenerator(
                "Pick training",
                MaterialTheme.colorScheme.onBackground,
                "subtitle"
            )

            listTrainings.forEach { training ->
                PrimaryButtonGenerator(
                    text = training.name,
                    onClick = {
                        if (userId != null) {
                            scope.launch {

                                val newWorkoutId =
                                    viewModel.addWorkout(userId, training.id)

                                workoutId = newWorkoutId

                                if (newWorkoutId != null) {
                                    viewModel.loadExercisesByTrainingId(training.id)

                                    currentExerciseIndex = 0
                                    resultsColumn = false
                                    exercisesColumn = true


                            } else {
                                    Toast.makeText(
                                        context,
                                        "Failed to start workout",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        } else {
                            Toast.makeText(
                                context,
                                "User not logged in",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                )
            }

            PrimaryButtonGenerator(
                text = "Back",
                modifier = Modifier.padding(top = 10.dp),
                onClick = {
                    resultsColumn = false
                    searchbarColumn = true
                }
            )
        }
    }

    /* ================= EXERCISES ================= */

    else if (exercisesColumn) {

        val exercise =
            if (currentExerciseIndex in listExercises.indices)
                listExercises[currentExerciseIndex]
            else
                null

        LaunchedEffect(currentExerciseIndex, listExercises) {
            setDataSet.clear()
            if (exercise != null) {
                repeat(exercise.sets_count) {
                    setDataSet.add(SetData())
                }
            }
        }


        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (exercise == null) {
                TextGenerator(
                    "Loading exercise...",
                    MaterialTheme.colorScheme.onBackground,
                    "small"
                )
                return@Column
            }

            TextGenerator(
                "Exercise ${currentExerciseIndex + 1}: ${exercise.name}",
                MaterialTheme.colorScheme.onBackground,
                "subtitle"
            )

            setDataSet.forEachIndexed { index, set ->

                TextGenerator(
                    "Set ${index + 1}",
                    MaterialTheme.colorScheme.onBackground,
                    "small"
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    OutlinedTextFieldGenerator(
                        set.reps,
                        {
                            setDataSet[index] =
                                set.copy(reps = it)
                        },
                        "Reps",
                        false,
                        KeyboardType.Number,
                        modifier = Modifier.weight(1f)
                    )

                    OutlinedTextFieldGenerator(
                        set.weight,
                        {
                            setDataSet[index] =
                                set.copy(weight = it)
                        },
                        "Weight",
                        false,
                        KeyboardType.Number,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            PrimaryButtonGenerator(
                text = "Next",
                onClick = {
                    scope.launch {

                        if (setDataSet.any { it.reps.isBlank() || it.weight.isBlank() }) {
                            Toast.makeText(
                                context,
                                "Please fill all reps and weight fields",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@launch
                        }

                        if (userId != null && workoutId != null) {

                            viewModel.addExerciseToWorkout(
                                userId,
                                workoutId!!,
                                exercise.id,
                                setDataSet.toList(),
                                exercise.sets_count
                            )

                            Toast.makeText(
                                context,
                                "Exercise '${exercise.name}' saved",
                                Toast.LENGTH_SHORT
                            ).show()

                            if (currentExerciseIndex < listExercises.size - 1) {
                                currentExerciseIndex++
                            } else {
                                Toast.makeText(
                                    context,
                                    "Workout finished!",
                                    Toast.LENGTH_LONG
                                ).show()

                                exercisesColumn = false
                                searchbarColumn = true
                            }
                        }
                    }

                }
            )

            PrimaryButtonGenerator(
                text = "Back",
                onClick = {
                    exercisesColumn = false
                    resultsColumn = true
                }
            )
        }
    }
}

/* ================= DATA ================= */

data class SetData(
    val reps: String = "",
    val weight: String = ""
)

