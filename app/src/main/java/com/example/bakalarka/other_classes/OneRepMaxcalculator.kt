package com.example.bakalarka.other_classes

import com.example.bakalarka.data.WorkoutExercise


class OneRepMaxcalculator {

    val viewModel = AppViewModel()

    // 1RM = váha × (1 + opakovania/30)

    fun Get1RMFun(weight: Float, reps: Int): Float {
        val oneRepMax = weight * (1 + reps.toFloat() / 30)
        return oneRepMax
    }

    fun constructDataForGraph(list: List<WorkoutExercise>){

    }



}
