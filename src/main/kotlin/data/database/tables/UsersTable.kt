package com.data.database.tables

import org.jetbrains.exposed.dao.id.IntIdTable

object UsersTable : IntIdTable("users") {
    val login = varchar("login", 255).uniqueIndex()
    val passwordHash = varchar("password_hash", 255)
    val pairId = integer("pair_id").nullable().default(null)
    val iconId = integer("icon_id").default(0)
    val colorId = integer("color_id").default(0)
    val fcmToken = text("fcm_token").nullable().default(null)
}