package com.awildboop.blinkohchat.commands;

import com.awildboop.blinkohchat.BlinkohChat;
import com.awildboop.blinkohchat.BlinkohChatInventory;
import com.awildboop.blinkohchat.managers.PrefixManager;
import com.awildboop.blinkohchat.utils.GuiUtils;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class PrefixCommand implements CommandExecutor, Listener {
    private final BlinkohChat plugin;
    private final PrefixManager prefixManager;
    private final Permission perms;

    public PrefixCommand(@NotNull BlinkohChat plugin) {
        this.plugin = plugin;
        this.prefixManager = plugin.getPrefixManager();
        this.perms = plugin.getPerms();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) return false;
        if (!(perms.has(sender, "blinkohchat.change-prefix") ||
                perms.has(sender, "blinkohchat.admin.change-prefix") ||
                perms.has(sender, "blinkohchat.admin.*") ||
                perms.has(sender, "blinkohchat.*"))) return false;

        Player target = null;
        if (args.length > 0) {
            if (!(perms.has(sender, "blinkohchat.admin.change-prefix") ||
                    perms.has(sender, "blinkohchat.admin.*"))) return false;

            target = Bukkit.getPlayer(args[0]);
            if (Objects.isNull(target)) {
                player.sendMessage(MiniMessage.miniMessage().deserialize("<red>Invalid target!</red>"));
                return false;
            }
        } else {
            target = player;
        }

        final BlinkohChatInventory inv = new BlinkohChatInventory(plugin, "Prefix Selector");
        final NamespacedKey key = new NamespacedKey(plugin, "prefix-content");

        int index = 1;
        for (String prefix : prefixManager.getGlobalPrefixes()) {
            if (perms.has(target, "blinkohchat.prefix.global." + index)) {
                ItemStack item = GuiUtils.guiItem(
                        Material.NAME_TAG,
                        prefix, key,
                        MiniMessage.miniMessage().deserialize("<italic>This global prefix is available to everyone</italic>")
                );

                inv.getInventory().addItem(item);
            }

            index++;
        }

        for (String prefix : prefixManager.getPersonalPrefixes(target)) {
            ItemStack item = GuiUtils.guiItem(
                    Material.NAME_TAG,
                    prefix, key,
                    MiniMessage.miniMessage().deserialize("<italic>This is a personal prefix.</italic>")
            );

            inv.getInventory().addItem(item);
        }

        player.openInventory(inv.getInventory());
        return true;
    }

}
