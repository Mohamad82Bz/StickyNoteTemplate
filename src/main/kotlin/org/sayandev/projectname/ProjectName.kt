package org.sayandev.projectname

import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import org.bukkit.plugin.java.JavaPlugin
import org.sayandev.projectname.command.ExampleCommand
import org.sayandev.projectname.config.LanguageConfig
import org.sayandev.projectname.database.Database
import org.sayandev.projectname.listener.ExampleSuspendingListener
import org.sayandev.stickynote.bukkit.hasPlugin
import org.sayandev.stickynote.bukkit.hook.PlaceholderAPIHook
import org.sayandev.stickynote.bukkit.log
import org.sayandev.stickynote.bukkit.registerSuspendingListener
import org.sayandev.stickynote.bukkit.utils.AdventureUtils
import org.sayandev.stickynote.loader.bukkit.StickyNoteBukkitLoader

class ProjectName: JavaPlugin() {

    override fun onEnable() {
        try {
            StickyNoteBukkitLoader(this)
        } catch (e: Exception) {
            logger.severe("Could not download libraries. The plugin requires its external libraries to function.")
            logger.severe("This happens if your server's network is unstable. You can try restarting the server.")
            logger.severe("You can also try to delete plugins/stickynote/lib directory and restarting the server.")
            logger.severe("For more detailed help, please contact us.")
            server.pluginManager.disablePlugin(this)
            return
        }
        Platform.setPlatform(Platform("bukkit", logger, dataFolder))

        try {
            log("Connecting to the database..")
            Database
        } catch (e: ExceptionInInitializerError) {
            logger.severe("Failed to connect to the database. Disabling plugin. Full stacktrace can be found below:")
            e.printStackTrace()
            server.pluginManager.disablePlugin(this)
            return
        }

        log("Registering commands..")
        ExampleCommand()

        log("Registering listeners..")
        registerSuspendingListener(ExampleSuspendingListener())

        if (!initializeConfigFiles()) {
            server.pluginManager.disablePlugin(this)
            return
        }

        AdventureUtils.setTagResolver(Placeholder.parsed("prefix", LanguageConfig.get().general.prefix))

        //Enable PlaceholderAPI if it was installed
        if (hasPlugin("PlaceholderAPI")) {
            log("PlaceholderAPI found. Enabling PlaceholderAPI hook..")
            PlaceholderAPIHook.injectComponent()
        }
    }

    override fun onDisable() {

    }

    fun initializeConfigFiles(): Boolean {
        try {
            log("Loading ${LanguageConfig.FILE_NAME}")
            LanguageConfig.reload()
            return true
        } catch (e: ExceptionInInitializerError) {
            logger.severe("Failed to load one of the config files. This is likely due to a syntax error in the config file," +
                    " or the plugin cannot add a new entry due to a plugin update.")
            logger.severe("Please try to backup and reset this config file.")

            e.printStackTrace()
            return false
        }
    }

}