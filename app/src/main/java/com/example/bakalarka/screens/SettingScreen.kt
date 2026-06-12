package com.example.bakalarka.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bakalarka.other_classes.*
import com.example.bakalarka.other_classes.ElementSizeProvider.getSize
import com.example.bakalarka.other_classes.ElementSizeProvider.getSizeForElement
import com.example.bakalarka.supabase.CurrentUserHolder
import com.example.bakalarka.supabase.updateTrainingName
import kotlinx.coroutines.launch
import com.example.bakalarka.R

@Composable
fun SettingScreen(viewModel: AppViewModel = viewModel()) {

    /* =========================
       BASIC SETUP
    ========================== */

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val userId = CurrentUserHolder.currentUser?.id
    val listTrainings by viewModel.trainings
    val listExercises by viewModel.exercises

    /* =========================
       UI STATE
    ========================== */

    var selectedTrainingId by remember { mutableStateOf(0) }
    var isTrainingSelected by remember { mutableStateOf(false) }

    var showDeleteDialog by remember { mutableStateOf(false) }

    var addExercise by remember { mutableStateOf(false) }
    var newExerciseName by remember { mutableStateOf("") }
    var newReps by remember { mutableStateOf("") }

    /* =========================
       LOAD DATA
    ========================== */

    LaunchedEffect(userId) {
        if (userId != null) {
            viewModel.loadTrainingsByUserId(userId)
        }
    }



    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {

        Text(
            text = "Edit your training routines",
            fontSize = 30.sp,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 3.dp)
        )


        if (!isTrainingSelected) {

            TrainingHeader()

            for (training in listTrainings) {

                TrainingDefaultRowStyled(
                    training = training.name, onEdit =
                        {
                            selectedTrainingId = training.id
                            viewModel.loadExercisesByTrainingId(training.id)
                            isTrainingSelected = true
                        },
                    onValueChange = {}
                )

            }
        }

        else {

            TrainingHeader()

            for (training in listTrainings) {
                if (training.id == selectedTrainingId) {

                    var editedTrainingName by remember(training.id) {
                        mutableStateOf(training.name)
                    }

                    TrainingEditRowStyled(
                        training = editedTrainingName,
                        onUpdate = {
                            scope.launch {
                                updateTrainingName(training.id, editedTrainingName)
                                viewModel.loadTrainingsByUserId(userId!!)
                                isTrainingSelected = false
                            }

                            Toast.makeText(context, R.string.settings_screen_training_updated, Toast.LENGTH_SHORT).show()
                        },
                        onDelete = {
                            selectedTrainingId = training.id
                            showDeleteDialog = true
                        },
                        onValueChange = {editedTrainingName = it}
                    )

                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            ExerciseHeader()


            for (exercise in listExercises) {

                var editedExerciseName by remember(exercise.id) {
                    mutableStateOf(exercise.name)
                }

                var editedReps by remember(exercise.id) {
                    mutableStateOf(exercise.sets_count.toString())
                }

                ExerciseRowStyled(exercise = editedExerciseName,
                    onExerciseChange = {editedExerciseName = it},
                    onRepsChange = {editedReps = it},
                    reps = editedReps,
                    onUpdate = {

                        val reps = editedReps.toIntOrNull()
                        if (reps == null) {
                            Toast.makeText(context, R.string.form_error_sets_invalid, Toast.LENGTH_SHORT).show()
                            return@ExerciseRowStyled
                        }

                        viewModel.updateExercise(
                            exercise.id,
                            editedExerciseName,
                            reps,
                            selectedTrainingId
                        )

                        Toast.makeText(context, R.string.settings_screen_exercise_updated, Toast.LENGTH_SHORT).show()
                    },
                    onDelete = {
                        viewModel.deleteExercise(
                            exercise.id,
                            trainingId = selectedTrainingId,
                            exercieseNum = exercise.order_index
                        )

                        Toast.makeText(context, R.string.settings_screen_exercise_deleted, Toast.LENGTH_SHORT).show()
                    }
                )

            }

            Row {
                SecondaryButtonGenerator(stringResource(R.string.button_add_exercise), onClick = {
                    addExercise = true
                })
            }
            if (addExercise) {
                Spacer(modifier = Modifier.height(12.dp))
                AddExerciseCard(newExerciseName,
                    {newExerciseName = it},
                    newReps,
                    {newReps = it},
                    onAddExercse = {
                        val order = listExercises.size + 1

                        viewModel.addExercise(
                            selectedTrainingId,
                            userId,
                            newExerciseName,
                            newReps,
                            listExercises.size,
                            order,
                            onSuccess = {
                                viewModel.loadExercisesByTrainingId(selectedTrainingId)
                                newExerciseName = ""
                                newReps = ""
                                addExercise = false
                            },
                            onError = {
                                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                )
            }

            PrimaryButtonGenerator(stringResource(R.string.button_back), onClick = {
                isTrainingSelected = false
            })
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text(stringResource(R.string.settings_screen_dialog_title)) },
            text = { Text(stringResource(R.string.settings_screen_dialog_text)) },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteTrainingById(selectedTrainingId)
                    showDeleteDialog = false
                    selectedTrainingId = 0
                    isTrainingSelected = false
                    viewModel.loadTrainingsByUserId(userId!!)
                }) {
                    Text(stringResource(R.string.settings_screen_dialog_yes))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text(stringResource(R.string.settings_screen_dialog_no))
                }
            }
        )
    }
}

