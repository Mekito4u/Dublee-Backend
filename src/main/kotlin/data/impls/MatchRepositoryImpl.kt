package com.data.impls

import com.data.database.tables.MatchesTable
import com.domain.model.Match
import com.domain.repositories.MatchRepository
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.time.LocalDateTime

class MatchRepositoryImpl : MatchRepository {
    override suspend fun addMatch(pairId: Int, optionId: Int): Match = newSuspendedTransaction {
        val now = System.currentTimeMillis()
        val id = MatchesTable.insertAndGetId {
            it[MatchesTable.pairId] = pairId
            it[MatchesTable.optionId] = optionId
            it[MatchesTable.matchedAt] = now
        }
        Match(
            id = id.value,
            pairId = pairId,
            optionId = optionId,
            matchedAt = now
        )
    }

    override suspend fun getMatchesByPair(pairId: Int): List<Match> = newSuspendedTransaction {
        MatchesTable.selectAll().where { MatchesTable.pairId eq pairId }
            .orderBy(MatchesTable.matchedAt to SortOrder.DESC)
            .map { rowToMatch(it) }
    }

    private fun rowToMatch(row: ResultRow): Match = Match(
        id = row[MatchesTable.id].value,
        pairId = row[MatchesTable.pairId],
        optionId = row[MatchesTable.optionId],
        matchedAt = row[MatchesTable.matchedAt]
    )
}