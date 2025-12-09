package com.example.bakalarka.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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

@Composable
fun TrainingScreen() {

    var search by remember {mutableStateOf("")}

    var searchbarColumn by remember { mutableStateOf(true) }
    var resultsColumn by remember { mutableStateOf(false) }


    var exercisesColumn by remember { mutableStateOf(false) }
    var numberOfExercises by remember { mutableStateOf(0) }

    var exerciseName by remember { mutableStateOf("") }
    var numberOfExerciseSets by remember { mutableStateOf(0) }


    // Use the viewModel() delegate to get the correct ViewModel instance
    val viewModel: AppViewModel = viewModel()
    if (viewModel.trainingNames.isEmpty()){
        viewModel.addTraining("prvy pokus",2)
        viewModel.addTraining("druhy pokus",3)
        viewModel.addTraining("treti pokus",4)
    }





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
            for(training in viewModel.getTrainings()){
                PrimaryButtonGenerator(
                    text = training.name,
                    onClick = {
                        numberOfExercises = viewModel.getTrainingExerciseNum(training.name)!!
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
            TextGenerator("Number of Exercises: "+numberOfExercises, color = MaterialTheme.colorScheme.onBackground, "subtitle")
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
        TrainingScreen()
    }
}