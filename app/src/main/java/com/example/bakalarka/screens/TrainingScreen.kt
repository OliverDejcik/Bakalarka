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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.example.bakalarka.ui.theme.BakalarkaTheme
import kotlinx.coroutines.currentCoroutineContext


data class Exercises(val name: String, val sets: String)


@Composable
fun TrainingScreen(viewModel: AppViewModel = viewModel()) {

    var search by remember {mutableStateOf("")}

    var searchbarColumn by remember { mutableStateOf(true) }
    var resultsColumn by remember { mutableStateOf(false) }


    var exercisesColumn by remember { mutableStateOf(false) }
    var numberOfExercises by remember { mutableStateOf(0) }

    var exerciseName by remember { mutableStateOf("") }
    var numberOfExerciseSets by remember { mutableStateOf(0) }


    // Use the viewModel() delegate to get the correct ViewModel instance



    var currentExerciseIndex by remember { mutableStateOf(1) }
    val actualExercises = remember { mutableStateListOf<Exercises>() }

    val setRepsValues = remember { mutableStateListOf<String>() }
    val setWeightValues = remember { mutableStateListOf<String>() }

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

            })


        }
    }else if(resultsColumn){
        Column(verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.background(MaterialTheme.colorScheme.background).fillMaxSize()
        )
        {
            TextGenerator("Pick training", color = MaterialTheme.colorScheme.onBackground, "subtitle")

            // This assumes 'getTrainings()' returns a list of objects with a 'name' property
            /*
            for(training in viewModel.getTrainingsj()){
                PrimaryButtonGenerator(
                    text = training.name,
                    onClick = {
                        numberOfExercises = viewModel.getTrainingExerciseNum(training.name)!!
                        actualExercises.clear()
                        for (name in viewModel.exercises){
                            if (name.training == training.name){
                                actualExercises.add(Exercises(name.name,name.sets))
                            }
                        }

                        if (actualExercises.isNotEmpty()) {
                            val firstExerciseSetCount = actualExercises[0].sets.toInt()
                            setRepsValues.clear()
                            setWeightValues.clear()
                            repeat(firstExerciseSetCount) {
                                setRepsValues.add("")
                                setWeightValues.add("")
                            }
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

    */


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

                    TextGenerator("Exercise "+ actualExercises.get(currentExerciseIndex).name, color = MaterialTheme.colorScheme.onBackground, "subtitle")
                    for (i in 1..actualExercises.get(currentExerciseIndex).sets.toInt()){
                        TextGenerator("Set "+i, color = MaterialTheme.colorScheme.onBackground, "small")
                        Row(horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            OutlinedTextFieldGenerator(
                                value = setRepsValues[i-1],
                                onValueChange = { setRepsValues[i-1] = it },
                                label = "Reps",
                                isPassword = false,
                                keyboardType = KeyboardType.Number,
                                modifier = Modifier.weight(0.5f)
                            )
                            OutlinedTextFieldGenerator(
                                value = setWeightValues[i-1],
                                onValueChange = { setWeightValues[i-1] = it },
                                label = "Weight",
                                isPassword = false,
                                keyboardType = KeyboardType.Number,
                                modifier = Modifier.weight(0.5f)
                            )
                        }
                    }
                    PrimaryButtonGenerator("Next", onClick = {

                        currentExerciseIndex++

                        // Ak ešte stále máme ďalšie cviky
                        if (currentExerciseIndex < numberOfExercises) {

                            val setsCount = actualExercises[currentExerciseIndex].sets.toInt()

                            // Reset setov
                            setRepsValues.clear()
                            setWeightValues.clear()

                            repeat(setsCount) {
                                setRepsValues.add("")
                                setWeightValues.add("")
                            }
                        }

                        Toast.makeText(context, "Exercise $currentExerciseIndex was completed", Toast.LENGTH_SHORT).show()
                    })



                }
            }else if(currentExerciseIndex == numberOfExercises){
                exercisesColumn = false
                searchbarColumn = true
            }


            PrimaryButtonGenerator("Back", onClick = {resultsColumn = true
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