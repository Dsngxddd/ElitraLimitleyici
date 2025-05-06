package net.cengiz1.elitraLimitleyici.manager;

import net.cengiz1.elitraLimitleyici.ElitraLimitleyici;
import net.cengiz1.elitraLimitleyici.config.ConfigManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class NotificationManager {

    private final ElitraLimitleyici plugin;
    private final ConfigManager configManager;

    public NotificationManager(ElitraLimitleyici plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
    }

    /**
     * Send a message to the configured Discord webhook
     *
     * @param content The message content
     */
    public void sendDiscordWebhook(String content) {
        if (!configManager.isEnableDiscordWebhook() ||
                configManager.getDiscordWebhookUrl() == null ||
                configManager.getDiscordWebhookUrl().equals("YOUR_WEBHOOK_URL_HERE")) {
            return;
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(configManager.getDiscordWebhookUrl());
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setRequestProperty("User-Agent", "MC-Server");
                    connection.setDoOutput(true);
                    String jsonPayload = String.format("{\"content\":\"%s\",\"username\":\"%s\",\"avatar_url\":\"%s\"}",
                            escapeJson(content),
                            escapeJson(configManager.getWebhookUsername()),
                            escapeJson(configManager.getWebhookAvatarUrl())
                    );
                    try (OutputStream os = connection.getOutputStream()) {
                        byte[] input = jsonPayload.getBytes(StandardCharsets.UTF_8);
                        os.write(input, 0, input.length);
                    }
                    int responseCode = connection.getResponseCode();
                    if (responseCode != 204) {
                        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                                connection.getErrorStream(), StandardCharsets.UTF_8))) {
                            StringBuilder response = new StringBuilder();
                            String responseLine;
                            while ((responseLine = br.readLine()) != null) {
                                response.append(responseLine.trim());
                            }
                            plugin.getLogger().warning("Bildirim gitmedi " + response);
                        }
                    }
                    connection.disconnect();

                } catch (IOException e) {
                    plugin.getLogger().warning("Bildirim gitmedi: " + e.getMessage());
                }
            }
        }.runTaskAsynchronously(plugin);
    }

    public void notifyStaff(String message) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission("elitralimiter.notify")) {
                player.sendMessage(ChatColor.RED + "[ElitraLimitleyici] " + ChatColor.WHITE + message);
            }
        }
    }

    /**
     * Escape special characters in a string for JSON
     *
     * @param text The text to escape
     * @return The escaped text
     */
    private String escapeJson(String text) {
        if (text == null) {
            return "";
        }

        return text.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}