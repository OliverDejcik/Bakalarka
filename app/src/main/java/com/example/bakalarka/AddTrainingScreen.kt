package com.example.bakalarka

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout

@Composable
fun AddTrainingScreen() {
    ConstraintLayout(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        val (ScreenLabel,HelpButton,inputLayout,inputButton) =createRefs()

        var trainingName by remember { mutableStateOf("") }
        var trainingNumber by remember { mutableStateOf("") }



        TextGenerator("Add Training Screen", MaterialTheme.colorScheme.onBackground, "title", modifier = Modifier.constrainAs(ScreenLabel){
            top.linkTo(parent.top)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        })

        ButtonGenerator(text = "Help", onClick = { /*Tu si dam presmerovanie na napovedu*/ }, modifier = Modifier.constrainAs(HelpButton){
            top.linkTo(ScreenLabel.bottom)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        })

        Row(modifier = Modifier.fillMaxWidth().constrainAs(inputLayout){
            top.linkTo(HelpButton.bottom)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }){
            OutlinedTextFieldGenerator(trainingName,{trainingName = it},"Training Name",modifier = Modifier.weight(1f))
            OutlinedTextFieldGenerator(trainingNumber,{trainingNumber = it},"Number of exercises",modifier = Modifier.weight(1f))
        }



        ButtonGenerator("add training", onClick = {/* posli */}, modifier = Modifier.constrainAs(inputButton){
            top.linkTo(inputLayout.bottom)
        })


    }







}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun AddTrainingPreview() {
    AddTrainingScreen()
}

