package net.cengiz1.elitraLimitleyici.config;

import net.cengiz1.elitraLimitleyici.ElitraLimitleyici;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Arrays;
import java.util.List;

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
    private boolean preventGhostFireworks;
    private int maxGhostFireworkWarnings;
    private long ghostFireworkCooldownMs;
    private boolean ghostFireworkDisablesElytra;
    private int ghostFireworkPunishmentDurationTicks;
    private String ghostFireworkNotificationMessage;
    private String ghostFireworkPlayerMessage;
    private String ghostFireworkPunishmentMessage;
    private List<String> ghostFireworkPunishmentCommands;

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
        config.addDefault("ghost-firework.prevent-ghost-fireworks", true);
        config.addDefault("ghost-firework.max-warnings", 3);
        config.addDefault("ghost-firework.warning-cooldown-ms", 5000);
        config.addDefault("ghost-firework.disables-elytra", true);
        config.addDefault("ghost-firework.punishment-duration-ticks", 1200); // 60 seconds
        config.addDefault("ghost-firework.notification-message", "Oyuncu %player% ghost fisek kullanmaya calisti! (%count%/%max%)");
        config.addDefault("ghost-firework.player-message", "&cElinde fisek yokken elytra boost yapamazsin! Uyari: %count%/%max%");
        config.addDefault("ghost-firework.punishment-message", "&4Cok fazla ghost fisek kullandin ve cezalandirildin!");
        config.addDefault("ghost-firework.punishment-commands", Arrays.asList(
                "title %player% title {\"text\":\"Ghost Fişek Kullanamazsın!\",\"color\":\"red\",\"bold\":true}",
                "title %player% subtitle {\"text\":\"Cezalandırıldın\",\"color\":\"yellow\"}"));

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
        preventGhostFireworks = config.getBoolean("ghost-firework.prevent-ghost-fireworks");
        maxGhostFireworkWarnings = config.getInt("ghost-firework.max-warnings");
        ghostFireworkCooldownMs = config.getLong("ghost-firework.warning-cooldown-ms");
        ghostFireworkDisablesElytra = config.getBoolean("ghost-firework.disables-elytra");
        ghostFireworkPunishmentDurationTicks = config.getInt("ghost-firework.punishment-duration-ticks");
        ghostFireworkNotificationMessage = config.getString("ghost-firework.notification-message");
        ghostFireworkPlayerMessage = config.getString("ghost-firework.player-message");
        ghostFireworkPunishmentMessage = config.getString("ghost-firework.punishment-message");
        ghostFireworkPunishmentCommands = config.getStringList("ghost-firework.punishment-commands");
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

    // Ghost firework getters
    public boolean isPreventGhostFireworks() {
        return preventGhostFireworks;
    }

    public int getMaxGhostFireworkWarnings() {
        return maxGhostFireworkWarnings;
    }

    public long getGhostFireworkCooldownMs() {
        return ghostFireworkCooldownMs;
    }

    public boolean isGhostFireworkDisablesElytra() {
        return ghostFireworkDisablesElytra;
    }

    public int getGhostFireworkPunishmentDurationTicks() {
        return ghostFireworkPunishmentDurationTicks;
    }

    public String getGhostFireworkNotificationMessage() {
        return ghostFireworkNotificationMessage;
    }

    public String getGhostFireworkPlayerMessage() {
        return ghostFireworkPlayerMessage;
    }

    public String getGhostFireworkPunishmentMessage() {
        return ghostFireworkPunishmentMessage;
    }

    public List<String> getGhostFireworkPunishmentCommands() {
        return ghostFireworkPunishmentCommands;
    }
}