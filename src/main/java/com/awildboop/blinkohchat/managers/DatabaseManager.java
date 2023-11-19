package com.awildboop.blinkohchat.managers;

import com.awildboop.blinkohchat.BlinkohChat;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.Statement;

public class DatabaseManager {
    private final BlinkohChat plugin;
    private HikariDataSource dataSource;

    public DatabaseManager(@NotNull BlinkohChat plugin) {
        this.plugin = plugin;
    }

    public boolean connect() {
        final String host = plugin.getConfig().getString("database.sql.host");
        final String port = plugin.getConfig().getString("database.sql.port");
        final String database = plugin.getConfig().getString("database.sql.database");
        final String username = plugin.getConfig().getString("database.sql.username");
        final String password = plugin.getConfig().getString("database.sql.password");

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://" + host + ':' + port + '/' + database); // Address of your running MySQL database
        config.setUsername(username);
        config.setPassword(password);
        config.setMaximumPoolSize(10);
        dataSource = new HikariDataSource(config);

        plugin.getLogger().info("Loading database...");
        try (Connection conn = dataSource.getConnection()) {
            Statement sql = conn.createStatement();
            sql.execute("""
                    CREATE TABLE IF NOT EXISTS `decorators` (
                        `uuid` CHAR(36),
                        `type` VARCHAR(10),
                        `content` VARCHAR(50),
                        INDEX `decs` (`uuid`, `type`),
                        INDEX `contents` (`content`)
                    );"""
            );

            sql.execute("""
                    CREATE TABLE IF NOT EXISTS `names` (
                        `uuid` CHAR(36),
                        `name` VARCHAR(50),
                        UNIQUE INDEX `uuids` (`uuid`),
                        UNIQUE INDEX `names` (`name`)
                    );"""
            );

            // used for development vvv
            if (plugin.getConfig().getBoolean("database.sql.sample")) {
                sql.execute("INSERT IGNORE INTO `decorators` (`uuid`, `type`, `content`) VALUES ('266b4077-3441-42bb-ba4b-a791edc7b260', 'prefix', 'prefix1')");
                sql.execute("INSERT IGNORE INTO `decorators` (`uuid`, `type`, `content`) VALUES ('266b4077-3441-42bb-ba4b-a791edc7b260', 'prefix', 'prefix2')");
                sql.execute("INSERT IGNORE INTO `decorators` (`uuid`, `type`, `content`) VALUES ('266b4077-3441-42bb-ba4b-a791edc7b260', 'suffix', 'suffix1')");
                sql.execute("INSERT IGNORE INTO `decorators` (`uuid`, `type`, `content`) VALUES ('266b4077-3441-42bb-ba4b-a791edc7b260', 'suffix', 'suffix2')");
            }
            // used for development ^^^

        } catch (Exception e) {
            plugin.getLogger().warning(e.getMessage());
            plugin.getLogger().warning("Encountered error loading database!");
            return false;
        }

        plugin.getLogger().info("Database loaded!");
        return true;
    }

    public HikariDataSource getDataSource() {
        return this.dataSource;
    }
}

/*



 */