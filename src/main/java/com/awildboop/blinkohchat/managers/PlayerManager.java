package com.awildboop.blinkohchat.managers;

import com.awildboop.blinkohchat.BlinkohChat;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerManager {
    private final BlinkohChat plugin;
    private final DatabaseManager dbManager;
    private final Chat chat;


    public PlayerManager(@NotNull BlinkohChat plugin) {
        this.plugin = plugin;
        this.dbManager = plugin.getDatabaseManager();
        this.chat = plugin.getChat();
    }

    public void setPlayerPrefix(Player player, Component prefix) {
        chat.setPlayerPrefix(player, MiniMessage.miniMessage().serialize(prefix));
    }

    public void setPlayerSuffix(Player player, Component suffix) {
        chat.setPlayerPrefix(player, MiniMessage.miniMessage().serialize(suffix));
    }

    public void setPlayerName(Player player, Component name) {
        player.displayName(name);

        try (Connection conn = dbManager.getDataSource().getConnection()) {
            PreparedStatement sql = conn.prepareStatement("INSERT IGNORE INTO `names` (`uuid`, `name`) VALUES (?, ?) " +
                    "ON DUPLICATE KEY UPDATE `name`=?;");
            sql.setString(1, player.getUniqueId().toString());
            sql.setString(2, name.toString());
            sql.setString(3, name.toString());
            sql.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Component getPlayerName(Player player) {
        try (Connection conn = dbManager.getDataSource().getConnection()) {
            PreparedStatement sql = conn.prepareStatement("SELECT name FROM names WHERE uuid=?;");
            sql.setString(1, player.getUniqueId().toString());

            ResultSet res = sql.executeQuery();
            if (!res.next()) {
                return player.name();
            } else {
                return MiniMessage.miniMessage().deserialize(res.getString("name"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
