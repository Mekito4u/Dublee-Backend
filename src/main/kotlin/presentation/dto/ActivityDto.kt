package com.presentation.dto

import kotlinx.serialization.Serializable

@Serializable
data class LikeRequest(
    val optionId: Int
)

@Serializable
data class LikeResponse(
    val id: Int,
    val optionId: Int,
    val createdAt: Long
)

@Serializable
data class MatchResponse(
    val id: Int,
    val optionId: Int,
    val matchedAt: Long
)