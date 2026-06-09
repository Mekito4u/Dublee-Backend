package com.domain.model

data class Like(
    val id: Int,
    val userId: Int,
    val optionId: Int,
    val createdAt: Long
)
