package com.domain.repository

import com.domain.model.User

interface UserRepository {
    suspend fun createUser(login: String, passwordHash: String): User
    suspend fun updateUser(userId: Int, iconId: Int?, colorId: Int?, fcmToken: String?): Boolean
    suspend fun findByLogin(login: String): User?
    suspend fun findById(id: Int): User?
    suspend fun updatePairId(userId: Int, pairId: Int?): Boolean
    suspend fun updateFcmToken(userId: Int, token: String): Boolean
}