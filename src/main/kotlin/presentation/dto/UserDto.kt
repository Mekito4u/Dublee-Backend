package com.presentation.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val id: Int,
    val login: String,
    val pairId: Int?,
    val iconId: Int,
    val colorId: Int
)

@Serializable
data class UpdateUserRequest(
    val iconId: Int? = null,
    val colorId: Int? = null,
    val fcmToken: String? = null
)