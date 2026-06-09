package com.presentation.controllers

import com.domain.repository.UserRepository
import com.infrastructure.security.JwtConfig
import com.infrastructure.security.PasswordHasher
import com.presentation.dto.*
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.authController(userRepository: UserRepository) {
    route("/api/auth") {
        post("/register") {
            val request = call.receive<RegisterRequest>()
            val existing = userRepository.findByLogin(request.login)
            if (existing != null) {
                call.respond(
                    HttpStatusCode.Conflict,
                    ErrorResponse("Login already exists")
                )
                return@post
            }

            val passwordHash = PasswordHasher.hash(request.password)
            val user = userRepository.createUser(request.login, passwordHash)

            val token = JwtConfig.generateToken(user.id, user.login)

            call.respond(
                HttpStatusCode.Created,
                LoginResponse(
                    token = token,
                    user = UserResponse(
                        id = user.id,
                        login = user.login,
                        pairId = user.pairId,
                        iconId = user.iconId,
                        colorId = user.colorId
                    )
                )
            )
        }
        post("/login") {
            val request = call.receive<LoginRequest>()
            val user = userRepository.findByLogin(request.login)
            if (user == null || !PasswordHasher.verify(request.password, user.passwordHash)) {
                call.respond(HttpStatusCode.Unauthorized, ErrorResponse("Invalid credentials"))
                return@post
            }

            val token = JwtConfig.generateToken(user.id, user.login)
            call.respond(
                HttpStatusCode.OK,
                LoginResponse(
                    token = token,
                    user = UserResponse(
                        id = user.id,
                        login = user.login,
                        pairId = user.pairId,
                        iconId = user.iconId,
                        colorId = user.colorId
                    )
                )
            )
        }
    }
}