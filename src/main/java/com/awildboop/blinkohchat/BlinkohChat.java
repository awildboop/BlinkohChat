package com.awildboop.blinkohchat;

import com.awildboop.blinkohchat.commands.PrefixCommand;
import com.awildboop.blinkohchat.commands.SuffixCommand;
import com.awildboop.blinkohchat.events.LoadPlayerName;
import com.awildboop.blinkohchat.events.PrefixInventoryClick;
import com.awildboop.blinkohchat.managers.*;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class BlinkohChat extends JavaPlugin {
    private static Chat chat;
    private static Permission perms;

    private DatabaseManager databaseManager;
    private PlayerManager playerManager;
    private DecoratorManager decoratorManager;
    private ColorManager colorManager;

    @Override
    public void onEnable() {
        if (!loadVaultChat()) {
            getLogger().severe("Unable to load Vault (Chat), disabling...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        if (!loadVaultPerms()) {
            getLogger().severe("Unable to load Vault (Permissions), disabling...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        saveDefaultConfig();
        loadManagers();
        if (!databaseManager.connect()) {
            getLogger().severe("Unable to connect to database, disabling...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Load events
        Bukkit.getPluginManager().registerEvents(new LoadPlayerName(this), this);
        Bukkit.getPluginManager().registerEvents(new PrefixInventoryClick(this), this);

        // Load commands
        Objects.requireNonNull(getCommand("prefix")).setExecutor(new PrefixCommand(this));
        Objects.requireNonNull(getCommand("suffix")).setExecutor(new SuffixCommand(this));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public boolean loadVaultChat() {
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        chat = rsp != null ? rsp.getProvider() : null;
        return chat != null;
    }

    public boolean loadVaultPerms() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp != null ? rsp.getProvider() : null;
        return perms != null;
    }


    public void loadManagers() {
        this.databaseManager = new DatabaseManager(this);
        this.playerManager = new PlayerManager(this);
        this.decoratorManager = new DecoratorManager(this);
        this.colorManager = new ColorManager(this);
    }

    public Chat getChat() {
        return chat;
    }
    public Permission getPerms() {
        return perms;
    }
    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }
    public PlayerManager getPlayerManager() {
        return playerManager;
    }
    public DecoratorManager getDecoratorManager() {
        return decoratorManager;
    }
    public ColorManager getColorManager() {
        return colorManager;
    }


}
