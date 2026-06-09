package com.data.database.tables

import org.jetbrains.exposed.dao.id.IntIdTable

object LikesTable : IntIdTable("likes") {
    val userId = integer("user_id")
    val optionId = integer("option_id")
    val createdAt = long("created_at")
}