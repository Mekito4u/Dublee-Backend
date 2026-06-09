package com.data.impl

import com.data.database.tables.PairsTable
import com.data.database.tables.UsersTable
import com.data.utils.CodeGenerator
import com.domain.model.Pair
import com.domain.repository.PairRepository
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class PairRepositoryImpl : PairRepository {
    override suspend fun createPair(user1Id: Int): Pair = newSuspendedTransaction {
        var inviteCode: String
        do {
            inviteCode = CodeGenerator.generate()
        } while (
            PairsTable.selectAll().where { PairsTable.inviteCode eq inviteCode }.any()
        )

        val id = PairsTable.insertAndGetId {
            it[PairsTable.user1Id] = user1Id
            it[PairsTable.inviteCode] = inviteCode
        }
        Pair(
            id = id.value,
            user1Id = user1Id,
            inviteCode = inviteCode
        )
    }

    override suspend fun joinPair(userId: Int, inviteCode: String): Boolean = newSuspendedTransaction {
        val targetPair = PairsTable.selectAll().where { PairsTable.inviteCode eq inviteCode }
            .map { rowToPair(it) }
            .singleOrNull() ?: return@newSuspendedTransaction false

        if (targetPair.user2Id != null) return@newSuspendedTransaction false

        val currentUser = UsersTable.selectAll().where { UsersTable.id eq userId }.singleOrNull()
        val currentPairId = currentUser?.get(UsersTable.pairId)

        if (currentPairId != null) {
            val currentPair = PairsTable.selectAll().where { PairsTable.id eq currentPairId }
                .map { rowToPair(it) }
                .singleOrNull()
            if (currentPair?.user2Id == null) {
                PairsTable.deleteWhere { PairsTable.id eq currentPairId }
            } else {
                return@newSuspendedTransaction false
            }
        }

        val updated = PairsTable.update({ PairsTable.id eq targetPair.id }) {
            it[PairsTable.user2Id] = userId
        } > 0

        if (updated) {
            UsersTable.update({ UsersTable.id eq userId }) {
                it[UsersTable.pairId] = targetPair.id
            }
        }

        return@newSuspendedTransaction updated
    }

    override suspend fun findByInviteCode(inviteCode: String): Pair? = newSuspendedTransaction {
        PairsTable.selectAll().where { PairsTable.inviteCode eq inviteCode }
            .map { rowToPair(it) }
            .singleOrNull()
    }

    override suspend fun findById(pairId: Int): Pair? = newSuspendedTransaction {
        PairsTable.selectAll().where { PairsTable.id eq pairId }
            .map { rowToPair(it) }
            .singleOrNull()
    }

    override suspend fun leavePair(userId: Int, pairId: Int): Boolean = newSuspendedTransaction {
        val pair = PairsTable.selectAll().where { PairsTable.id eq pairId }
            .map { rowToPair(it) }
            .singleOrNull() ?: return@newSuspendedTransaction false

        if (pair.user1Id != userId && pair.user2Id != userId) {
            return@newSuspendedTransaction false
        }

        val updated = when {
            pair.user1Id == userId && pair.user2Id == null -> {
                PairsTable.deleteWhere { PairsTable.id eq pairId } > 0
            }

            pair.user1Id == userId && pair.user2Id != null -> {
                PairsTable.update({ PairsTable.id eq pairId }) {
                    it[PairsTable.user1Id] = pair.user2Id
                    it[PairsTable.user2Id] = null
                } > 0
            }

            pair.user2Id == userId -> {
                PairsTable.update({ PairsTable.id eq pairId }) {
                    it[PairsTable.user2Id] = null
                } > 0
            }

            else -> false
        }
        updated
    }

    private fun rowToPair(row: ResultRow): Pair = Pair(
        id = row[PairsTable.id].value,
        user1Id = row[PairsTable.user1Id],
        user2Id = row[PairsTable.user2Id],
        inviteCode = row[PairsTable.inviteCode]
    )
}