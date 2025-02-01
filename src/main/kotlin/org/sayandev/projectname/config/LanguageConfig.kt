package org.sayandev.projectname.config

import org.sayandev.projectname.Platform
import org.sayandev.stickynote.core.configuration.Config
import org.spongepowered.configurate.objectmapping.ConfigSerializable
import java.io.File

var language: LanguageConfig = LanguageConfig.fromConfig() ?: LanguageConfig.defaultConfig()

@ConfigSerializable
data class LanguageConfig(
    val general: General = General()
): Config(Platform.get().pluginDirectory, FILE_NAME) {

    init {
        load()
    }

    @ConfigSerializable
    data class General(
        //TODO: Change Prefix
        val prefix: String = "<dark_aqua>Prefix <gray>»",
    )

    companion object {
        const val FILE_NAME = "language.yml"
        val languageFile = File(Platform.get().pluginDirectory, FILE_NAME)

        fun defaultConfig(): LanguageConfig {
            return LanguageConfig()
        }

        fun fromConfig(): LanguageConfig? {
            return fromConfig<LanguageConfig>(languageFile)
        }

        fun reloadConfig() {
            language = fromConfig() ?: defaultConfig()
        }
    }

}