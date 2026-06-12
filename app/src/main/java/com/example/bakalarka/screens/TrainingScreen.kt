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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bakalarka.animations.LoadingAnimation
import com.example.bakalarka.data.Training
import com.example.bakalarka.other_classes.*
import com.example.bakalarka.supabase.CurrentUserHolder
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.ceil
import com.example.bakalarka.R

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
    var isLoading = viewModel.isLoading

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        /* ================= SEARCH ================= */
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f)), // Jemné stmavenie pozadia
                contentAlignment = Alignment.Center
            ) {
                LoadingAnimation()
            }
        }

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
                        Toast.makeText(context, R.string.error_user_not_logged_in, Toast.LENGTH_SHORT).show()
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
                        Toast.makeText(context, R.string.error_user_not_logged_in, Toast.LENGTH_SHORT).show()
                        return@SearchResults
                    }

                    scope.launch {
                        val newWorkoutId = viewModel.addWorkout(userId, training.id)

                        if (newWorkoutId == null) {
                            Toast.makeText(context, R.string.error_network, Toast.LENGTH_SHORT).show()
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
                        TextGenerator(stringResource(R.string.error_no_data), MaterialTheme.colorScheme.onBackground, "small")
                        return@Column
                    }

                    val match = listPreviousWorkout.find {
                        it.exercise_id == exercise.id
                    }

                    if (match != null) {

                        TextGenerator(
                            stringResource(R.string.button_previous_workout),
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
                            stringResource(R.string.error_no_data),
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

                    PrimaryButtonGenerator(stringResource(R.string.button_next_exercise),
                        onClick = {
                        scope.launch {

                            if (setDataSet.any { it.reps.isBlank() || it.weight.isBlank() }) {
                                Toast.makeText(context, R.string.form_error_blank, Toast.LENGTH_SHORT)
                                    .show()
                                return@launch
                            }

                            // Clean inputs (commas to dots) and parse safely
                            val validatedData = setDataSet.map {
                                val cleanReps = it.reps.replace(',', '.')
                                val cleanWeight = it.weight.replace(',', '.')
                                it.copy(reps = cleanReps, weight = cleanWeight)
                            }

                            when {
                                validatedData.any { it.reps.toFloatOrNull() == null } -> {
                                    Toast.makeText(context, R.string.form_error_reps_invalid, Toast.LENGTH_SHORT).show()
                                    return@launch
                                }

                                validatedData.any {
                                    val reps = it.reps.toFloatOrNull()
                                    reps != null && reps > 40f
                                } -> {
                                    Toast.makeText(context, R.string.form_error_reps_max, Toast.LENGTH_SHORT).show()
                                    return@launch
                                }

                                validatedData.any { it.weight.toFloatOrNull() == null } -> {
                                    Toast.makeText(context, R.string.form_error_weight_invalid , Toast.LENGTH_SHORT).show()
                                    return@launch
                                }

                                validatedData.any {
                                    val weight = it.weight.toFloatOrNull()
                                    weight != null && weight > 1100f
                                } -> {
                                    Toast.makeText(context, R.string.form_error_weight_max, Toast.LENGTH_SHORT).show()
                                    return@launch
                                }
                            }


                            if (userId != null && workoutId != null) {

                                viewModel.addExerciseToWorkout(
                                    userId,
                                    workoutId!!,
                                    exercise!!.id,
                                    validatedData,
                                    exercise.sets_count
                                )

                                if (currentExerciseIndex < listExercises.size - 1) {
                                    currentExerciseIndex++
                                } else {
                                    Toast.makeText(context, R.string.training_screen_popup_finished, Toast.LENGTH_LONG).show()
                                    exercisesColumn = false
                                    searchbarColumn = true
                                }
                            }
                        }
                    })

                    PrimaryButtonGenerator(stringResource(R.string.button_back),
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

        TextGenerator(stringResource(R.string.training_screen_search), MaterialTheme.colorScheme.onBackground, "subtitle")

        OutlinedTextFieldGenerator(
            searchInput,
            onSearchInputChanged,
            stringResource(R.string.outlined_text_field_label_search),
            false,
            KeyboardType.Text,
            Icons.Default.Search
        )

        PrimaryButtonGenerator(stringResource(R.string.button_search), onClick = onSearch)
    }
}

@Composable
fun SearchResults(
    listTrainings: List<Training>,
    onClick: (Training) -> Unit,
    onBack: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        TextGenerator(stringResource(R.string.training_screen_pick_training), MaterialTheme.colorScheme.onBackground, "subtitle")

        listTrainings.forEach {
            PrimaryButtonGenerator(
                text = it.name,
                onClick = { onClick(it) }
            )
        }

        PrimaryButtonGenerator(stringResource(R.string.button_back), onClick = onBack)
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