package com.example.bakalarka.data

import kotlinx.serialization.Serializable

@Serializable
data class Set(
    val id: Int,
    val workout_exercise_id: Int,
    val set_number: Int,
    val reps: Int,
    val weight: String,
    val created_at: String
)