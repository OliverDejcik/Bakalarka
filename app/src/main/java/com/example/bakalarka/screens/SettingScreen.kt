package com.example.bakalarka.screens

import android.provider.Settings
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.bakalarka.Bakalarka
import com.example.bakalarka.data.Training
import com.example.bakalarka.other_classes.AppViewModel
import com.example.bakalarka.other_classes.OutlinedTextFieldGenerator
import com.example.bakalarka.other_classes.PrimaryButtonGenerator
import com.example.bakalarka.other_classes.SecondaryButtonGenerator
import com.example.bakalarka.other_classes.SettingsButtonGenerator
import com.example.bakalarka.other_classes.TextGenerator
import com.example.bakalarka.supabase.CurrentUserHolder
import com.example.bakalarka.ui.theme.BakalarkaTheme

@Composable
fun SettingScreen(viewModel: AppViewModel = viewModel()) {

    val context = LocalContext.current

    val userId = CurrentUserHolder.currentUser?.id
    val listTrainings by viewModel.trainings
    val listExercises by viewModel.exercises


    LaunchedEffect(userId) {
        if (userId != null) {
            viewModel.loadTrainingsByUserId(userId)
        }
    }

    var selectedTrainingId by remember {mutableStateOf(0)}

    var isTrainingSelected by remember {mutableStateOf(false)}







    Column(verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.background(MaterialTheme.colorScheme.background).fillMaxSize()
    )
    {
        Text(text = "Edit your training routines", fontSize = 30.sp,color = MaterialTheme.colorScheme.onBackground,modifier = Modifier.padding(bottom = 3.dp))



        if (!isTrainingSelected) {

            Row(modifier = Modifier.padding(bottom = 3.dp).fillMaxWidth().border(1.dp, MaterialTheme.colorScheme.onBackground)){
                TextGenerator("Training Name", MaterialTheme.colorScheme.onBackground, "body",true,modifier = Modifier.weight(1f))

                Box(modifier = Modifier.weight(1f),contentAlignment = Alignment.Center){
                }
            }
            for (training in listTrainings) {
                Row(modifier = Modifier.border(1.dp, MaterialTheme.colorScheme.onBackground)){
                    Box(modifier = Modifier.weight(1.5f),contentAlignment = Alignment.Center){
                        TextGenerator("${training.name}", MaterialTheme.colorScheme.onBackground, "body",true)

                    }

                    SettingsButtonGenerator("Edit", onClick = {
                        selectedTrainingId = training.id
                        viewModel.loadExercisesByTrainingId(training.id)
                        isTrainingSelected = true
                    }, modifier = Modifier.weight(0.7f))

                    SettingsButtonGenerator("Delete", onClick = { /* popup na potvrdenie vymazania */ }, modifier = Modifier.weight(0.8f))
                }
            }
        }else{
            for (training in listTrainings) {
                if (training.id == selectedTrainingId) {

                    var editedTrainingName by remember(training.id) {
                        mutableStateOf(training.name)
                    }

                    Row(
                        modifier = Modifier
                            .padding(bottom = 3.dp)
                            .fillMaxWidth()
                            .border(1.dp, MaterialTheme.colorScheme.onBackground)
                    ) {
                        TextGenerator(
                            "Training Name",
                            MaterialTheme.colorScheme.onBackground,
                            "body",
                            true,
                            modifier = Modifier.weight(1f)
                        )
                        Box(modifier = Modifier.weight(1f))
                    }

                    Row {
                        Box(
                            modifier = Modifier.weight(1.5f),
                            contentAlignment = Alignment.Center
                        ) {
                            OutlinedTextFieldGenerator(
                                value = editedTrainingName,
                                onValueChange = { editedTrainingName = it },
                                label = "",
                                false
                            )
                        }

                        Box(
                            modifier = Modifier.weight(0.7f),
                            contentAlignment = Alignment.Center
                        ) {
                            SettingsButtonGenerator(
                                "Update",
                                onClick = {
                                    // viewModel.updateTrainingName(
                                    //     training.id,
                                    //     editedTrainingName
                                    // )
                                    Toast.makeText(
                                        context,
                                        "Training updated",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            )
                        }
                    }
                }
            }

            Row(modifier = Modifier.padding(bottom = 3.dp).fillMaxWidth().border(1.dp, MaterialTheme.colorScheme.onBackground)){
                Box(modifier = Modifier.weight(1f),contentAlignment = Alignment.Center){
                    TextGenerator("Exercise name", MaterialTheme.colorScheme.onBackground, "body",true)
                }
                Box(modifier = Modifier.weight(0.5f),contentAlignment = Alignment.Center){
                    TextGenerator("Reps", MaterialTheme.colorScheme.onBackground, "body",true)
                }


                Box(modifier = Modifier.weight(0.7f)){
                }
            }
            for (exercise in listExercises) {

                var editedExerciseName by remember(exercise.id) {
                    mutableStateOf(exercise.name)
                }

                var editedReps by remember(exercise.id) {
                    mutableStateOf(exercise.sets_count.toString())
                }

                Row {
                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                        OutlinedTextFieldGenerator(
                            value = editedExerciseName,
                            onValueChange = { editedExerciseName = it },
                            label = "",
                            false
                        )
                    }

                    Box(modifier = Modifier.weight(0.5f), contentAlignment = Alignment.Center) {
                        OutlinedTextFieldGenerator(
                            value = editedReps,
                            onValueChange = { editedReps = it },
                            label = "",
                            false
                        )
                    }

                    Box(contentAlignment = Alignment.Center) {
                        SettingsButtonGenerator("Update", onClick = {
                            // viewModel.updateExerciseNameReps(
                            //     exercise.id,
                            //     editedExerciseName,
                            //     editedReps.toInt()
                            // )
                            Toast.makeText(context, "Exercise updated", Toast.LENGTH_SHORT).show()
                        })
                    }
                }
            }



        }







    }
}

