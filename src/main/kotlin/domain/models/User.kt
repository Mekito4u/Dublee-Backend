package com.domain.model

data class User(
    val id: Int,
    val login: String,
    val passwordHash: String,
    val pairId: Int? = null,
    val iconId: Int = 0,
    val colorId: Int = 0,
    val fcmToken: String? = null
)