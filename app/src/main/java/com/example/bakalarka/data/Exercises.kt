package com.example.bakalarka.data

import kotlinx.serialization.Serializable

@Serializable
data class Exercise(
    val id: Int,
    val training_id: Int,
    val name: String,
    val sets_count: Int,
    val order_index: Int,
    val created_at: String
)