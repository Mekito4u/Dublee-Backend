package com.infrastructure.security

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import java.util.*

object JwtConfig {
    private const val SECRET = "dublee-super-secret-key-2025-2026"
    private const val ISSUER = "dublee-app"
    private const val AUDIENCE = "dublee-users"
    private const val EXPIRES_MS = 365L * 24 * 60 * 60 * 1000 // 1 год

    private val algorithm = Algorithm.HMAC256(SECRET)
    private val verifier = JWT.require(algorithm)
        .withIssuer(ISSUER)
        .withAudience(AUDIENCE)
        .build()

    fun generateToken(userId: Int, login: String): String =
        JWT.create()
            .withIssuer(ISSUER)
            .withAudience(AUDIENCE)
            .withClaim("userId", userId)
            .withClaim("login", login)
            .withIssuedAt(Date())
            .withExpiresAt(Date(System.currentTimeMillis() + EXPIRES_MS))
            .sign(algorithm)

    fun getVerifier(): JWTVerifier = verifier
}