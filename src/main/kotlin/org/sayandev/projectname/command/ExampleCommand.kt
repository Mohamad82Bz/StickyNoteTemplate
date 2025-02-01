package org.sayandev.projectname.command

import org.sayandev.projectname.config.LanguageConfig
import org.sayandev.projectname.config.language
import org.sayandev.stickynote.bukkit.command.BukkitCommand
import org.sayandev.stickynote.bukkit.command.player
import org.sayandev.stickynote.bukkit.extension.sendComponent
import org.sayandev.stickynote.bukkit.plugin

class ExampleCommand: BukkitCommand("examplecommand") {

    init {
        registerHelpLiteral()
        rawCommandBuilder().registerCopy {
            literalWithPermission("reload")
            handler { context ->
                val player = context.player() ?: return@handler

                LanguageConfig.reloadConfig()

                player.sendComponent(language.general.reload)
            }
        }
    }

}