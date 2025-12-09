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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.bakalarka.Bakalarka
import com.example.bakalarka.other_classes.AppViewModel
import com.example.bakalarka.other_classes.ElementSizeProvider
import com.example.bakalarka.other_classes.OutlinedTextFieldGenerator
import com.example.bakalarka.other_classes.PrimaryButtonGenerator
import com.example.bakalarka.other_classes.SecondaryButtonGenerator
import com.example.bakalarka.other_classes.TextGenerator
import com.example.bakalarka.ui.theme.BakalarkaTheme

@SuppressLint("ViewModelConstructorInComposable")
@Composable
fun AddTrainingScreen() {

    // 1. Use the correct viewModel() delegate to get a shared ViewModel instance.
    val viewModel: AppViewModel = viewModel()

    var trainingName by remember { mutableStateOf("") }
    var trainingNumber by remember { mutableStateOf("") }

    var exercise by remember { mutableStateOf("") }
    var sets by remember { mutableStateOf("") }
    var currentExerciseIndex by remember { mutableStateOf(0) }

    var isFormVisible by remember { mutableStateOf(true) }
    var isExerciseFormVisible by remember { mutableStateOf(false) }

    var context = LocalContext.current




    Column(modifier = Modifier
        .fillMaxSize()
        .background(color = MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center

    ) {
        if (isFormVisible) {
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = (10.dp * ElementSizeProvider.getScale())), horizontalAlignment = Alignment.CenterHorizontally) {
                TextGenerator("Add Training", MaterialTheme.colorScheme.onBackground, "title")

                OutlinedTextFieldGenerator(trainingName,{trainingName = it},"Training Name")
                OutlinedTextFieldGenerator(trainingNumber,{trainingNumber = it},"Number of exercises",false, KeyboardType.Number)

                PrimaryButtonGenerator("Construct training", onClick = {
                    if (trainingNumber.isNotEmpty() && trainingName.isNotEmpty()){
                        isFormVisible = false
                        isExerciseFormVisible = true
                        currentExerciseIndex = 1
                        viewModel.setTrainingName(trainingName)
                        viewModel.setTrainingNumber(trainingNumber)
                    }
                    else{
                        Toast.makeText(context, "Name and number of exercises are required", Toast.LENGTH_SHORT).show()
                    }

                })
            }
        }else if(isExerciseFormVisible) {
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = (10.dp * ElementSizeProvider.getScale())), horizontalAlignment = Alignment.CenterHorizontally) {
                TextGenerator("Add Training", MaterialTheme.colorScheme.onBackground, "title")

                OutlinedTextFieldGenerator(exercise,{exercise = it},"Name of "+(currentExerciseIndex)+". exercise exercise")
                OutlinedTextFieldGenerator(sets,{sets = it},"Number of sets",false, KeyboardType.Number)

                // 2. Safely convert trainingNumber to an Int to prevent crashes.
                if(currentExerciseIndex == (trainingNumber.toInt())){
                    PrimaryButtonGenerator("Add an Exercise", onClick = {
                        if (exercise.isNotEmpty() && sets.isNotEmpty()){
                            viewModel.addExercise(exercise, sets)
                            Toast.makeText(context, "Training added name: "+viewModel.trainingName+"  number of exercises: "+viewModel.trainingNumber+"  ", Toast.LENGTH_SHORT).show()
                            exercise = ""
                            sets = ""
                            trainingName = ""
                            trainingNumber = ""
                            currentExerciseIndex = 0
                            isExerciseFormVisible = false
                            isFormVisible = true
                            //send to database funkcia
                        }
                        else{
                            Toast.makeText(context, "Name and number of sets are required", Toast.LENGTH_SHORT).show()
                        }
                    }
                    )
                }else{
                    PrimaryButtonGenerator("Add an Exercise", onClick = {

                        if (exercise.isNotEmpty() && sets.isNotEmpty()){
                            viewModel.addExercise(exercise, sets)
                            Toast.makeText(context, "Exercise added name: "+exercise+"  sets: "+sets, Toast.LENGTH_SHORT).show()
                            exercise = ""
                            sets = ""
                            currentExerciseIndex ++
                        }
                        else{
                            Toast.makeText(context, "Name and number of sets are required", Toast.LENGTH_SHORT).show()
                        }
                    }
                    )
                }
            }
        }

        
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically){

            Box(modifier = Modifier.weight(0.7f)){

            }

            Box(modifier = Modifier.weight(1f),contentAlignment = Alignment.Center){
                TextGenerator("Need help?", MaterialTheme.colorScheme.onBackground,"small")
            }
            Box(modifier = Modifier.weight(1f),contentAlignment = Alignment.Center){
                SecondaryButtonGenerator(text = "Help", onClick = { /*Tu si dam presmerovanie na napovedu*/ })
            }

            Box(modifier = Modifier.weight(0.7f)){

            }

        }
    }
}



@Preview(showSystemUi = true, showBackground = true)
@Composable
fun AddTrainingPreview() {
    BakalarkaTheme(darkTheme = true, dynamicColor = false) {
        // Removed Bakalarka() from preview as it's not needed to preview a single screen.
        AddTrainingScreen()
    }
}
