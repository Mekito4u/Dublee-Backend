package com.presentation.controllers

import com.domain.repository.PairRepository
import com.domain.repository.UserRepository
import com.presentation.controllers.utils.requireUserId
import com.presentation.dto.ErrorResponse
import com.presentation.dto.JoinPairRequest
import com.presentation.dto.PairResponse
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.pairController(
    pairRepository: PairRepository,
    userRepository: UserRepository,
) {
    route("/api/pairs") {
        authenticate("auth-jwt") {
            get("/code") {
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

                call.respond(
                    HttpStatusCode.OK,
                    mapOf("inviteCode" to pair.inviteCode)
                )
            }
            post("/create") {
                val userId = call.requireUserId()

                val existingUser = userRepository.findById(userId)
                if (existingUser?.pairId != null) {
                    return@post call.respond(
                        HttpStatusCode.BadRequest,
                        ErrorResponse("User already in a pair")
                    )
                }

                val pair = pairRepository.createPair(userId)
                userRepository.updatePairId(userId, pair.id)

                call.respond(
                    HttpStatusCode.Created,
                    PairResponse(
                        id = pair.id,
                        inviteCode = pair.inviteCode,
                        user1Id = pair.user1Id,
                        user2Id = pair.user2Id
                    )
                )
            }
            post("/join") {
                val userId = call.requireUserId()
                val request = call.receive<JoinPairRequest>()
                val success = pairRepository.joinPair(userId, request.inviteCode)
                if (!success) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        ErrorResponse("Cannot join pair"))
                } else {
                    call.respond(
                        HttpStatusCode.OK,
                        mapOf("status" to "joined"))
                }
            }
            delete("/leave") {
                val userId = call.requireUserId()

                val user = userRepository.findById(userId) ?: return@delete call.respond(
                    HttpStatusCode.NotFound,
                    ErrorResponse("User not found")
                )
                val pairId = user.pairId ?: return@delete call.respond(
                    HttpStatusCode.BadRequest,
                    ErrorResponse("User not in a pair")
                )

                val success = pairRepository.leavePair(userId, pairId)
                if (success) {
                    userRepository.updatePairId(userId, null)
                    call.respond(
                        HttpStatusCode.OK,
                        mapOf("status" to "left")
                    )
                } else {
                    call.respond(
                        HttpStatusCode.InternalServerError,
                        ErrorResponse("Failed to leave pair")
                    )
                }

            }
        }
    }
}