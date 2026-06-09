package com.infrastructure.security

import at.favre.lib.crypto.bcrypt.BCrypt

object PasswordHasher {
    private const val ROUNDS = 12

    fun hash(password: String): String =
        BCrypt.withDefaults().hashToString(ROUNDS, password.toCharArray())

    fun verify(password: String, hash: String): Boolean =
        BCrypt.verifyer().verify(password.toCharArray(), hash).verified
}