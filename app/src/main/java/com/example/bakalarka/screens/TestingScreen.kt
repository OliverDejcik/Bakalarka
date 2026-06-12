package com.example.bakalarka.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bakalarka.other_classes.*
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun calendarBuilder(viewModel: AppViewModel) { // Pridaný parameter

    val daysOfWeek = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    val currentMonth = remember { YearMonth.now() }
    val currentDay = remember { LocalDate.now().dayOfMonth }
    val isActualMonth = remember { currentMonth == YearMonth.now() }

    // Správny odber stavu zo StateFlow
    val selectedDate by viewModel.selectedDate.collectAsState()

    val daysInMonth = currentMonth.lengthOfMonth()
    val firstDayOfMonth = currentMonth.atDay(1).dayOfWeek.value

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onBackground,
                shape = RoundedCornerShape(10.dp)
            )
            .padding(10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(){
            Box(modifier = Modifier.weight(1f)){}

            Box(modifier = Modifier.weight(10f),
                contentAlignment = Alignment.Center){
                TextGenerator("Calendar", MaterialTheme.colorScheme.onBackground, "subtitle")

            }

            Box(modifier = Modifier.weight(1f)){
                IconButton({

                }
                ) {
                    Icon(
                        Icons.Default.Settings,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.onBackground


                    )
                }

            }


        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier.fillMaxWidth()
                .border(1.dp, MaterialTheme.colorScheme.onBackground, RectangleShape)
        ) {
            items(count = 7){ index ->
                Box(modifier = Modifier.size(40.dp), contentAlignment = Alignment.Center){
                    Text(text = daysOfWeek[index])
                }
            }

            items(firstDayOfMonth - 1) {
                Box(modifier = Modifier.size(40.dp))
            }

            items(daysInMonth) { day ->
                val dateAtDay = currentMonth.atDay(day + 1)
                val isSelected = selectedDate == dateAtDay
                val isToday = isActualMonth && (day == currentDay - 1)

                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clickable { viewModel.onDateSelected(dateAtDay) } // Volanie funkcie vo ViewModeli
                        .background(
                            color = if (isToday) MaterialTheme.colorScheme.primary
                            else if (isSelected) Color.Transparent
                            else Color.Transparent,
                        )
                        .then(
                            if (isSelected) Modifier.border(
                                1.dp,
                                MaterialTheme.colorScheme.onBackground,
                            )
                            else Modifier
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${day + 1}",
                        color = if (isSelected) MaterialTheme.colorScheme.onBackground
                        else MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
        Column(Modifier.fillMaxWidth()){
            Row(verticalAlignment = Alignment.CenterVertically){
                Icon(
                    Icons.Default.Circle,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.primary
                )
                TextGenerator(text = "Today",color = MaterialTheme.colorScheme.onBackground, textType = "ultrasmall")
            }
            Row(verticalAlignment = Alignment.CenterVertically){
                Icon(
                    Icons.Default.Circle,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.secondary
                )
                TextGenerator(text = "Done W.O.",color = MaterialTheme.colorScheme.onBackground, textType = "ultrasmall")
            }
            Row(verticalAlignment = Alignment.CenterVertically){
                Icon(
                    Icons.Default.Circle,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.tertiary
                )
                TextGenerator(text = "Planned W.O.",color = MaterialTheme.colorScheme.onBackground, textType = "ultrasmall")
            }

        }
    }
}

@Composable
fun ScreenTest(viewModel: AppViewModel = viewModel()) {

    val selectedDate by viewModel.selectedDate.collectAsState()

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        calendarBuilder(viewModel)
        Spacer(modifier = Modifier.height(16.dp))

        TextGenerator(text = "Selected day: ${selectedDate ?: "None"}",color = MaterialTheme.colorScheme.onBackground, textType = "subtitle")
    }
}





@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TrainingScreenTestPreview() {
    ScreenTest()
}




