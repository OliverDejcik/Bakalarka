package com.example.bakalarka.data

import kotlinx.serialization.Serializable

@Serializable
data class TrainingInsert(
    val user_id: Int,
    val name: String,
    val number_of_exercises: Int
)
