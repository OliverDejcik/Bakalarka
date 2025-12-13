package com.example.bakalarka.data

import kotlinx.serialization.Serializable

@Serializable
data class Workout(
    val id: Int,
    val user_id: Int,
    val training_id: String,
    val created_at: String
)