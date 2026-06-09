package com.data.database.tables

import org.jetbrains.exposed.dao.id.IntIdTable

object PairsTable : IntIdTable("pairs") {
    val user1Id = integer("user1_id")
    val user2Id = integer("user2_id").nullable().default(null)
    val inviteCode = varchar("invite_code", 6).uniqueIndex()
}