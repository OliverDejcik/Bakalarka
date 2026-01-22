package com.example.bakalarka.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bakalarka.other_classes.*
import com.example.bakalarka.other_classes.ElementSizeProvider.getSize
import com.example.bakalarka.other_classes.ElementSizeProvider.getSizeForElement
/*
@Composable
fun SettingScreenDesignOnly() {

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Edit your training routines",
            fontSize = 30.sp,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        /* =========================
           TRAINING DETAIL
        ========================== */

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, MaterialTheme.colorScheme.onBackground)
        ) {
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                TextGenerator("Training Name", MaterialTheme.colorScheme.onBackground, "body", true)
            }
            Box(modifier = Modifier.weight(1f))
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {

            Box(modifier = Modifier.weight(1.5f)
                .height(getSizeForElement("ultrasmall"))
                .fillMaxWidth()
                .border(1.dp, MaterialTheme.colorScheme.onBackground),
                contentAlignment = Alignment.Center) {
                BasicTextField(
                    value = "Push day",
                    onValueChange = {},
                    textStyle = TextStyle(
                        fontSize = getSize("ultrasmall"),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Box(
                modifier = Modifier.weight(0.7f),
                contentAlignment = Alignment.Center
            ) {
                SettingsButtonGenerator("Update", onClick = {})
            }
        }

        /* =========================
           EXERCISES HEADER
        ========================== */

        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Box(modifier = Modifier.weight(1f).border(2.dp, MaterialTheme.colorScheme.onBackground), contentAlignment = Alignment.Center) {
                TextGenerator("Exercise", MaterialTheme.colorScheme.onBackground, "small", true)
            }
            Box(modifier = Modifier.weight(0.5f).border(2.dp, MaterialTheme.colorScheme.onBackground), contentAlignment = Alignment.Center) {
                TextGenerator("Reps", MaterialTheme.colorScheme.onBackground, "small", true)
            }
            Box(modifier = Modifier.weight(1.5f))
        }

        /* =========================
           EXERCISES LIST (STATIC)
        ========================== */

        repeat(2) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {

                Box(modifier = Modifier.weight(1f)
                    .height(getSizeForElement("ultrasmall"))
                    .fillMaxWidth()
                    .border(1.dp, MaterialTheme.colorScheme.onBackground),
                    contentAlignment = Alignment.Center
                ) {
                    BasicTextField(
                        value = "Bench Press",
                        onValueChange = {},
                        textStyle = TextStyle(
                            fontSize = getSize("ultrasmall"),
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onBackground
                        ),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                    )

                }

                Box(
                    modifier = Modifier
                        .height(getSizeForElement("ultrasmall"))
                        .fillMaxWidth()
                        .weight(0.5f)
                        .border(1.dp, MaterialTheme.colorScheme.onBackground),
                    contentAlignment = Alignment.Center
                ) {
                    BasicTextField(
                        value = "10",
                        onValueChange = {},
                        textStyle = TextStyle(
                            fontSize = getSize("ultrasmall"),
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onBackground
                        ),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Box(
                    modifier = Modifier.weight(0.7f),
                    contentAlignment = Alignment.Center
                ) {
                    SettingsButtonGenerator("Update", onClick = {})
                }

                Box(
                    modifier = Modifier.weight(0.8f),
                    contentAlignment = Alignment.Center
                ) {
                    SettingsButtonGenerator("Delete", onClick = {})
                }
            }
        }


        /* =========================
           ADD EXERCISE
        ========================== */

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Add exercise",
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 18.sp
        )

        OutlinedTextFieldGenerator(
            value = "New exercise",
            onValueChange = {},
            label = "Exercise name",
            false
        )

        OutlinedTextFieldGenerator(
            value = "12",
            onValueChange = {},
            label = "Reps",
            false,
            keyboardType = KeyboardType.Number
        )

        SettingsButtonGenerator("Add exercise", onClick = {})

        Spacer(modifier = Modifier.height(24.dp))

        PrimaryButtonGenerator("Back", onClick = {})
    }
}
*/
@Composable
fun SettingScreenDesignOnly() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        ExerciseHeader()

        Spacer(modifier = Modifier.height(8.dp))

        repeat(3) {
            ExerciseRowStyled(
                exercise = "Bench Press",
                reps = "10",
                onUpdate = {},
                onDelete = {}
            )
        }

        AddExerciseCard()

        Spacer(modifier = Modifier.height(24.dp))

        PrimaryButtonGenerator("Back", onClick = {})
    }
}

@Composable
fun ExerciseHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.outline)
    ) {
        HeaderCell("Exercise", 1.5f)
        HeaderCell("Reps", 0.5f)
        Spacer(modifier = Modifier.weight(1.5f))
    }
}

@Composable
fun ExerciseRowStyled(
    exercise: String,
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

        InputCell(exercise, 1.5f)

        InputCell(reps, 0.5f, KeyboardType.Number)

        ActionButton(
            text = "Update",
            modifier = Modifier.weight(0.75f),
            onClick = onUpdate
        )

        ActionButton(
            text = "Delete",
            modifier = Modifier.weight(0.75f),
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
fun AddExerciseCard() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp)
            .border(
                1.dp,
                MaterialTheme.colorScheme.outline,
                shape = MaterialTheme.shapes.medium
            )
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Add exercise",
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        OutlinedTextFieldGenerator(
            value = "",
            onValueChange = {},
            label = "Exercise name",
            false
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextFieldGenerator(
            value = "",
            onValueChange = {},
            label = "Reps",
            false,
            keyboardType = KeyboardType.Number
        )

        Spacer(modifier = Modifier.height(12.dp))

        PrimaryButtonGenerator(
            text = "Add exercise",
            onClick = {}
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
private fun ExerciseRow() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        InputCell("Bench Press", 1.5f)

        InputCell("10", 0.5f, KeyboardType.Number)

        Box(modifier = Modifier.weight(0.75f)) {
            SettingsButtonGenerator("Update", {})
        }

        Box(modifier = Modifier.weight(0.75f)) {
            SettingsButtonGenerator("Delete", {})
        }
    }
}

@Composable
private fun RowScope.InputCell(
    value: String,
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
            onValueChange = {},
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
fun SettingScreenDesignPreview() {
    SettingScreenDesignOnly()
}
