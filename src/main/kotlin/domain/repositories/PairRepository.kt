package com.domain.repository

import com.domain.model.Pair

interface PairRepository {
    suspend fun createPair(user1Id: Int): Pair
    suspend fun joinPair(user2Id: Int, inviteCode: String): Boolean
    suspend fun findByInviteCode(inviteCode: String): Pair?
    suspend fun findById(pairId: Int): Pair?
    suspend fun leavePair(userId: Int, pairId: Int): Boolean
}