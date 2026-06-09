package com.config.di

import com.data.impl.PairRepositoryImpl
import com.data.impl.UserRepositoryImpl
import com.data.impls.LikeRepositoryImpl
import com.data.impls.MatchRepositoryImpl
import com.data.services.ActivityService
import com.data.services.NotificationService
import com.domain.repositories.LikeRepository
import com.domain.repositories.MatchRepository
import com.domain.repository.PairRepository
import com.domain.repository.UserRepository

class AppModule {
    val userRepository: UserRepository = UserRepositoryImpl()
    val pairRepository: PairRepository = PairRepositoryImpl()
    val likeRepository: LikeRepository = LikeRepositoryImpl()
    val matchRepository: MatchRepository = MatchRepositoryImpl()
    val notificationService = NotificationService()

    val activityService: ActivityService = ActivityService(
        likeRepository = likeRepository,
        matchRepository = matchRepository,
        pairRepository = pairRepository,
        userRepository = userRepository,
        notificationService = notificationService,
    )
}