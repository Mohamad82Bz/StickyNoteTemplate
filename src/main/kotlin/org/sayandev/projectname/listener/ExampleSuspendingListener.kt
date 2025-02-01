package org.sayandev.projectname.listener

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

class ExampleSuspendingListener: Listener {

    @EventHandler
    suspend fun onJoin(event: PlayerQuitEvent) {
        //intense computation here, Note that suspending listeners cannot cancel the event
    }

}