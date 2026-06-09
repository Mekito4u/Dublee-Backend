package com.data.impls

import com.data.database.tables.LikesTable
import com.domain.model.Like
import com.domain.repositories.LikeRepository
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class LikeRepositoryImpl : LikeRepository {
    override suspend fun addLike(userId: Int, optionId: Int): Like = newSuspendedTransaction {
        val now = System.currentTimeMillis()
        val id = LikesTable.insertAndGetId {
            it[LikesTable.userId] = userId
            it[LikesTable.optionId] = optionId
            it[LikesTable.createdAt] = now
        }
        Like(
            id = id.value,
            userId = userId,
            optionId = optionId,
            createdAt = now
        )
    }

    override suspend fun getLikesByUser(userId: Int): List<Like> = newSuspendedTransaction {
        LikesTable.selectAll().where { LikesTable.userId eq userId }
            .map { rowToLike(it) }
    }

    override suspend fun getLikeById(likeId: Int): Like? = newSuspendedTransaction {
        LikesTable.selectAll().where { LikesTable.id eq likeId }
            .map { rowToLike(it) }
            .singleOrNull()
    }

    override suspend fun deleteLike(likeId: Int): Boolean = newSuspendedTransaction {
        LikesTable.deleteWhere { LikesTable.id eq likeId } > 0
    }

    override suspend fun deleteLikesByUser(userId: Int): Boolean = newSuspendedTransaction {
        LikesTable.deleteWhere { LikesTable.userId eq userId } > 0
    }

    private fun rowToLike(row: ResultRow): Like = Like(
        id = row[LikesTable.id].value,
        userId = row[LikesTable.userId],
        optionId = row[LikesTable.optionId],
        createdAt = row[LikesTable.createdAt]
    )
}