@Composable
fun TrainingHeader(){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.onBackground)
    ) {
        HeaderCell(stringResource(R.string.settings_screen_training_name), 1f)
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun TrainingDefaultRowStyled(
    training: String,
    onValueChange: (String) -> Unit,
    onEdit: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(getSizeForElement("ultrasmall"))
            .border(1.dp, MaterialTheme.colorScheme.onBackground),
        verticalAlignment = Alignment.CenterVertically
    ) {

        InputCell(training, onValueChange, 1f)

        ActionButton(
            text = stringResource(R.string.button_edit),
            modifier = Modifier.weight(1f),
            onClick = onEdit
        )

    }
}

@Composable
fun TrainingEditRowStyled(
    training: String,
    onValueChange: (String) -> Unit,
    onUpdate: () -> Unit,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(getSizeForElement("ultrasmall"))
            .border(1.dp, MaterialTheme.colorScheme.onBackground),
        verticalAlignment = Alignment.CenterVertically
    ) {

        InputCell(training, onValueChange,1f)

        ActionButton(
            text = "Update",
            modifier = Modifier.weight(0.5f),
            onClick = onUpdate
        )
        ActionButton(
            text = "Delete",
            modifier = Modifier.weight(0.5f),
            onClick = onDelete
        )

    }
}

@Composable
fun ExerciseHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.onBackground)
    ) {
        HeaderCell(stringResource(R.string.settings_screen_exercise_name), 1.5f)
        HeaderCell(stringResource(R.string.settings_screen_sets), 0.5f)
        Spacer(modifier = Modifier.weight(2f))
    }
}

@Composable
fun ExerciseRowStyled(
    exercise: String,
    onExerciseChange: (String) -> Unit,
    onRepsChange: (String) -> Unit,
    reps: String,
    onUpdate: () -> Unit,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(getSizeForElement("ultrasmall"))
            .border(1.dp, MaterialTheme.colorScheme.onBackground),
        verticalAlignment = Alignment.CenterVertically
    ) {

        InputCell(exercise,onExerciseChange, 1.5f)


        InputCell(reps, onRepsChange, weight = 0.5f,KeyboardType.Number)

        ActionButton(
            text = stringResource(R.string.button_update),
            modifier = Modifier.weight(1f),
            onClick = onUpdate
        )

        ActionButton(
            text = stringResource(R.string.button_delete),
            modifier = Modifier.weight(1f),
            onClick = onDelete,
            danger = true
        )
    }
}

@Composable
private fun ActionButton(
    text: String,
    modifier: Modifier,
    danger: Boolean = false,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier.padding(horizontal = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        SettingsButtonGenerator(
            text = text,
            onClick = onClick,
        )
    }
}

@Composable
fun AddExerciseCard(
    newExerciseName: String,
    onExerciseNameChange: (String) -> Unit,
    newReps: String,
    onRepsChange: (String) -> Unit,
    onAddExercse: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp)
            .border(
                1.dp,
                MaterialTheme.colorScheme.onBackground,
                shape = MaterialTheme.shapes.medium
            )
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = stringResource(R.string.settings_screen_add_exercise),
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        OutlinedTextFieldGenerator(
            value = newExerciseName,
            onValueChange = onExerciseNameChange,
            label = stringResource(R.string.outlined_text_field_label_exercise_name),
            false
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextFieldGenerator(
            value = newReps,
            onValueChange = onRepsChange,
            label = stringResource(R.string.outlined_text_field_label_sets),
            false,
            keyboardType = KeyboardType.Number
        )

        Spacer(modifier = Modifier.height(12.dp))

        PrimaryButtonGenerator(
            text = stringResource(R.string.button_add_exercise),
            onClick = onAddExercse
        )
    }
}

@Composable
private fun RowScope.HeaderCell(text: String, weight: Float) {
    Box(
        modifier = Modifier
            .weight(weight)
            .height(getSizeForElement("ultrasmall"))
            .border(1.dp, MaterialTheme.colorScheme.onBackground),
        contentAlignment = Alignment.Center
    ) {
        TextGenerator(text, MaterialTheme.colorScheme.onBackground, "small", true)
    }
}

@Composable
private fun RowScope.InputCell(
    value: String,
    onValueChange: (String) -> Unit,
    weight: Float,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    Box(
        modifier = Modifier
            .weight(weight)
            .height(getSizeForElement("ultrasmall"))
            .border(1.dp, MaterialTheme.colorScheme.onBackground),
        contentAlignment = Alignment.Center
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            textStyle = TextStyle(
                fontSize = getSize("ultrasmall"),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}


@Preview(showBackground = true)
@Composable
fun SettingScreenPreview() {
    SettingScreen()
}