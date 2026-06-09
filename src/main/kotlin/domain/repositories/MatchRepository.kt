package com.domain.repositories

import com.domain.model.Match

interface MatchRepository {
    suspend fun addMatch(pairId: Int, optionId: Int): Match
    suspend fun getMatchesByPair(pairId: Int): List<Match>
}