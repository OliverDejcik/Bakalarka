package com.example.bakalarka.other_classes

import android.content.res.Resources
import androidx.lifecycle.ViewModel

// Data class to represent an exercise with a name and details.
data class Exercise(val name: String, val sets: String, val training: String)
data class Training(val name: String,val exerciseNum: Int)


class AppViewModel : ViewModel() {


    //Add training screen

    var exercises = mutableListOf<Exercise>()
        private set

    var trainings = mutableListOf<Training>()
        private set

    fun addExercise(name: String, sets: String,training: String) {
        exercises.add(Exercise(name, sets.toString(),training))
    }

    fun addTraining(name: String, exerciseNum: Int){
        trainings.add(Training(name, exerciseNum.toInt()))
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



    // training screen veci

    fun getTrainings(): List<Training> {
        return trainings
    }

    fun getTrainingExerciseNum(name: String): Int? {
        return trainings.find{it.name == name}?.exerciseNum
    }



}
