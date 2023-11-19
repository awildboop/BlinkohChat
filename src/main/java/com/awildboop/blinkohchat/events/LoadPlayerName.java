package com.awildboop.blinkohchat.events;

import com.awildboop.blinkohchat.BlinkohChat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

public class LoadPlayerName implements Listener {
    private final BlinkohChat plugin;

    public LoadPlayerName(@NotNull BlinkohChat plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> p.displayName(plugin.getPlayerManager().getPlayerName(p)));
    }
}
