
plugins {
    alias(libs.plugins.kotlin.jvm)           // Kotlin 2.0+
    alias(ktorLibs.plugins.ktor)             // Ktor 3.1.0
    alias(libs.plugins.kotlin.serialization) // для @Serializable
}

group = "com"
version = "1.0.0-SNAPSHOT"

application {
    mainClass = "io.ktor.server.netty.EngineMain"
}

kotlin {
    jvmToolchain(21)
}

dependencies {
    // JSON сериализация
    implementation(ktorLibs.serialization.kotlinx.json)

    // JWT аутентификация
    implementation(ktorLibs.server.auth)
    implementation(ktorLibs.server.auth.jwt)

    // ⚠Логи запросов в консоль
    implementation(ktorLibs.server.callLogging)

    // Авто-парсинг JSON из запросов
    implementation(ktorLibs.server.contentNegotiation)

    // Ядро Ktor
    implementation(ktorLibs.server.core)

    // CORS — полезно (Postman/браузер)
    implementation(ktorLibs.server.cors)

    // Netty движок
    implementation(ktorLibs.server.netty)

    // Логирование ошибок
    implementation(libs.logback.classic)

    // База данных
    implementation("org.jetbrains.exposed:exposed-core:0.56.0")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.56.0")
    implementation("org.jetbrains.exposed:exposed-java-time:0.56.0")
    implementation("org.postgresql:postgresql:42.7.4")
    implementation("com.zaxxer:HikariCP:6.0.0")

    // Хэширование паролей
    implementation("at.favre.lib:bcrypt:0.10.2")

    // JWT генерация
    implementation("com.auth0:java-jwt:4.5.0")

    // FCM уведомления
    implementation("com.google.firebase:firebase-admin:9.3.0")

    // Обработка ошибок
    implementation("io.ktor:ktor-server-status-pages:3.1.0")

    // FCM уведомления
    implementation("com.google.firebase:firebase-admin:9.3.0")
}

