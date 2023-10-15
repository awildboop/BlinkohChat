package com.awildboop.blinkohchat.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class GuiUtils {
    public static ItemStack guiItem(Material material, String name, NamespacedKey key, Component... lore) {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();

        meta.displayName(MiniMessage.miniMessage().deserialize(name));
        meta.lore(List.of(lore));
        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, name);

        item.setItemMeta(meta);
        return item;
    }
}
