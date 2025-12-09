package com.example.bakalarka.other_classes

import android.content.res.Resources
import androidx.lifecycle.ViewModel

// Data class to represent an exercise with a name and details.
data class Exercise(val name: String, val details: String)

class AppViewModel : ViewModel() {


    //Add training screen
    var trainingName = ""
        private set

    var trainingNumber: Int = 0
        private set

    var exercises = mutableListOf<Exercise>()
        private set

    fun setTrainingName(name: String) {
        trainingName = name
    }

    fun setTrainingNumber(number: String) {
        trainingNumber = number.toInt()
    }

    fun addExercise(name: String, sets: String) {
        exercises.add(Exercise(name, sets.toString()))
    }



    // velkosti obrazovky
    var screenWidth = 0
        private set

    var screenHeight = 0
        private set

    var density = 1f
        private set

    fun setScreenInfo(width: Int, height: Int, density: Float) {
        screenWidth = width
        screenHeight = height
        this.density = density
    }
    fun getScreenHeightPX(): Int {
        return Resources.getSystem().displayMetrics.heightPixels
    }


    fun pxToDp(px: Int): Float = px / density

}
