package com.presentation.controllers.utils

import com.presentation.dto.ErrorResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*

class UnauthorizedException : RuntimeException()

suspend fun ApplicationCall.requireUserId(): Int {
    val principal = principal<JWTPrincipal>()
    val userId = principal?.payload?.getClaim("userId")?.asInt()
    if (userId == null) {
        respond(HttpStatusCode.Unauthorized, ErrorResponse("Unauthorized"))
        throw UnauthorizedException()
    }
    return userId
}