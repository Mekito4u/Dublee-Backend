package com.domain.repositories

import com.domain.model.Like

interface LikeRepository {
    suspend fun addLike(userId: Int, optionId: Int): Like
    suspend fun getLikesByUser(userId: Int): List<Like>
    suspend fun getLikeById(likeId: Int): Like?
    suspend fun deleteLike(likeId: Int): Boolean
    suspend fun deleteLikesByUser(userId: Int): Boolean
}