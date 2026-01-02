package com.example.bakalarka.screens

import android.provider.Settings
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.bakalarka.Bakalarka
import com.example.bakalarka.data.Training
import com.example.bakalarka.other_classes.AppViewModel
import com.example.bakalarka.other_classes.PrimaryButtonGenerator
import com.example.bakalarka.other_classes.SecondaryButtonGenerator
import com.example.bakalarka.other_classes.SettingsButtonGenerator
import com.example.bakalarka.other_classes.TextGenerator
import com.example.bakalarka.supabase.CurrentUserHolder
import com.example.bakalarka.ui.theme.BakalarkaTheme

@Composable
fun SettingScreen(viewModel: AppViewModel = viewModel()) {


    val userId = CurrentUserHolder.currentUser?.id
    val listTrainings by viewModel.trainings
    val listExercises by viewModel.exercises


    LaunchedEffect(userId) {
        if (userId != null) {
            viewModel.loadTrainingsByUserId(userId)
        }
    }

    var selectedTraining by remember {mutableStateOf("")}


    Column(verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.background(MaterialTheme.colorScheme.background).fillMaxSize()
    )
    {
        Text(text = "Edit your training routines", fontSize = 30.sp,color = MaterialTheme.colorScheme.onBackground,modifier = Modifier.padding(bottom = 3.dp))

        Row(modifier = Modifier.padding(bottom = 3.dp).fillMaxWidth().border(1.dp, MaterialTheme.colorScheme.onBackground)){
            TextGenerator("Training Name", MaterialTheme.colorScheme.onBackground, "body",true,modifier = Modifier.weight(1f))

            Box(modifier = Modifier.weight(1f)){
            }
        }

        for (training in listTrainings) {
            Row(modifier = Modifier.border(1.dp, MaterialTheme.colorScheme.onBackground)){
                Box(modifier = Modifier.weight(1.5f),contentAlignment = Alignment.Center){
                    TextGenerator("${training.name}", MaterialTheme.colorScheme.onBackground, "body",true)

                }

                SettingsButtonGenerator("Edit", onClick = {
                    selectedTraining = training.name
                    viewModel.loadExercisesByTrainingId(training.id)
                                                         }, modifier = Modifier.weight(0.7f))

                SettingsButtonGenerator("Delete", onClick = {/* funkcia na popup pre potvrdenie vymazania*/}, modifier = Modifier.weight(0.8f))
            }
        }






    }
}

