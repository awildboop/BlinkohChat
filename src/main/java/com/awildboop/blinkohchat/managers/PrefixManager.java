package com.awildboop.blinkohchat.managers;

import com.awildboop.blinkohchat.BlinkohChat;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PrefixManager {
    private final BlinkohChat plugin;
    private final DatabaseManager dbManager;

    public PrefixManager(@NotNull BlinkohChat plugin) {
        this.plugin = plugin;
        this.dbManager = plugin.getDatabaseManager();
    }

    // Personal Prefixes \\
    public List<String> getPersonalPrefixes(Player player) {
        List<String> prefixes = new ArrayList<String>();
        try (Connection conn = dbManager.getDataSource().getConnection()) {
            PreparedStatement sql = conn.prepareStatement("SELECT prefix FROM prefixes WHERE uuid=?;");
            sql.setString(1, player.getUniqueId().toString());

            ResultSet res = sql.executeQuery();
            while (res.next()) {
                prefixes.add(res.getString("prefix"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return prefixes;
    }

    /* ---- Adding personal prefixes ---- */
    public void addPersonalPrefix(Player player, String prefix) {
        try (Connection conn = dbManager.getDataSource().getConnection()) {
            PreparedStatement sql = conn.prepareStatement("INSERT IGNORE INTO `prefixes` (`uuid`, `prefix`) VALUES (?, ?);");
            sql.setString(1, player.getUniqueId().toString());
            sql.setString(2, prefix);
            sql.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addPersonalPrefixes(Player player, List<String> prefixes) {
        if (prefixes.isEmpty()) return;

        try (Connection conn = dbManager.getDataSource().getConnection()) {
            PreparedStatement sql = conn.prepareStatement("INSERT IGNORE INTO `prefixes` (`uuid`, `prefix`) VALUES (?, ?);");
            for (String prefix : prefixes) {
                sql.setString(1, player.getUniqueId().toString());
                sql.setString(2, prefix);
                sql.addBatch();
            }

            sql.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    /* ---- Adding personal prefixes ---- */

    /* ---- Removing personal prefixes ---- */
    public void removePersonalPrefix(Player player, String prefix) {
        try (Connection conn = dbManager.getDataSource().getConnection()) {
            PreparedStatement sql = conn.prepareStatement("DELETE FROM `prefixes` WHERE `uuid` = ? AND prefix ?;");
            sql.setString(1, player.getUniqueId().toString());
            sql.setString(2, prefix);
            sql.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removePersonalPrefixes(Player player, List<String> prefixes) {
        if (prefixes.isEmpty()) return;

        try (Connection conn = dbManager.getDataSource().getConnection()) {
            PreparedStatement sql = conn.prepareStatement("DELETE FROM `prefixes` WHERE `uuid` = ? AND `prefix` = ?;");
            for (String prefix : prefixes) {
                sql.setString(1, player.getUniqueId().toString());
                sql.setString(2, prefix);
                sql.addBatch();
            }

            sql.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    /* ---- Removing personal prefixes ---- */

    // Global Prefixes \\
    public List<String> getGlobalPrefixes() {
        return plugin.getConfig().getStringList("global-prefixes");
    }

    /* ---- Add global prefixes ---- */
    public void addGlobalPrefix(String prefix) {
        List<String> newList = getGlobalPrefixes();
        newList.add(prefix);
        plugin.getConfig().set("global-prefixes", newList);
    }

    public void addGlobalPrefixes(List<String> prefixes) {
        for (String prefix : prefixes) {
            addGlobalPrefix(prefix);
        }
    }
    /* ---- Add global prefixes ---- */

    /* ---- Remove global prefixes ---- */
    public void removeGlobalPrefix(String prefix) {
        List<String> newList = getGlobalPrefixes();
        newList.remove(prefix);
        plugin.getConfig().set("global-prefixes", newList);
    }

    public void removeGlobalPrefixes(List<String> prefixes) {
        for (String prefix : prefixes) {
            removeGlobalPrefix(prefix);
        }
    }
    /* ---- Remove global prefixes ---- */


}