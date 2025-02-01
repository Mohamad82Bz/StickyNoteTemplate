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
import org.sayandev.stickynote.bukkit.plugin
import org.sayandev.stickynote.bukkit.pluginDirectory
import org.sayandev.stickynote.core.coroutine.dispatcher.AsyncDispatcher

object Database {

    @JvmStatic
    val databaseDispatcher = AsyncDispatcher("${plugin.name.lowercase()}-${StorageConfig.get().method.name.lowercase()}-thread", StorageConfig.get().poolingSize)
    private val database: Database

    init {
        val config = HikariConfig().apply {
            jdbcUrl = when (StorageConfig.get().method) {
                StorageConfig.DatabaseMethod.SQLITE -> "jdbc:sqlite:${pluginDirectory.absolutePath}/storage"
                StorageConfig.DatabaseMethod.MYSQL,
                StorageConfig.DatabaseMethod.MARIADB -> "jdbc:${StorageConfig.get().method.name.lowercase()}://${StorageConfig.get().host}:${StorageConfig.get().port}/${StorageConfig.get().database}"
            }

            driverClassName = when (StorageConfig.get().method) {
                StorageConfig.DatabaseMethod.SQLITE -> "org.sqlite.JDBC"
                StorageConfig.DatabaseMethod.MYSQL -> "com.mysql.cj.jdbc.Driver"
                StorageConfig.DatabaseMethod.MARIADB -> "org.mariadb.jdbc.Driver"
            }

            username = StorageConfig.get().username
            password = StorageConfig.get().password
            maximumPoolSize = StorageConfig.get().poolingSize
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