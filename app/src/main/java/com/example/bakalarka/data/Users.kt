package com.example.bakalarka.data

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int,
    val username: String,
    val password_hash: String,
    val email: String,
    val created_at: String
)