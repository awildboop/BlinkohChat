package com.awildboop.blinkohchat.events;

import com.awildboop.blinkohchat.BlinkohChat;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class LoadPlayerName implements Listener {
    private final BlinkohChat plugin;

    public LoadPlayerName(BlinkohChat plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        p.displayName(plugin.getPlayerManager().getPlayerName(p));
    }
}
