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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bakalarka.other_classes.AppViewModel
import com.example.bakalarka.other_classes.ElementSizeProvider
import com.example.bakalarka.other_classes.OutlinedTextFieldGenerator
import com.example.bakalarka.other_classes.PrimaryButtonGenerator
import com.example.bakalarka.other_classes.SecondaryButtonGenerator
import com.example.bakalarka.other_classes.TextGenerator
import com.example.bakalarka.supabase.CurrentUserHolder
import com.example.bakalarka.ui.theme.BakalarkaTheme
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
                    "Create new training",
                    MaterialTheme.colorScheme.onBackground,
                    "title"
                )

                OutlinedTextFieldGenerator(
                    trainingName,
                    { trainingName = it },
                    "Training Name"
                )

                OutlinedTextFieldGenerator(
                    trainingNumber,
                    { trainingNumber = it },
                    "Number of exercises",
                    false,
                    KeyboardType.Number
                )

                PrimaryButtonGenerator(
                    "Construct training",
                    onClick = {
                        val number = trainingNumber.toIntOrNull()

                        if (trainingName.isBlank() || number == null || number <= 0) {
                            Toast.makeText(
                                context,
                                "Name and a valid number of exercises are required",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@PrimaryButtonGenerator
                        }

                        if (userId == null) {
                            Toast.makeText(
                                context,
                                "Error: User not logged in.",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@PrimaryButtonGenerator
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
                                    "Training created. Now add exercises.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Toast.makeText(
                                    context,
                                    "Failed to create training.",
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
                    "Finish Training"
                else
                    "Add Exercise"

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
                    "Number of sets",
                    false,
                    KeyboardType.Number
                )

                PrimaryButtonGenerator(
                    buttonText,
                    onClick = {
                        if (exercise.isBlank() || sets.isBlank()) {
                            Toast.makeText(
                                context,
                                "Name and number of sets are required",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@PrimaryButtonGenerator
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
                                "Training successfully created!",
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
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                TextGenerator("Need help?", MaterialTheme.colorScheme.onBackground, "small")
            }
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {

                SecondaryButtonGenerator(
                    text = "Back",
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
