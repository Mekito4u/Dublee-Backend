package com.config.plugins

import com.infrastructure.security.JwtConfig
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*

fun Application.configureAuthentication() {
    install(Authentication) {
        jwt("auth-jwt") {
            verifier(JwtConfig.getVerifier())
            validate { credential ->
                val userId = credential.payload.getClaim("userId").asInt()
                val login = credential.payload.getClaim("login").asString()

                if (userId != null && login != null) {
                    JWTPrincipal(credential.payload)
                } else null
            }

            challenge { _, _ ->
                call.respond(HttpStatusCode.Unauthorized, "Invalid token")
            }
        }
    }
}