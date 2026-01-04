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

    var trainingName by remember { mutableStateOf("") }
    var trainingNumber by remember { mutableStateOf("") }

    var exercise by remember { mutableStateOf("") }
    var sets by remember { mutableStateOf("") }
    var currentExerciseIndex by remember { mutableStateOf(0) }

    var isFormVisible by remember { mutableStateOf(true) }
    var isExerciseFormVisible by remember { mutableStateOf(false) }

    // --- OPRAVENÁ A DOPLNENÁ ČASŤ ---
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // Získavame userId bezpečne, bez `!!`, aby aplikácia nespadla
    val userId = CurrentUserHolder.currentUser?.id

    // Tento stav bude držať ID tréningu, až keď ho reálne dostaneme z databázy
    var createdTrainingId by remember { mutableStateOf<Int?>(null) }
    // --- KONIEC OPRAVENEJ ČASTI ---

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (isFormVisible) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = (15.dp * ElementSizeProvider.getScale())),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextGenerator("Create new training", MaterialTheme.colorScheme.onBackground, "title")

                OutlinedTextFieldGenerator(trainingName, { trainingName = it }, "Training Name")
                OutlinedTextFieldGenerator(trainingNumber, { trainingNumber = it }, "Number of exercises", false, KeyboardType.Number)

                PrimaryButtonGenerator("Construct training", onClick = {
                    val number = trainingNumber.toIntOrNull()
                    if (trainingName.isNotBlank() && number != null && number > 0) {
                        if (userId == null) {
                            Toast.makeText(context, "Error: User not logged in.", Toast.LENGTH_SHORT).show()
                            return@PrimaryButtonGenerator
                        }

                        // Spustíme korutinu, ktorá počká na výsledok
                        scope.launch {
                            // Zavoláme `suspend` funkciu a počkáme na jej výsledok (ID)
                            Toast.makeText(
                                context,
                                "training name:"+trainingName+" number:"+number+" userId:"+userId,
                                Toast.LENGTH_SHORT
                            ).show()
                            val newId = viewModel.addTraining(trainingName, number, userId)

                            if (newId != null) {
                                // Ak sme úspešne získali ID...
                                createdTrainingId = newId // ...uložíme ho do nášho stavu
                                isFormVisible = false
                                isExerciseFormVisible = true
                                currentExerciseIndex = 1 // Začíname prvým cvikom
                                Toast.makeText(
                                    context,
                                    "Training created. Now add exercises.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                // Ak nastala chyba pri vytváraní tréningu
                                Toast.makeText(
                                    context,
                                    "Failed to create training.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    } else {
                        Toast.makeText(context, "Name and a valid number of exercises are required", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        } else if (isExerciseFormVisible) {
            val totalExercises = trainingNumber.toIntOrNull() ?: 0
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = (15.dp * ElementSizeProvider.getScale())),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextGenerator("Add Exercise $currentExerciseIndex / $totalExercises", MaterialTheme.colorScheme.onBackground, "title")

                OutlinedTextFieldGenerator(exercise, { exercise = it }, "Name of $currentExerciseIndex. exercise")
                OutlinedTextFieldGenerator(sets, { sets = it }, "Number of sets", false, KeyboardType.Number)

                // Zmeníme text tlačidla, ak ide o posledný cvik
                val buttonText = if (currentExerciseIndex == totalExercises) "Finish Training" else "Add Exercise"

                PrimaryButtonGenerator(buttonText, onClick = {
                    if (exercise.isNotBlank() && sets.isNotBlank()) {
                        // Spustíme korutinu na pridanie cviku
                        scope.launch {
                            viewModel.addExercise(createdTrainingId, userId, exercise, sets, currentExerciseIndex)

                            if (currentExerciseIndex == totalExercises) {
                                // Posledný cvik bol pridaný
                                Toast.makeText(context, "Training successfully created!", Toast.LENGTH_LONG).show()

                                // Resetujeme celý formulár do pôvodného stavu
                                exercise = ""
                                sets = ""
                                trainingName = ""
                                trainingNumber = ""
                                currentExerciseIndex = 0
                                createdTrainingId = null // Dôležité: vynulujeme ID
                                isExerciseFormVisible = false
                                isFormVisible = true
                            } else {
                                // Pokračujeme ďalším cvikom
                                Toast.makeText(context, "Exercise added: $exercise", Toast.LENGTH_SHORT).show()
                                exercise = ""
                                sets = ""
                                currentExerciseIndex++
                            }
                        }
                    } else {
                        Toast.makeText(context, "Name and number of sets are required", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }

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
                SecondaryButtonGenerator(text = "Help", onClick = { /*Tu si dam presmerovanie na napovedu*/ })
            }
            Box(modifier = Modifier.weight(0.7f))
        }
    }
}
