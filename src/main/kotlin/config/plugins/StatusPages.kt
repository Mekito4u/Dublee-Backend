package com.infrastructure.plugins

import com.presentation.controllers.utils.UnauthorizedException
import com.presentation.dto.ErrorResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

fun Application.configureStatusPages() {
    install(StatusPages) {
        exception<UnauthorizedException> { call, _ ->
            if (!call.response.isCommitted) {
                call.respond(
                    HttpStatusCode.Unauthorized,
                    ErrorResponse("Unauthorized")
                )
            }
        }
    }
}