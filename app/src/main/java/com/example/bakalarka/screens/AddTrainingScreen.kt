package com.example.bakalarka.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bakalarka.R
import com.example.bakalarka.animations.LoadingAnimation
import com.example.bakalarka.other_classes.AppViewModel
import com.example.bakalarka.other_classes.ElementSizeProvider
import com.example.bakalarka.other_classes.OutlinedTextFieldGenerator
import com.example.bakalarka.other_classes.PrimaryButtonGenerator
import com.example.bakalarka.other_classes.SecondaryButtonGenerator
import com.example.bakalarka.other_classes.TextGenerator
import com.example.bakalarka.supabase.CurrentUserHolder
import kotlinx.coroutines.launch

@SuppressLint("ViewModelConstructorInComposable")
@Composable
fun AddTrainingScreen(viewModel: AppViewModel = viewModel()) {

    // --- STATES ---
    var trainingName by remember { mutableStateOf("") }
    var trainingNumber by remember { mutableStateOf("") }

    var exercise by remember { mutableStateOf("") }
    var sets by remember { mutableStateOf("") }
    var currentExerciseIndex by remember { mutableStateOf(0) }

    var isFormVisible by remember { mutableStateOf(true) }
    var isExerciseFormVisible by remember { mutableStateOf(false) }

    var createdTrainingId by remember { mutableStateOf<Int?>(null) }
    var isLoading = viewModel.isLoading

    // --- CONTEXT ---
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val userId = CurrentUserHolder.currentUser?.id

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

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

        /* ===========================
           CREATE TRAINING FORM
        ============================ */
        if (isFormVisible) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = (15.dp * ElementSizeProvider.getScale())),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                TextGenerator(
                    stringResource(R.string.add_training_screen_title),
                    MaterialTheme.colorScheme.onBackground,
                    "title"
                )

                OutlinedTextFieldGenerator(
                    trainingName,
                    { trainingName = it },
                    stringResource(R.string.outlined_text_field_label_training_name)
                )

                OutlinedTextFieldGenerator(
                    trainingNumber,
                    { trainingNumber = it },
                    stringResource(R.string.outlined_text_field_label_number_of_exercises),
                    false,
                    KeyboardType.Number
                )

                PrimaryButtonGenerator(
                    stringResource(R.string.button_create_training),
                    onClick = {
                        val number = trainingNumber.toIntOrNull()

                        when {
                            trainingName.isBlank() -> {
                                Toast.makeText(
                                    context,
                                    R.string.form_error_training_name_blank,
                                    Toast.LENGTH_SHORT
                                ).show()
                                return@PrimaryButtonGenerator
                            }

                            number == null -> {
                                Toast.makeText(
                                    context,
                                    R.string.form_error_number_of_exercises_blank,
                                    Toast.LENGTH_SHORT
                                ).show()
                                return@PrimaryButtonGenerator
                            }

                            number <= 0 -> {
                                Toast.makeText(
                                    context,
                                    R.string.form_error_number_of_exercises_invalid,
                                    Toast.LENGTH_SHORT
                                ).show()
                                return@PrimaryButtonGenerator
                            }
                            number > 10 -> {
                                Toast.makeText(
                                    context,
                                    R.string.form_error_number_of_exercises_max,
                                    Toast.LENGTH_SHORT
                                ).show()
                                return@PrimaryButtonGenerator
                            }
                            userId == null -> {
                                Toast.makeText(
                                    context,
                                    R.string.error_user_not_logged_in,
                                    Toast.LENGTH_SHORT
                                ).show()
                                return@PrimaryButtonGenerator
                            }
                        }

                        scope.launch {
                            val newId = viewModel.addTraining(
                                trainingName,
                                number,
                                userId
                            )

                            if (newId != null) {
                                createdTrainingId = newId
                                currentExerciseIndex = 1
                                isFormVisible = false
                                isExerciseFormVisible = true

                                Toast.makeText(
                                    context,
                                    R.string.add_traning_screen_popup_training_completed,
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Toast.makeText(
                                    context,
                                    R.string.add_traning_screen_popup_training_failed,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                )
            }
        }

        /* ===========================
           ADD EXERCISES FORM
        ============================ */
        else if (isExerciseFormVisible) {

            val totalExercises = trainingNumber.toIntOrNull() ?: 0
            val buttonText =
                if (currentExerciseIndex == totalExercises)
                    stringResource(R.string.button_finish_training)
                else
                    stringResource(R.string.button_add_exercise)

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = (15.dp * ElementSizeProvider.getScale())),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                TextGenerator(
                    "Add Exercise $currentExerciseIndex / $totalExercises",
                    MaterialTheme.colorScheme.onBackground,
                    "title"
                )

                OutlinedTextFieldGenerator(
                    exercise,
                    { exercise = it },
                    "Name of $currentExerciseIndex. exercise"
                )

                OutlinedTextFieldGenerator(
                    sets,
                    { sets = it },
                    stringResource(R.string.outlined_text_field_label_number_of_exercises),
                    false,
                    KeyboardType.Number
                )

                PrimaryButtonGenerator(
                    buttonText,
                    onClick = {
                        when {
                            exercise.isBlank() -> {
                                Toast.makeText(
                                    context,
                                    R.string.form_error_exercise_name_blank,
                                    Toast.LENGTH_SHORT
                                ).show()
                                return@PrimaryButtonGenerator
                            }

                            sets.isBlank() -> {
                                Toast.makeText(
                                    context,
                                    R.string.form_error_number_of_sets_blank,
                                    Toast.LENGTH_SHORT
                                ).show()
                                return@PrimaryButtonGenerator
                            }
                        }

                        viewModel.addExercise(
                            createdTrainingId,
                            userId,
                            exercise,
                            sets,
                            order = currentExerciseIndex
                        )

                        if (currentExerciseIndex == totalExercises) {
                            Toast.makeText(
                                context,
                                R.string.add_traning_screen_popup_training_completed,
                                Toast.LENGTH_LONG
                            ).show()

                            trainingName = ""
                            trainingNumber = ""
                            exercise = ""
                            sets = ""
                            currentExerciseIndex = 0
                            createdTrainingId = null
                            isExerciseFormVisible = false
                            isFormVisible = true
                        } else {
                            Toast.makeText(
                                context,
                                "Exercise added: $exercise",
                                Toast.LENGTH_SHORT
                            ).show()

                            exercise = ""
                            sets = ""
                            currentExerciseIndex++
                        }
                    }
                )
            }
        }

        /* ===========================
           FOOTER
        ============================ */
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.weight(0.7f))
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {}
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {

                SecondaryButtonGenerator(
                    text = stringResource(R.string.button_back),
                    onClick = {
                        scope.launch {

                            if (createdTrainingId != null) {
                                viewModel.deleteTrainingById(createdTrainingId!!)
                                createdTrainingId = null
                            }

                            currentExerciseIndex = 0
                        }
                    }
                )


            }
            Box(modifier = Modifier.weight(0.7f))
        }
    }
}


