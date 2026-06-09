package com.presentation.controllers

import com.data.services.ActivityService
import com.presentation.controllers.utils.requireUserId
import com.presentation.dto.ErrorResponse
import com.presentation.dto.LikeRequest
import com.presentation.dto.LikeResponse
import com.presentation.dto.MatchResponse
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.activityController(
    activityService: ActivityService,
) {
    route("/api/activity") {
        authenticate("auth-jwt") {
            get("/likes") {
                val userId = call.requireUserId()
                val likes = activityService.getUserLikes(userId)
                val response = likes.map {
                    LikeResponse(
                        it.id,
                        it.optionId,
                        it.createdAt
                    )
                }

                call.respond(response)
            }

            get("/matches") {
                val userId = call.requireUserId()
                val matches = activityService.getUserMatches(userId)
                val response = matches.map {
                    MatchResponse(
                        it.id,
                        it.optionId,
                        it.matchedAt
                    )
                }
                call.respond(response)
            }

            post("/likes/add") {
                val userId = call.requireUserId()
                val request = call.receive<LikeRequest>()
                val match = activityService.processLike(userId, request.optionId)
                if (match != null) {
                    call.respond(
                        HttpStatusCode.OK,
                        MatchResponse(
                            match.id,
                            match.optionId,
                            match.matchedAt
                        )
                    )
                } else {
                    call.respond(
                        HttpStatusCode.OK,
                        mapOf("status" to "liked")
                    )
                }
            }

            delete("/likes/{likeId}") {
                val userId = call.requireUserId()
                val likeId = call.parameters["likeId"]?.toIntOrNull()
                    ?: return@delete call.respond(
                        HttpStatusCode.BadRequest,
                        ErrorResponse("Invalid like ID")
                    )
                val deleted = activityService.removeLike(userId, likeId)
                if (!deleted) {
                    call.respond(HttpStatusCode.Forbidden, ErrorResponse("Cannot delete this like"))
                } else {
                    call.respond(HttpStatusCode.OK, mapOf("status" to "deleted"))
                }
            }
        }
    }
}