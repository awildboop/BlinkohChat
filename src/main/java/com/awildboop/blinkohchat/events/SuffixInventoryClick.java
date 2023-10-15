package com.awildboop.blinkohchat.events;

import com.awildboop.blinkohchat.BlinkohChat;
import com.awildboop.blinkohchat.BlinkohChatInventory;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class SuffixInventoryClick implements Listener {
    private final BlinkohChat plugin;

    public SuffixInventoryClick(@NotNull BlinkohChat plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (!(e.getInventory().getHolder(false) instanceof BlinkohChatInventory)) return;
        if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) return;
        if (!(e.getWhoClicked() instanceof Player)) return;

        e.setCancelled(true);
        e.getView().close();

        final NamespacedKey key = new NamespacedKey(plugin, "suffix-content");
        final PersistentDataContainer pdc = e.getCurrentItem().getItemMeta().getPersistentDataContainer();
        final String content = pdc.get(key, PersistentDataType.STRING);

        if (pdc.has(key, PersistentDataType.STRING)) {
            Component suffix = MiniMessage.miniMessage().deserialize(Objects.requireNonNull(content));
            plugin.getPlayerManager().setPlayerSuffix((Player) e.getWhoClicked(), suffix);
            e.getWhoClicked().sendMessage(MiniMessage.miniMessage().deserialize("<green>Changed your suffix to <reset>" + content));
        }
    }
}
