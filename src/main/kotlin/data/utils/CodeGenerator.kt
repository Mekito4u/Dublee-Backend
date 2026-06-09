package com.data.utils

object CodeGenerator {
    private val CHARS = ('A'..'Z') + ('0'..'9')
    private const val LENGTH = 6

    fun generate(): String {
        return (1..LENGTH)
            .map { CHARS.random() }
            .joinToString("")
    }
}