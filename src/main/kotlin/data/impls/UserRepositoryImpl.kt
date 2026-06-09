package com.data.impl

import com.data.database.tables.UsersTable
import com.domain.model.User
import com.domain.repository.UserRepository
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.update

class UserRepositoryImpl : UserRepository {
    override suspend fun updateUser(userId: Int, iconId: Int?, colorId: Int?, fcmToken: String?): Boolean =
        newSuspendedTransaction {
            UsersTable.update({ UsersTable.id eq userId }) { update ->
                iconId?.let { update[UsersTable.iconId] = it }
                colorId?.let { update[UsersTable.colorId] = it }
                fcmToken?.let { update[UsersTable.fcmToken] = it }
            } > 0
        }

    override suspend fun createUser(login: String, passwordHash: String): User = newSuspendedTransaction {
        val id = UsersTable.insertAndGetId {
            it[UsersTable.login] = login
            it[UsersTable.passwordHash] = passwordHash
        }
        User(
            id = id.value,
            login = login,
            passwordHash = passwordHash
        )
    }

    override suspend fun findByLogin(login: String): User? = newSuspendedTransaction {
        UsersTable.selectAll().where { UsersTable.login eq login }
            .map { rowToUser(it) }
            .singleOrNull()
    }

    override suspend fun findById(id: Int): User? = newSuspendedTransaction {
        UsersTable.selectAll().where { UsersTable.id eq id }
            .map { rowToUser(it) }
            .singleOrNull()
    }

    override suspend fun updatePairId(userId: Int, pairId: Int?): Boolean = newSuspendedTransaction {
        UsersTable.update({ UsersTable.id eq userId }) {
            it[UsersTable.pairId] = pairId
        } > 0
    }

    override suspend fun updateFcmToken(userId: Int, token: String): Boolean = newSuspendedTransaction {
        UsersTable.update({ UsersTable.id eq userId }) {
            it[UsersTable.fcmToken] = token
        } > 0
    }

    private fun rowToUser(row: ResultRow): User = User(
        id = row[UsersTable.id].value,
        login = row[UsersTable.login],
        passwordHash = row[UsersTable.passwordHash],
        pairId = row[UsersTable.pairId],
        iconId = row[UsersTable.iconId],
        colorId = row[UsersTable.colorId],
        fcmToken = row[UsersTable.fcmToken]
    )

}