package com.example.bakalarka.data

import kotlinx.serialization.Serializable


@Serializable
data class WorkoutInsert(
    val training_id: Int,
    val user_id: Int,
)