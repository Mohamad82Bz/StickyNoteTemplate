package org.sayandev.projectname.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Deferred
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.experimental.suspendedTransactionAsync
import org.jetbrains.exposed.sql.transactions.transaction
import org.sayandev.projectname.config.StorageConfig
import org.sayandev.projectname.config.storage
import org.sayandev.stickynote.bukkit.plugin
import org.sayandev.stickynote.bukkit.pluginDirectory
import org.sayandev.stickynote.core.coroutine.dispatcher.AsyncDispatcher

object Database {

    @JvmStatic
    val databaseDispatcher = AsyncDispatcher("${plugin.name.lowercase()}-${storage.method.name.lowercase()}-thread", storage.poolingSize)
    private val database: Database

    init {
        val config = HikariConfig().apply {
            jdbcUrl = when (storage.method) {
                StorageConfig.DatabaseMethod.SQLITE -> "jdbc:sqlite:${pluginDirectory.absolutePath}/storage"
                StorageConfig.DatabaseMethod.MYSQL,
                StorageConfig.DatabaseMethod.MARIADB -> "jdbc:${storage.method.name.lowercase()}://${storage.host}:${storage.port}/${storage.database}"
            }

            driverClassName = when (storage.method) {
                StorageConfig.DatabaseMethod.SQLITE -> "org.sqlite.JDBC"
                StorageConfig.DatabaseMethod.MYSQL -> "com.mysql.cj.jdbc.Driver"
                StorageConfig.DatabaseMethod.MARIADB -> "org.mariadb.jdbc.Driver"
            }

            username = storage.username
            password = storage.password
            maximumPoolSize = storage.poolingSize
        }
        database = Database.connect(HikariDataSource(config))
        TransactionManager.defaultDatabase = database

        transaction {
            //TODO: Create tables
        }
    }

    suspend fun <T> async(statement: suspend Transaction.() -> T): Deferred<T> {
        return suspendedTransactionAsync(
            databaseDispatcher,
            database,
            statement = statement
        )
    }

}