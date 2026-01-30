package com.example.bakalarka.screens

import android.R.attr.value
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.example.bakalarka.other_classes.*
import com.example.bakalarka.other_classes.ElementSizeProvider.getSize
import com.example.bakalarka.other_classes.ElementSizeProvider.getSizeForElement



@Composable
private fun SearchCulumn() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        TextGenerator(
            "Search your training by name",
            MaterialTheme.colorScheme.onBackground,
            "subtitle"
        )

        OutlinedTextFieldGenerator(
            "",
            {},
            "Search",
            false,
            KeyboardType.Text,
            Icons.Default.Search
        )

        PrimaryButtonGenerator(
            text = "Search",
            onClick = {}
        )
    }
}

@Composable
private fun SearchResults(){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .systemBarsPadding()
            .background(MaterialTheme.colorScheme.background)
            .border(
                1.dp,
                MaterialTheme.colorScheme.onBackground,
                shape = MaterialTheme.shapes.medium
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        TextGenerator("Pick your training", MaterialTheme.colorScheme.onBackground, "subtitle")
        Spacer(
            modifier = Modifier.height(16.dp)
        )

        repeat(3) {
            Spacer(
                modifier = Modifier.height(8.dp)
            )
            SearchResultsRow()
        }
    }
}
@Composable
private fun SearchResultsRow(){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(getSizeForElement("ultrasmall"))
            .border(
                1.dp,
                MaterialTheme.colorScheme.onBackground,
                shape = MaterialTheme.shapes.medium
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        Box(modifier = Modifier.weight(3f),contentAlignment = Alignment.Center){
            TextGenerator("Training name", MaterialTheme.colorScheme.onBackground, "small")
        }
        Box(modifier = Modifier.weight(1.5f),contentAlignment = Alignment.Center){
            PrimaryButtonGenerator("Pick",{})
        }

    }
}

@Composable
private fun WorkoutsColumn(){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        /* ===== PREVIOUS WORKOUT ===== */

        Spacer(modifier = Modifier.height(10.dp))

        PreviousWorkoutColumn()

        /* ===== CURRENT EXERCISE ===== */

        CurrentExerciseColumn()


    }
}

@Composable
private fun PreviousWorkoutColumn(){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .border(
                1.dp,
                MaterialTheme.colorScheme.onBackground,
                shape = MaterialTheme.shapes.medium
            )
            .padding(start = 10.dp, end = 10.dp, top = 5.dp, bottom = 5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if (false) {
            TextGenerator(
                "Loading exercise...",
                MaterialTheme.colorScheme.onBackground,
                "small"
            )
            return@Column
        }


        if (true) {

            TextGenerator(
                "Your previous workout:",
                MaterialTheme.colorScheme.onBackground,
                "body"
            )



            TextGenerator("21.12", MaterialTheme.colorScheme.onBackground, "small")


            Row(modifier = Modifier.fillMaxWidth()) {
                val sets = (1..15).toList()  // Vytvoríme zoznam čísel od 1 do 15

                // Pre každú tretinu z 15 setov
                for (i in 0 until 3) {
                    Column(modifier = Modifier.weight(1f)) {
                        // Pre každý set v príslušnej tretine
                        for (j in 1..5) {
                            val setNumber = i * 5 + j  // Výpočet aktuálneho čísla setu
                            TextGenerator("Set $setNumber", MaterialTheme.colorScheme.onBackground, "ultrasmall")
                        }
                    }
                }
            }


        } else {
            TextGenerator(
                "No previous workouts",
                MaterialTheme.colorScheme.onBackground,
                "ultrasmall"
            )
        }
    }
}

@Composable
private fun ColumnScope.CurrentExerciseColumn() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if (false) {
            TextGenerator(
                "Loading exercise...",
                MaterialTheme.colorScheme.onBackground,
                "small"
            )
            return@Column
        }

        Spacer(modifier = Modifier.height(10.dp))

        TextGenerator(
            "Exercise one",
            MaterialTheme.colorScheme.onBackground,
            "subtitle"
        )

        Spacer(modifier = Modifier.height(10.dp))

        repeat(2) { index ->
            WorkoutInputColumn()
        }


        PrimaryButtonGenerator(
            text = "Next Exercise",
            onClick = {}
        )

        PrimaryButtonGenerator(
            text = "Back",
            onClick = {}
        )
    }
}

@Composable
private fun WorkoutInputColumn(){
    Column(modifier = Modifier.fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.onBackground, shape = MaterialTheme.shapes.medium)
            .padding(10.dp),
        Arrangement.Center,
        Alignment.CenterHorizontally
    ) {

        TextGenerator(
            "Set number 1",
            MaterialTheme.colorScheme.onBackground,
            "small"
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(){
            InputBox("reps",{},1f)
            Box(modifier = Modifier.weight(0.1f))
            InputBox("weight",{},1f)

        }
    }
    Spacer(modifier = Modifier.height(20.dp))

}

@Composable
private fun RowScope.InputBox(value: String, onValueChange: (String) -> Unit,weight: Float) {
    Box(
        modifier = Modifier
            .weight(weight)
            .height(getSizeForElement("ultrasmall")),
        contentAlignment = Alignment.Center
    ) {

        BasicTextField(
            value = value,
            onValueChange = {onValueChange(it)},
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            textStyle = TextStyle(
                fontSize = getSize("small"),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(3 * getSizeForElement("ultrasmall"))
                .border(
                    1.dp,
                    MaterialTheme.colorScheme.onBackground,
                    RoundedCornerShape(30)
                ),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    if (value.isEmpty()) {
                        TextGenerator(
                            "weight",
                            MaterialTheme.colorScheme.onBackground,
                            "small"
                        )
                    }
                    innerTextField()
                }
            }
        )


    }
}

@Composable
fun TrainingScreenTest() {

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp),
    ){
        if (false) {
            SearchCulumn()
        }


        else if (false) {
            SearchResults()
        }


        else if (true) {

            WorkoutsColumn()

        }
    }
    }





@Preview
@Composable
fun TrainingScreenTestPreview() {
    TrainingScreenTest()
}




