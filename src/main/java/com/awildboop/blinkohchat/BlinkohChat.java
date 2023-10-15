package com.awildboop.blinkohchat;

import com.awildboop.blinkohchat.managers.ColorManager;
import com.awildboop.blinkohchat.managers.DatabaseManager;
import com.awildboop.blinkohchat.managers.PlayerManager;
import com.awildboop.blinkohchat.managers.PrefixManager;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class BlinkohChat extends JavaPlugin {
    private static Chat chat;
    private ColorManager colorManager;
    private DatabaseManager databaseManager;
    private PlayerManager playerManager;
    private PrefixManager prefixManager;

    @Override
    public void onEnable() {
        if (!loadVault()) {
            getLogger().severe("Unable to load Vault, disabling...");
            getServer().getPluginManager().disablePlugin(this);
        }

        loadManagers();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public boolean loadVault() {
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        chat = rsp != null ? rsp.getProvider() : null;
        return chat != null;
    }

    public void loadManagers() {
        this.colorManager = new ColorManager(this);
        this.databaseManager = new DatabaseManager(this);
        this.playerManager = new PlayerManager(this);
        this.prefixManager = new PrefixManager(this);
    }

    public Chat getChat() {
        return chat;
    }

    public ColorManager getColorManager() {
        return colorManager;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public PrefixManager getPrefixManager() {
        return prefixManager;
    }

}
