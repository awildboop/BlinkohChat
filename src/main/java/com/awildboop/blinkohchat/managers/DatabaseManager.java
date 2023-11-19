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
            sql.execute("CREATE TABLE IF NOT EXISTS `decorators` (\n" +
                    "    `uuid` VARCHAR(36),\n" +
                    "    `type` VARCHAR(10),\n" +
                    "    `content` VARCHAR(50),\n" +
                    "    INDEX `uuids` (`uuid`),\n" +
                    "    INDEX `prefixes` (`prefix`)\n" +
                    ");"
            );

            sql.execute("CREATE TABLE IF NOT EXISTS `names` (\n" +
                    "    `uuid` CHAR(36),\n" +
                    "    `name` VARCHAR(50),\n" +
                    "    UNIQUE INDEX `uuids` (`uuid`),\n" +
                    "    UNIQUE INDEX `names` (`name`)\n" +
                    ");"
            );

            // used for development vvvvv
            if (plugin.getConfig().getBoolean("database.sql.sample")) {
                sql.execute("INSERT IGNORE INTO `prefixes` (`uuid`, `prefix`) VALUES ('266b4077-3441-42bb-ba4b-a791edc7b260', 'prefix1')");
                sql.execute("INSERT IGNORE INTO `prefixes` (`uuid`, `prefix`) VALUES ('266b4077-3441-42bb-ba4b-a791edc7b260', 'prefix2')");
                sql.execute("INSERT IGNORE INTO `prefixes` (`uuid`, `prefix`) VALUES ('266b4077-3441-42bb-ba4b-a791edc7b260', 'prefix3')");
                sql.execute("INSERT IGNORE INTO `prefixes` (`uuid`, `prefix`) VALUES ('266b4077-3441-42bb-ba4b-a791edc7b260', 'prefix4')");
            }
            // used for development ^^^^^

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