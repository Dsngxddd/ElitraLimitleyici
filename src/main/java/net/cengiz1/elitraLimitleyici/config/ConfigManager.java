package net.cengiz1.elitraLimitleyici.config;

import net.cengiz1.elitraLimitleyici.ElitraLimitleyici;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {

    private final ElitraLimitleyici plugin;
    private FileConfiguration config;
    private int maxCriticalHits;
    private int timeWindowSeconds;
    private int disableDurationSeconds;
    private String discordWebhookUrl;
    private String notificationMessage;
    private String playerNotificationMessage;
    private boolean enableNotifications;
    private boolean enableDiscordWebhook;
    private boolean enablePlayerMessage;
    private String webhookUsername;
    private String webhookAvatarUrl;

    public ConfigManager(ElitraLimitleyici plugin) {
        this.plugin = plugin;
    }

    public void loadConfig() {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        config = plugin.getConfig();
        config.addDefault("max-critical-hits", 3);
        config.addDefault("time-window-seconds", 5);
        config.addDefault("disable-duration-seconds", 3);
        config.addDefault("enable-notifications", true);
        config.addDefault("enable-discord-webhook", true);
        config.addDefault("enable-player-message", true);
        config.addDefault("discord-webhook-url", "URL");
        config.addDefault("webhook-username", "Elitra sikici");
        config.addDefault("webhook-avatar-url", "");
        config.addDefault("notification-message", "Oyuncu %player% amunu yurdunu siktiğim çok fazla kiritik vurdu he");
        config.addDefault("player-notification-message", "&cÇok fazla kritik vuruş yaptıgın icin %saniye% saniye elitran kapalı");
        config.options().copyDefaults(true);
        plugin.saveConfig();
        maxCriticalHits = config.getInt("max-critical-hits");
        timeWindowSeconds = config.getInt("time-window-seconds");
        disableDurationSeconds = config.getInt("disable-duration-seconds");
        enableNotifications = config.getBoolean("enable-notifications");
        enableDiscordWebhook = config.getBoolean("enable-discord-webhook");
        enablePlayerMessage = config.getBoolean("enable-player-message");
        discordWebhookUrl = config.getString("discord-webhook-url");
        notificationMessage = config.getString("notification-message");
        playerNotificationMessage = config.getString("player-notification-message");
        webhookUsername = config.getString("webhook-username");
        webhookAvatarUrl = config.getString("webhook-avatar-url");
    }

    public void reloadConfig() {
        plugin.reloadConfig();
        loadConfig();
    }

    public int getMaxCriticalHits() {
        return maxCriticalHits;
    }

    public int getTimeWindowSeconds() {
        return timeWindowSeconds;
    }

    public int getDisableDurationSeconds() {
        return disableDurationSeconds;
    }

    public String getDiscordWebhookUrl() {
        return discordWebhookUrl;
    }

    public String getNotificationMessage() {
        return notificationMessage;
    }

    public String getPlayerNotificationMessage() {
        return playerNotificationMessage;
    }

    public boolean isEnableNotifications() {
        return enableNotifications;
    }

    public boolean isEnableDiscordWebhook() {
        return enableDiscordWebhook;
    }

    public boolean isEnablePlayerMessage() {
        return enablePlayerMessage;
    }

    public String getWebhookUsername() {
        return webhookUsername;
    }

    public String getWebhookAvatarUrl() {
        return webhookAvatarUrl;
    }
}