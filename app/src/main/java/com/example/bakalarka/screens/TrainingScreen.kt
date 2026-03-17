package com.example.bakalarka.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bakalarka.data.Training
import com.example.bakalarka.other_classes.*
import com.example.bakalarka.supabase.CurrentUserHolder
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.ceil

/* ================= MAIN SCREEN ================= */

@Composable
fun TrainingScreen(viewModel: AppViewModel = viewModel()) {

    var search by remember { mutableStateOf("") }

    val listTrainings by viewModel.trainings
    val listExercises by viewModel.exercises
    val listPreviousWorkout by viewModel.previousWorkout

    val userId = CurrentUserHolder.currentUser?.id
    var workoutId by remember { mutableStateOf<Int?>(null) }

    var searchbarColumn by remember { mutableStateOf(true) }
    var resultsColumn by remember { mutableStateOf(false) }
    var exercisesColumn by remember { mutableStateOf(false) }

    var currentExerciseIndex by remember { mutableStateOf(0) }

    val setDataSet = remember { mutableStateListOf<SetData>() }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        /* ================= SEARCH ================= */

        if (searchbarColumn) {

            SearchCulumn(
                searchInput = search,
                onSearchInputChanged = { search = it },
                onSearch = {
                    if (userId != null) {
                        viewModel.loadTrainingsByName(search, userId)
                        searchbarColumn = false
                        resultsColumn = true
                    } else {
                        Toast.makeText(context, "User not logged in", Toast.LENGTH_SHORT).show()
                    }
                }
            )
        }

        /* ================= RESULTS ================= */

        else if (resultsColumn) {

            SearchResults(
                listTrainings = listTrainings,
                onClick = { training ->

                    if (userId == null) {
                        Toast.makeText(context, "User not logged in", Toast.LENGTH_SHORT).show()
                        return@SearchResults
                    }

                    scope.launch {
                        val newWorkoutId = viewModel.addWorkout(userId, training.id)

                        if (newWorkoutId == null) {
                            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                            return@launch
                        }

                        workoutId = newWorkoutId

                        viewModel.loadpreviousWorkout(training.id)
                        viewModel.loadExercisesByTrainingId(training.id)

                        snapshotFlow { listExercises }
                            .first { it.isNotEmpty() }

                        currentExerciseIndex = 0
                        resultsColumn = false
                        exercisesColumn = true
                    }
                },
                onBack = {
                    resultsColumn = false
                    searchbarColumn = true
                }
            )
        }

        /* ================= EXERCISES ================= */

        else if (exercisesColumn) {

            val exercise =
                if (currentExerciseIndex in listExercises.indices)
                    listExercises[currentExerciseIndex]
                else null

            LaunchedEffect(currentExerciseIndex, listExercises) {
                setDataSet.clear()
                if (exercise != null) {
                    repeat(exercise.sets_count) {
                        setDataSet.add(SetData())
                    }
                }
            }

            Column(modifier = Modifier.fillMaxSize()) {

                /* ===== PREVIOUS WORKOUT ===== */

                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    if (exercise == null) {
                        TextGenerator("Loading...", MaterialTheme.colorScheme.onBackground, "small")
                        return@Column
                    }

                    val match = listPreviousWorkout.find {
                        it.exercise_id == exercise.id
                    }

                    if (match != null) {

                        TextGenerator(
                            "Previous workout",
                            MaterialTheme.colorScheme.onBackground,
                            "body"
                        )

                        val date = runCatching {
                            OffsetDateTime.parse(match.created_at)
                                .format(DateTimeFormatter.ofPattern("dd.MM"))
                        }.getOrNull() ?: "-"

                        TextGenerator(date, MaterialTheme.colorScheme.onBackground, "small")

                        val columns = when {
                            exercise.sets_count > 11 -> 3
                            exercise.sets_count > 5 -> 2
                            else -> 1
                        }

                        val rows = ceil(exercise.sets_count.toDouble() / columns).toInt()

                        var i = 0
                        Row {
                            repeat(columns) {
                                Column(Modifier.weight(1f)) {
                                    repeat(rows) {
                                        val data = listPreviousWorkout.find {
                                            it.exercise_id == exercise.id && it.set_number == i + 1
                                        }
                                        i++
                                        if (data != null) {
                                            TextGenerator(
                                                "Set $i: ${data.reps} x ${data.weight}",
                                                MaterialTheme.colorScheme.onBackground,
                                                "ultrasmall"
                                            )
                                        }
                                    }
                                }
                            }
                        }

                    } else {
                        TextGenerator(
                            "No previous workouts",
                            MaterialTheme.colorScheme.onBackground,
                            "ultrasmall"
                        )
                    }

                }
                Divider(
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                    thickness = 1.dp
                )

                /* ===== CURRENT ===== */

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    TextGenerator(
                        "Exercise ${currentExerciseIndex + 1}: ${exercise?.name ?: ""}",
                        MaterialTheme.colorScheme.onBackground,
                        "subtitle"
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    setDataSet.forEachIndexed { index, set ->

                        WorkoutInputColumn(
                            setNumber = index + 1,
                            reps = set.reps,
                            weight = set.weight,
                            onRepsChange = {
                                setDataSet[index] = set.copy(reps = it)
                            },
                            onWeightChange = {
                                setDataSet[index] = set.copy(weight = it)
                            }
                        )
                    }

                    PrimaryButtonGenerator("Next Exercise",
                        onClick = {
                        scope.launch {

                            if (setDataSet.any { it.reps.isBlank() || it.weight.isBlank() }) {
                                Toast.makeText(context, "Fill all fields", Toast.LENGTH_SHORT)
                                    .show()
                                return@launch
                            }

                            if (userId != null && workoutId != null) {

                                viewModel.addExerciseToWorkout(
                                    userId,
                                    workoutId!!,
                                    exercise!!.id,
                                    setDataSet.toList(),
                                    exercise.sets_count
                                )

                                if (currentExerciseIndex < listExercises.size - 1) {
                                    currentExerciseIndex++
                                } else {
                                    Toast.makeText(context, "Finished!", Toast.LENGTH_LONG).show()
                                    exercisesColumn = false
                                    searchbarColumn = true
                                }
                            }
                        }
                    })

                    PrimaryButtonGenerator("Back",
                        onClick = {
                        scope.launch {
                            workoutId?.let { viewModel.deleteWorkout(it) }
                            workoutId = null
                            currentExerciseIndex = 0
                            exercisesColumn = false
                            resultsColumn = true
                        }
                    }
                    )
                }

            }
        }
    }
}

