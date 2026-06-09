package com.data.database.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime

object MatchesTable : IntIdTable("matches") {
    val pairId = integer("pair_id")
    val optionId = integer("option_id")
    val matchedAt = long("matched_at")
}