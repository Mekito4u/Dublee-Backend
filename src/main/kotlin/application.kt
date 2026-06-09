package com

import com.config.plugins.*
import com.data.database.configureDatabase
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    FirebaseInitializer.init()
    embeddedServer(
        Netty,
        port = 8080,
        host = "0.0.0.0"
    ) {
        configureAuthentication()
        configureDatabase()
        configureRouting()
        configureSerialization()
        configureHttp()
    }.start(wait = true)
}
