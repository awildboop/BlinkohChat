package com.awildboop.blinkohchat.managers;

import com.awildboop.blinkohchat.BlinkohChat;
import com.awildboop.blinkohchat.BlinkohChatDecorator;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class DecoratorManager {
    private final BlinkohChat plugin;
    private final DatabaseManager dbManager;

    public DecoratorManager(@NotNull BlinkohChat plugin) {
        this.plugin = plugin;
        this.dbManager = plugin.getDatabaseManager();
    }

    public List<String> getPersonalDecorators(Player player, BlinkohChatDecorator type) {
        List<String> decorators = new ArrayList<>();
        try (Connection conn = dbManager.getDataSource().getConnection()) {
            PreparedStatement sql = conn.prepareStatement("SELECT `content` FROM `decorators` WHERE `uuid`=? AND `type`=?;");
            sql.setString(1, player.getUniqueId().toString());
            sql.setString(2, type.toString());

            ResultSet res = sql.executeQuery();
            while (res.next()) {
                decorators.add(res.getString("content"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return decorators;
    }

    public void addPersonalDecorator(Player player, BlinkohChatDecorator type, String content) {
        try (Connection conn = dbManager.getDataSource().getConnection()) {
            PreparedStatement sql = conn.prepareStatement("INSERT IGNORE INTO `decorators` (`uuid`, `type`, `content`) VALUES (?, ?, ?);");
            sql.setString(1, player.getUniqueId().toString());
            sql.setString(2, type.toString());
            sql.setString(3, content);
            sql.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addPersonalDecorators(Player player, BlinkohChatDecorator type, List<String> contents) {
        if (contents.isEmpty()) return;

        try (Connection conn = dbManager.getDataSource().getConnection()) {
            PreparedStatement sql = conn.prepareStatement("INSERT IGNORE INTO `decorators` (`uuid`, `type`,`content`) VALUES (?, ?, ?);");
            for (String content : contents) {
                sql.setString(1, player.getUniqueId().toString());
                sql.setString(2, type.toString());
                sql.setString(3, content);
                sql.addBatch();
            }

            sql.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removePersonalDecorator(Player player, BlinkohChatDecorator type, String content) {
        try (Connection conn = dbManager.getDataSource().getConnection()) {
            PreparedStatement sql = conn.prepareStatement("DELETE FROM `decorators` WHERE `uuid`=? AND `type`=? AND `content`=?;");
            sql.setString(1, player.getUniqueId().toString());
            sql.setString(2, type.toString());
            sql.setString(3, content);
            sql.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removePersonalDecorators(Player player, BlinkohChatDecorator type, List<String> contents) {
        if (contents.isEmpty()) return;

        try (Connection conn = dbManager.getDataSource().getConnection()) {
            PreparedStatement sql = conn.prepareStatement("DELETE FROM `decorators` WHERE `uuid`=? AND `type`=? AND `content`=?;");
            for (String content : contents) {
                sql.setString(1, player.getUniqueId().toString());
                sql.setString(2, type.toString());
                sql.setString(3, content);
                sql.addBatch();
            }

            sql.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> getGlobalDecorators(BlinkohChatDecorator type) {
        return plugin.getConfig().getStringList("global-"+type.toString());
    }

    public void addGlobalDecorator(BlinkohChatDecorator type, String content) {
            List<String> newList = getGlobalDecorators(type);
            newList.add(content);
            plugin.getConfig().set("global-"+type, newList);
    }

    public void addGlobalDecorators(BlinkohChatDecorator type, List<String> contents) {
        plugin.getConfig().set("global-"+type.toString(), Stream.concat(getGlobalDecorators(type).stream(), contents.stream()).toList());
    }

    public void removeGlobalDecorator(BlinkohChatDecorator type, String content) {
        List<String> newList = getGlobalDecorators(type);
        newList.remove(content);
        plugin.getConfig().set("global-"+type, newList);
    }

    public void removeGlobalDecorators(BlinkohChatDecorator type, List<String> contents) {
        for (String content : contents) {
            removeGlobalDecorator(type, content);
        }
    }
}
