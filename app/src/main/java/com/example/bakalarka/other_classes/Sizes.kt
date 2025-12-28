package com.example.bakalarka.other_classes

import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.example.bakalarka.other_classes.AppViewModel


object ElementSizeProvider {

    val viewModel = AppViewModel()


    fun getSize(name: String): TextUnit {

        val height = viewModel.pxToDp(viewModel.getScreenHeightPX())


        val screenSize = when (height.toInt()) {
            in 0 .. 650 -> "Small phone"
            in 651..800 -> "Medium phone"
            in 801..900 -> "Large phone"
            in 901..1200 -> "Small tablet"
            else -> "Large tablet"
        }


        val scale = when (screenSize.lowercase()) {
            "small phone" -> 0.85f
            "medium phone" -> 1f
            "large phone" -> 1.15f
            "small tablet" -> 1.3f

            else -> 1.45f
        }




        val baseSize = when (name.lowercase()) {
            "title" -> 26.sp
            "subtitle" -> 20.sp
            "body" -> 16.sp
            "small" -> 14.sp
            "button" -> 16.sp
            "label" -> 12.sp
            "ultrasmall" -> 10.sp

            else -> 16.sp // default
        }

        return (baseSize.value * scale).sp
    }

    fun getScale(): Float {
        val height = viewModel.pxToDp(viewModel.getScreenHeightPX())

        val screenSize = when (height.toInt()) {
            in 0 .. 650 -> "Small phone"
            in 651..800 -> "Medium phone"
            in 801..900 -> "Large phone"
            in 901..1200 -> "Small tablet"
            else -> "Large tablet"
        }


        val scale = when (screenSize.lowercase()) {
            "small phone" -> 0.85f
            "medium phone" -> 1f
            "large phone" -> 1.15f
            "small tablet" -> 1.3f

            else -> 1.45f
        }

        return scale
    }
}
