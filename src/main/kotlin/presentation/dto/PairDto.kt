package com.presentation.dto

import kotlinx.serialization.Serializable

@Serializable
data class JoinPairRequest(
    val inviteCode: String
)

@Serializable
data class PairResponse(
    val id: Int,
    val inviteCode: String,
    val user1Id: Int,
    val user2Id: Int?
)