/* ================= UI COMPONENTS ================= */

@Composable
fun SearchCulumn(
    searchInput: String,
    onSearchInputChanged: (String) -> Unit,
    onSearch: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        TextGenerator("Search training", MaterialTheme.colorScheme.onBackground, "subtitle")

        OutlinedTextFieldGenerator(
            searchInput,
            onSearchInputChanged,
            "Search",
            false,
            KeyboardType.Text,
            Icons.Default.Search
        )

        PrimaryButtonGenerator("Search", onClick = onSearch)
    }
}

@Composable
fun SearchResults(
    listTrainings: List<Training>,
    onClick: (Training) -> Unit,
    onBack: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        TextGenerator("Pick training", MaterialTheme.colorScheme.onBackground, "subtitle")

        listTrainings.forEach {
            PrimaryButtonGenerator(
                text = it.name,
                onClick = { onClick(it) }
            )
        }

        PrimaryButtonGenerator("Back", onClick = onBack)
    }
}

@Composable
fun WorkoutInputColumn(
    setNumber: Int,
    reps: String,
    weight: String,
    onRepsChange: (String) -> Unit,
    onWeightChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.onBackground, RoundedCornerShape(10.dp))
            .padding(10.dp)
    ) {

        TextGenerator("Set $setNumber", MaterialTheme.colorScheme.onBackground, "small")

        Spacer(modifier = Modifier.height(8.dp))

        Row {
            InputBox(reps, onRepsChange, 1f, "reps")
            Spacer(modifier = Modifier.width(8.dp))
            InputBox(weight, onWeightChange, 1f, "weight")
        }
    }

    Spacer(modifier = Modifier.height(10.dp))
}

@Composable
fun RowScope.InputBox(
    value: String,
    onValueChange: (String) -> Unit,
    weight: Float,
    placeholder: String
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        textStyle = TextStyle(textAlign = TextAlign.Center),
        modifier = Modifier
            .weight(weight)
            .border(1.dp, MaterialTheme.colorScheme.onBackground, RoundedCornerShape(20.dp))
            .padding(8.dp),
        decorationBox = { inner ->
            Box(contentAlignment = Alignment.Center) {
                if (value.isEmpty()) {
                    TextGenerator(placeholder, MaterialTheme.colorScheme.onBackground, "small")
                }
                inner()
            }
        }
    )
}

/* ================= DATA ================= */

data class SetData(
    val reps: String = "",
    val weight: String = ""
)