package com.example.bakalarka.data

import kotlinx.serialization.Serializable

@Serializable
data class Training(
    val id: Int,
    val user_id: Int,
    val name: String,
    val number_of_exercises: Int,
    val created_at: String
)