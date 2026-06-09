package com.domain.model

data class Pair(
    val id: Int,
    val user1Id: Int,
    val user2Id: Int? = null,
    val inviteCode: String
)
