package org.sayandev.projectname.config

import org.sayandev.projectname.Platform
import org.sayandev.stickynote.core.configuration.Config
import org.spongepowered.configurate.objectmapping.ConfigSerializable
import java.io.File

@ConfigSerializable
data class LanguageConfig(
    val general: General = General()
): Config(Platform.get().pluginDirectory, FILE_NAME) {

    @ConfigSerializable
    data class General(
        //TODO: Change Prefix
        val prefix: String = "<dark_aqua>Prefix <gray>»",
        val reload: String = "<prefix> <gray>Reloaded configuration files.",
    )

    companion object {
        const val FILE_NAME = "language.yml"
        val languageFile = File(Platform.get().pluginDirectory, FILE_NAME)
        private var config = fromConfig() ?: defaultConfig()

        fun get(): LanguageConfig {
            return config
        }

        fun defaultConfig(): LanguageConfig {
            return LanguageConfig().apply { save() }
        }

        fun fromConfig(): LanguageConfig? {
            return fromConfig<LanguageConfig>(languageFile)
        }

        fun reload() {
            config = fromConfig() ?: defaultConfig()
        }
    }

}