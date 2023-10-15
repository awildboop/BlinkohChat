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

public class SuffixManager {
    private final BlinkohChat plugin;
    private final DatabaseManager dbManager;

    public SuffixManager(@NotNull BlinkohChat plugin) {
        this.plugin = plugin;
        this.dbManager = plugin.getDatabaseManager();
    }

    // Personal Suffixes \\
    public List<String> getPersonalSuffixes(Player player) {
        List<String> suffixes = new ArrayList<String>();
        try (Connection conn = dbManager.getDataSource().getConnection()) {
            PreparedStatement sql = conn.prepareStatement("SELECT uuid, suffix FROM suffixes WHERE uuid=?;");
            sql.setString(1, player.getUniqueId().toString());

            ResultSet res = sql.executeQuery();
            while (res.next()) {
                suffixes.add(res.getString("suffix"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return suffixes;
    }

    /* ---- Adding personal suffixes ---- */
    public void addPersonalSuffix(Player player, String suffix) {
        try (Connection conn = dbManager.getDataSource().getConnection()) {
            PreparedStatement sql = conn.prepareStatement("INSERT IGNORE INTO `suffixes` (`uuid`, `suffix`) VALUES (?, ?);");
            sql.setString(1, player.getUniqueId().toString());
            sql.setString(2, suffix);
            sql.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addPersonalSuffixes(Player player, List<String> suffixes) {
        if (suffixes.isEmpty()) return;

        try (Connection conn = dbManager.getDataSource().getConnection()) {
            PreparedStatement sql = conn.prepareStatement("INSERT IGNORE INTO `suffixes` (`uuid`, `suffix`) VALUES (?, ?);");
            for (String suffix : suffixes) {
                sql.setString(1, player.getUniqueId().toString());
                sql.setString(2, suffix);
                sql.addBatch();
            }

            sql.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    /* ---- Adding personal suffixes ---- */

    /* ---- Removing personal suffixes ---- */
    public void removePersonalSuffix(Player player, String suffix) {
        try (Connection conn = dbManager.getDataSource().getConnection()) {
            PreparedStatement sql = conn.prepareStatement("DELETE FROM `suffixes` WHERE `uuid` = ? AND `suffix` = ?;");
            sql.setString(1, player.getUniqueId().toString());
            sql.setString(2, suffix);
            sql.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removePersonalSuffixes(Player player, List<String> suffixes) {
        if (suffixes.isEmpty()) return;

        try (Connection conn = dbManager.getDataSource().getConnection()) {
            PreparedStatement sql = conn.prepareStatement("DELETE FROM `suffixes` WHERE `uuid` = ? AND `suffix` = ?;");
            for (String suffix : suffixes) {
                sql.setString(1, player.getUniqueId().toString());
                sql.setString(2, suffix);
                sql.addBatch();
            }

            sql.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    /* ---- Removing personal suffixes ---- */

    // Global Suffixes \\
    public List<String> getGlobalSuffixes() {
        return plugin.getConfig().getStringList("global-suffixes");
    }

    /* ---- Add global suffixes ---- */
    public void addGlobalSuffix(String suffix) {
        List<String> newList = getGlobalSuffixes();
        newList.add(suffix);
        plugin.getConfig().set("global-suffixes", newList);
    }

    public void addGlobalSuffixes(List<String> suffixes) {
        for (String suffix : suffixes) {
            addGlobalSuffix(suffix);
        }
    }
    /* ---- Add global suffixes ---- */

    /* ---- Remove global suffixes ---- */
    public void removeGlobalSuffix(String suffix) {
        List<String> newList = getGlobalSuffixes();
        newList.remove(suffix);
        plugin.getConfig().set("global-suffixes", newList);
    }

    public void removeGlobalSuffixes(List<String> suffixes) {
        for (String suffix : suffixes) {
            removeGlobalSuffix(suffix);
        }
    }
    /* ---- Remove global suffixes ---- */
}
