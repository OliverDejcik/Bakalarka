package com.example.bakalarka.data

import kotlinx.serialization.Serializable

@Serializable
data class WorkoutExercise(
    val id: Int,
    val workout_id: Int,
    val exercise_id: Int,
    val order_index: Int,
    val created_at: String
)