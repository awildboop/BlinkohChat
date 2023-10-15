package com.awildboop.blinkohchat.commands;

import com.awildboop.blinkohchat.BlinkohChat;
import com.awildboop.blinkohchat.BlinkohChatInventory;
import com.awildboop.blinkohchat.managers.SuffixManager;
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
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class SuffixCommand implements CommandExecutor {
    private final BlinkohChat plugin;
    private final SuffixManager suffixManager;
    private final Permission perms;

    public SuffixCommand(@NotNull BlinkohChat plugin) {
        this.plugin = plugin;
        this.suffixManager = plugin.getSuffixManager();
        this.perms = plugin.getPerms();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) return false;
        if (!(perms.has(sender, "blinkohchat.change-suffix") ||
                perms.has(sender, "blinkohchat.admin.change-suffix") ||
                perms.has(sender, "blinkohchat.admin.*") ||
                perms.has(sender, "blinkohchat.*"))) return false;

        Player target = null;
        if (args.length > 0) {
            if (!(perms.has(sender, "blinkohchat.admin.change-suffix") ||
                    perms.has(sender, "blinkohchat.admin.*"))) return false;

            target = Bukkit.getPlayer(args[0]);
            if (Objects.isNull(target)) {
                player.sendMessage(MiniMessage.miniMessage().deserialize("<red>Invalid target!</red>"));
                return false;
            }
        } else {
            target = player;
        }

        final BlinkohChatInventory inv = new BlinkohChatInventory(plugin, "Suffix Selector");
        final NamespacedKey key = new NamespacedKey(plugin, "suffix-content");

        int index = 1;
        for (String suffix : suffixManager.getGlobalSuffixes()) {
            if (perms.has(target, "blinkohchat.suffix.global." + index)) {
                ItemStack item = GuiUtils.guiItem(
                        Material.NAME_TAG,
                        suffix, key,
                        MiniMessage.miniMessage().deserialize("<italic>This global suffix is available to everyone</italic>")
                );

                inv.getInventory().addItem(item);
            }

            index++;
        }

        for (String suffix : suffixManager.getPersonalSuffixes(target)) {
            ItemStack item = GuiUtils.guiItem(
                    Material.NAME_TAG,
                    suffix, key,
                    MiniMessage.miniMessage().deserialize("<italic>This is a personal suffix.</italic>")
            );

            inv.getInventory().addItem(item);
        }

        player.openInventory(inv.getInventory());
        return true;
    }
}
