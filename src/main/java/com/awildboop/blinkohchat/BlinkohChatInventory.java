package com.awildboop.blinkohchat;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public class BlinkohChatInventory implements InventoryHolder {
    private final Inventory inventory;

    public BlinkohChatInventory(@NotNull BlinkohChat plugin, @NotNull String title) {
        this.inventory = plugin.getServer().createInventory(this,
                InventoryType.CHEST,
                MiniMessage.miniMessage().deserialize(title)
        );
    }

    @Override
    public @NotNull Inventory getInventory() {
        return this.inventory;
    }
}
