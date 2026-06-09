package com.presentation.controllers

import com.domain.repository.PairRepository
import com.domain.repository.UserRepository
import com.presentation.controllers.utils.requireUserId
import com.presentation.dto.ErrorResponse
import com.presentation.dto.UpdateUserRequest
import com.presentation.dto.UserResponse
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.userController(
    userRepository: UserRepository,
    pairRepository: PairRepository
) {
    route("/api/users") {
        authenticate("auth-jwt") {
            get("/me") {
                val userId = call.requireUserId()

                val user = userRepository.findById(userId) ?: return@get call.respond(
                    HttpStatusCode.NotFound,
                    ErrorResponse("User not found")
                )

                call.respond(
                    UserResponse(
                        id = user.id,
                        login = user.login,
                        pairId = user.pairId,
                        iconId = user.iconId,
                        colorId = user.colorId
                    )
                )
            }
            patch("/me") {
                val userId = call.requireUserId()
                val request = call.receive<UpdateUserRequest>()
                val updated = userRepository.updateUser(
                    userId = userId,
                    iconId = request.iconId,
                    colorId = request.colorId,
                    fcmToken = request.fcmToken
                )
                if (!updated) {
                    return@patch call.respond(
                        HttpStatusCode.InternalServerError,
                        ErrorResponse("Update failed")
                    )
                }
                val user = userRepository.findById(userId) ?: return@patch call.respond(
                    HttpStatusCode.NotFound,
                    ErrorResponse("User not found after update")
                )
                call.respond(
                    UserResponse(
                        id = user.id,
                        login = user.login,
                        pairId = user.pairId,
                        iconId = user.iconId,
                        colorId = user.colorId
                    )
                )
            }
            get("/partner") {
                val userId = call.requireUserId()
                val user = userRepository.findById(userId) ?: return@get call.respond(
                    HttpStatusCode.NotFound,
                    ErrorResponse("User not found")
                )
                val pairId = user.pairId ?: return@get call.respond(
                    HttpStatusCode.BadRequest,
                    ErrorResponse("User not in a pair")
                )
                val pair = pairRepository.findById(pairId) ?: return@get call.respond(
                    HttpStatusCode.NotFound,
                    ErrorResponse("Pair not found")
                )
                val partnerId = when {
                    pair.user1Id == userId -> pair.user2Id
                    pair.user2Id == userId -> pair.user1Id
                    else -> null
                }
                if (partnerId == null) {
                    return@get call.respond(
                        HttpStatusCode.BadRequest,
                        ErrorResponse("Partner not found (pair incomplete)")
                    )
                }
                val partner = userRepository.findById(partnerId) ?: return@get call.respond(
                    HttpStatusCode.NotFound,
                    ErrorResponse("Partner user not found")
                )
                call.respond(
                    UserResponse(
                        id = partner.id,
                        login = partner.login,
                        pairId = partner.pairId,
                        iconId = partner.iconId,
                        colorId = partner.colorId
                    )
                )
            }
        }
    }
}