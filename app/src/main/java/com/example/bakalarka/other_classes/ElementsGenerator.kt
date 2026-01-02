package com.example.bakalarka.other_classes

// Android
import android.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView

// Compose – layout & runtime
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color as ComposeColor
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

// App

// MPAndroidChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener

// Date
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@Composable
fun PrimaryButtonGenerator(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(onClick = {onClick()},modifier = modifier,colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.tertiary), shape = RoundedCornerShape(35)){
        TextGenerator(text, MaterialTheme.colorScheme.onTertiary, "button",false)
    }
}

@Composable
fun SecondaryButtonGenerator(text: String,   onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(onClick = {onClick()},modifier = modifier,colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary), shape = RoundedCornerShape(35)){
        TextGenerator(text, MaterialTheme.colorScheme.onSecondary, "label",false)
    }
}

@Composable
fun SettingsButtonGenerator(text: String,   onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(onClick = {onClick()},modifier = modifier,colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary), shape = RoundedCornerShape(35)){
        TextGenerator(text, MaterialTheme.colorScheme.onSecondary, "ultrasmall",false)
    }
}

@Composable
fun TextGenerator(
    text: String,
    color: ComposeColor,
    textType: String,
    bold: Boolean = false,
    textAlign: TextAlign? = null,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        fontSize = ElementSizeProvider.getSize(textType),
        fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal,
        color = color,
        textAlign = textAlign,
        modifier = modifier
    )
}


@Composable
fun OutlinedTextFieldGenerator(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isPassword: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    leadingIcon: ImageVector? = null,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = leadingIcon?.let { icon ->
            { Icon(imageVector = icon, contentDescription = null) }
        },
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        shape = RoundedCornerShape(25),
        modifier = modifier,
        colors = TextFieldDefaults.colors(
            focusedTextColor = MaterialTheme.colorScheme.onBackground,
            unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
            focusedLabelColor = MaterialTheme.colorScheme.onBackground,
            unfocusedLabelColor = MaterialTheme.colorScheme.onBackground,
            focusedIndicatorColor = MaterialTheme.colorScheme.onBackground,
            unfocusedIndicatorColor = MaterialTheme.colorScheme.onBackground,
            focusedContainerColor = MaterialTheme.colorScheme.background,
            unfocusedContainerColor = MaterialTheme.colorScheme.background,
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectBoxMaterial(
    options: List<String>,
    selected: String,
    onSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            readOnly = true,
            value = selected,
            onValueChange = {},
            label = { Text("Select option") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded)
            }
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun LineChartView(
    data: List<Pair<Float, Float>>, // x = epochDay, y = 1RM
    descriptionText: String,
) {
    var selectedValue by remember { mutableStateOf<Float?>(null) }
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }

    val onBackgroundColor = composeColorToAndroid(MaterialTheme.colorScheme.onBackground)
    val BackgroundColor = composeColorToAndroid(MaterialTheme.colorScheme.background)
    val PrimaryColor = composeColorToAndroid(MaterialTheme.colorScheme.primary)
    val SecondaryColor = composeColorToAndroid(MaterialTheme.colorScheme.secondary)
    val TertiaryColor = composeColorToAndroid(MaterialTheme.colorScheme.tertiary)
    val onPrimaryColor = composeColorToAndroid(MaterialTheme.colorScheme.onPrimary)
    val onSecondaryColor = composeColorToAndroid(MaterialTheme.colorScheme.onSecondary)
    val onTertiaryColor = composeColorToAndroid(MaterialTheme.colorScheme.onTertiary)



    Column {
        Column(
            modifier = Modifier
                .padding(12.dp)
        ) {
            TextGenerator(text = selectedValue?.let { "One Rep Max: ${it.toInt()} kg" } ?: "", MaterialTheme.colorScheme.onBackground, "body",true)

            TextGenerator(text = selectedDate?.let { "Date: ${it}"} ?: "", MaterialTheme.colorScheme.onBackground, "ultrasmall",false)

        }

        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),

            factory = { context ->
                LineChart(context).apply {

                    // ===============================
                    // ZÁKLADNÉ NASTAVENIA
                    // ===============================

                    setBackgroundColor(Color.TRANSPARENT)
                    setDrawGridBackground(false)

                    setTouchEnabled(true)
                    isDragEnabled = true
                    setPinchZoom(false)
                    setScaleEnabled(false)
                    isDoubleTapToZoomEnabled = false

                    animateX(800)

                    // ===============================
                    // DESCRIPTION
                    // ===============================

                    description.text = descriptionText
                    description.textSize = 12f
                    description.textColor = onBackgroundColor
                    description.isEnabled = true

                    // ===============================
                    // LEGENDA
                    // ===============================

                    legend.isEnabled = true
                    legend.textSize = 12f
                    legend.textColor = onBackgroundColor
                    legend.form =
                        com.github.mikephil.charting.components.Legend.LegendForm.LINE

                    // ===============================
                    // Y AXIS – ĽAVÁ
                    // ===============================

                    axisLeft.apply {
                        axisMinimum = 0f
                        granularity = 10f
                        textSize = 12f
                        textColor = onBackgroundColor
                        setDrawGridLines(false)
                        setDrawZeroLine(true)
                        zeroLineColor = onBackgroundColor
                    }

                    // ===============================
                    // Y AXIS – PRAVÁ
                    // ===============================

                    axisRight.isEnabled = false

                    // ===============================
                    // X AXIS – SPODNÁ (DÁTUMY)
                    // ===============================
                    xAxis.isEnabled = false


                    // ===============================
                    // LISTENER – POHYB PRSTOM
                    // ===============================

                    setOnChartValueSelectedListener(object :
                        OnChartValueSelectedListener {
                        override fun onValueSelected(e: Entry?, h: Highlight?) {
                            e ?: return
                            selectedValue = e.y
                            selectedDate =
                                LocalDate.ofEpochDay(e.x.toLong())
                        }

                        override fun onNothingSelected() {
                            selectedValue = null
                            selectedDate = null
                        }
                    })
                }
            },

            update = { chart ->

                // ===============================
                // PREVOD DÁT
                // ===============================

                val entries = data.map {
                    Entry(it.first, it.second)
                }

                // ===============================
                // DATASET
                // ===============================

                val dataSet = LineDataSet(entries, descriptionText).apply {

                    lineWidth = 3f
                    color = PrimaryColor
                    mode = LineDataSet.Mode.HORIZONTAL_BEZIER

                    setDrawCircles(true)
                    circleRadius = 6f
                    setCircleColor(PrimaryColor)
                    setDrawCircleHole(true)
                    circleHoleRadius = 3f
                    circleHoleColor = onPrimaryColor

                    setDrawValues(false)


                    highLightColor = onBackgroundColor
                    highlightLineWidth = 2f
                    setDrawHighlightIndicators(true)
                    setDrawHorizontalHighlightIndicator(false)
                }

                chart.data = LineData(dataSet)
                chart.invalidate()
            }
        )

        // ===============================
        // OVERLAY TEXT (ĽAVÝ HORNÝ ROH)
        // ===============================


    }
}

fun composeColorToAndroid(color: ComposeColor): Int {
    return color.toArgb()
}




