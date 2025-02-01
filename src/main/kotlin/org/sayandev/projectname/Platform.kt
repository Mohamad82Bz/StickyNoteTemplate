package org.sayandev.projectname

import java.io.File
import java.util.logging.Logger

@JvmRecord
data class Platform(
    val id: String,
    val logger: Logger,
    val pluginDirectory: File
) {

    companion object {
        private var currentPlatform = Platform("default", Logger.getGlobal(), File("."))

        @JvmStatic
        fun setPlatform(platform: Platform) {
            currentPlatform = platform
        }

        @JvmStatic
        fun get() = currentPlatform
    }

}