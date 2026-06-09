package com.data.services

import com.domain.model.Like
import com.domain.model.Match
import com.domain.repositories.LikeRepository
import com.domain.repositories.MatchRepository
import com.domain.repository.PairRepository
import com.domain.repository.UserRepository

class ActivityService(
    private val likeRepository: LikeRepository,
    private val matchRepository: MatchRepository,
    private val pairRepository: PairRepository,
    private val userRepository: UserRepository,
    private val notificationService: NotificationService
) {
    suspend fun processLike(userId: Int, optionId: Int): Match? {
        val like = likeRepository.addLike(userId, optionId)

        val user = userRepository.findById(userId) ?: return null
        val pairId = user.pairId ?: return null

        val pair = pairRepository.findById(pairId) ?: return null
        val partnerId = when {
            pair.user1Id == userId -> pair.user2Id
            pair.user2Id == userId -> pair.user1Id
            else -> null
        } ?: return null

        val partnerLikes = likeRepository.getLikesByUser(partnerId)
        val matchingLike = partnerLikes.find { it.optionId == optionId }

        if (matchingLike != null) {
            val match = matchRepository.addMatch(pairId, optionId)
            likeRepository.deleteLike(like.id)
            likeRepository.deleteLike(matchingLike.id)

            // Отправить push
            val user1 = userRepository.findById(pair.user1Id)
            val user2 = pair.user2Id?.let { userRepository.findById(it) }
            user1?.fcmToken?.let {
                notificationService.sendPush(it, "Dublee", "Мэтч на опции $optionId!")
            }
            user2?.fcmToken?.let {
                notificationService.sendPush(it, "Dublee", "Мэтч на опции $optionId!")
            }

            return match
        }
        return null
    }

    suspend fun getUserLikes(userId: Int): List<Like> {
        return likeRepository.getLikesByUser(userId)
    }

    suspend fun getUserMatches(userId: Int): List<Match> {
        val user = userRepository.findById(userId) ?: return emptyList()
        val pairId = user.pairId ?: return emptyList()
        return matchRepository.getMatchesByPair(pairId)
    }

    suspend fun removeLike(userId: Int, likeId: Int): Boolean {
        val like = likeRepository.getLikeById(likeId) ?: return false
        if (like.userId != userId) return false
        return likeRepository.deleteLike(likeId)
    }
}