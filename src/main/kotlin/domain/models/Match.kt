package com.domain.model

import java.time.LocalDateTime

data class Match(
    val id: Int,
    val pairId: Int,
    val optionId: Int,
    val matchedAt: Long
)
