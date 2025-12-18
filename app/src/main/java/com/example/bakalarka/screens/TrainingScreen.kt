package com.example.bakalarka.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bakalarka.other_classes.AppViewModel
import com.example.bakalarka.other_classes.OutlinedTextFieldGenerator
import com.example.bakalarka.other_classes.PrimaryButtonGenerator
import com.example.bakalarka.other_classes.TextGenerator
import com.example.bakalarka.supabase.CurrentUserHolder
import com.example.bakalarka.supabase.addWorkoutToDB
import com.example.bakalarka.ui.theme.BakalarkaTheme
import kotlinx.coroutines.launch


@Composable
fun TrainingScreen(viewModel: AppViewModel = viewModel()) {

    var search by remember {mutableStateOf("")}
    val listTrainings by viewModel.trainings
    val listExercises by viewModel.exercises
    val userId = CurrentUserHolder.currentUser?.id
    var workoutId: Int? by remember { mutableStateOf(0) }


    var searchbarColumn by remember { mutableStateOf(true) }
    var resultsColumn by remember { mutableStateOf(false) }


    var exercisesColumn by remember { mutableStateOf(false) }
    var numberOfExercises by remember { mutableStateOf(0) }

    var exerciseName by remember { mutableStateOf("") }
    var numberOfExerciseSets by remember { mutableStateOf(0) }


    val scope = rememberCoroutineScope()



    var currentExerciseIndex by remember { mutableStateOf(1) }

    val setDataSet = remember { mutableStateListOf<SetData>() }



    val context = androidx.compose.ui.platform.LocalContext.current













    if(searchbarColumn) {
        Column(verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.background(MaterialTheme.colorScheme.background).fillMaxSize()
        )
        {
            TextGenerator("Search your traning by name", color = MaterialTheme.colorScheme.onBackground, "subtitle")

            OutlinedTextFieldGenerator(search, { search = it }, "Search",false, KeyboardType.Text, Icons.Default.Search)
            PrimaryButtonGenerator("Search", onClick = {
                resultsColumn = true
                searchbarColumn = false
                if (userId != null) {
                    viewModel.loadTrainingsByName(search, userId)
                } else {
                    Toast.makeText(context, "User not logged in", Toast.LENGTH_SHORT).show()
                }


            })


        }
    }else if(resultsColumn){
        Column(verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.background(MaterialTheme.colorScheme.background).fillMaxSize()
        )
        {
            TextGenerator("Pick training", color = MaterialTheme.colorScheme.onBackground, "subtitle")



            for(training in listTrainings) {
                PrimaryButtonGenerator(
                    text = training.name,
                    onClick = {
                        numberOfExercises = training.number_of_exercises
                        if(userId != null){
                            scope.launch{
                                workoutId =  viewModel.addWorkout(userId,training.id)
                            }

                        }else{
                            Toast.makeText(context, "User not logged in", Toast.LENGTH_SHORT).show()
                        }
                        currentExerciseIndex = 0
                        exercisesColumn = true
                        resultsColumn = false

                    }
                )
            }
            PrimaryButtonGenerator("Back", onClick = {
                resultsColumn = false
                searchbarColumn = true
            },modifier = Modifier.padding(top = 10.dp)
            )




        }
    }else if(exercisesColumn) {
        Column(verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.background(MaterialTheme.colorScheme.background).fillMaxSize()
        ){


            if (currentExerciseIndex < numberOfExercises){
                Column(verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.background(MaterialTheme.colorScheme.background).fillMaxWidth()
                ) {
                    var textString = remember { mutableStateOf("") }

                    TextGenerator("Exercise "+listExercises.first {it.order_index == currentExerciseIndex}.name, color = MaterialTheme.colorScheme.onBackground, "subtitle")
                    for (i in 1..listExercises.first {it.order_index == currentExerciseIndex}.sets_count){
                        TextGenerator("Set "+i, color = MaterialTheme.colorScheme.onBackground, "small")
                        Row(horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            OutlinedTextFieldGenerator(
                                value = setDataSet[i].reps,
                                onValueChange = { setDataSet[i] = setDataSet[i].copy(reps = it) },
                                label = "Reps",
                                isPassword = false,
                                keyboardType = KeyboardType.Number,
                                modifier = Modifier.weight(0.5f)
                            )
                            OutlinedTextFieldGenerator(
                                value = setDataSet[i].weight,
                                onValueChange = { setDataSet[i] = setDataSet[i].copy(weight = it) },
                                label = "Weight",
                                isPassword = false,
                                keyboardType = KeyboardType.Number,
                                modifier = Modifier.weight(0.5f)
                            )
                        }
                    }
                    PrimaryButtonGenerator("Next", onClick = {

                        scope.launch {
                            if (userId != null && workoutId != null) {
                                // 'setDataSet' je zoznam, ktorý posielame
                                viewModel.addExerciseToWorkout(userId, workoutId!!, listExercises.first{it.order_index == currentExerciseIndex}.id, setDataSet.toList(),listExercises.first{it.order_index == currentExerciseIndex}.sets_count)
                                Toast.makeText(context, "Exercise '${listExercises.first{it.order_index == currentExerciseIndex}.name}' saved!", Toast.LENGTH_SHORT).show()

                                // Prejdeme na ďalší cvik alebo ukončíme tréning
                                if (currentExerciseIndex < numberOfExercises) {
                                    currentExerciseIndex++
                                } else {
                                    Toast.makeText(context, "Workout finished!", Toast.LENGTH_LONG).show()
                                    exercisesColumn = false
                                    searchbarColumn = true
                                }
                            }
                        }
                        currentExerciseIndex++
                    })



                }
            }else if(currentExerciseIndex == numberOfExercises){
                exercisesColumn = false
                searchbarColumn = true
            }


            PrimaryButtonGenerator("Back", onClick = {
                resultsColumn = true
                searchbarColumn = false}
            )
        }
    }

}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun TrainingPreview() {
    BakalarkaTheme(darkTheme = true, dynamicColor = false) {
        TrainingScreen(viewModel())
    }
}

data class SetData(
    var reps: String = "",
    var weight: String = ""
)
