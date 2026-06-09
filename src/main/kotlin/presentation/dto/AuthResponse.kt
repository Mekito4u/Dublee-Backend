package com.presentation.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val token: String,
    val user: UserResponse
)

@Serializable
data class ErrorResponse(
    val message: String
)