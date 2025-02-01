package org.sayandev.projectname.config

import org.sayandev.projectname.Platform
import org.sayandev.stickynote.bukkit.plugin
import org.sayandev.stickynote.bukkit.pluginDirectory
import org.sayandev.stickynote.core.configuration.Config
import org.spongepowered.configurate.objectmapping.ConfigSerializable
import org.spongepowered.configurate.objectmapping.meta.Setting
import java.io.File
import kotlin.text.get

@ConfigSerializable
data class StorageConfig(
    val method: DatabaseMethod = DatabaseMethod.SQLITE,
    val host: String = "localhost",
    val port: Int = 3306,
    val database: String = plugin.name.lowercase(),
    val username: String = "minecraft",
    val password: String = "",
    @Setting("use-ssl") val useSSL: Boolean = false,
    val poolingSize: Int = 5,
): Config(pluginDirectory, FILE_NAME) {

    enum class DatabaseMethod {
        SQLITE,
        MYSQL,
        MARIADB
    }

    companion object {
        private const val FILE_NAME = "storage.yml"
        val storageConfigFile = File(Platform.get().pluginDirectory, FILE_NAME)
        val config = fromConfig() ?: defaultConfig()

        fun get(): StorageConfig {
            return config
        }

        fun defaultConfig(): StorageConfig {
            return StorageConfig().apply { save() }
        }

        fun fromConfig(): StorageConfig? {
            return fromConfig<StorageConfig>(storageConfigFile)
        }
    }
}