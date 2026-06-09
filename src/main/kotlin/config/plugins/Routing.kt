package com.config.plugins

import com.config.di.AppModule
import com.presentation.controllers.activityController
import com.presentation.controllers.authController
import com.presentation.controllers.pairController
import com.presentation.controllers.userController
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    val appModule = AppModule()

    routing {
        authController(appModule.userRepository)
        userController(appModule.userRepository, appModule.pairRepository)
        pairController(appModule.pairRepository, appModule.userRepository)
        activityController(appModule.activityService)
    }
}