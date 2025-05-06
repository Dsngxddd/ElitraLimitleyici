package net.cengiz1.elitraLimitleyici.manager;

import net.cengiz1.elitraLimitleyici.ElitraLimitleyici;
import net.cengiz1.elitraLimitleyici.config.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ElytraManager {

    private final ElitraLimitleyici plugin;
    private final ConfigManager configManager;
    private final NotificationManager notificationManager;
    private final Map<UUID, Boolean> disabledElytraPlayers = new HashMap<>();

    public ElytraManager(ElitraLimitleyici plugin, ConfigManager configManager, NotificationManager notificationManager) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.notificationManager = notificationManager;
    }

    /**
     * Disable a player's elytra for the configured duration
     *
     * @param player The player whose elytra should be disabled
     */
    public void disableElytra(Player player) {
        UUID playerUUID = player.getUniqueId();
        disabledElytraPlayers.put(playerUUID, true);
        player.setGliding(false);
        if (configManager.isEnableNotifications()) {
            plugin.getLogger().info(configManager.getNotificationMessage().replace("%player%", player.getName()));
            if (configManager.isEnableDiscordWebhook()) {
                String message = configManager.getNotificationMessage().replace("%player%", player.getName());
                notificationManager.sendDiscordWebhook(message);
            }
            if (configManager.isEnablePlayerMessage()) {
                String message = ChatColor.translateAlternateColorCodes('&',
                        configManager.getPlayerNotificationMessage()
                                .replace("%seconds%", String.valueOf(configManager.getDisableDurationSeconds())));
                player.sendMessage(message);
            }
            if (configManager.isEnableNotifications()) {
                String notifyMessage = configManager.getNotificationMessage().replace("%player%", player.getName());
                plugin.getLogger().info(notifyMessage);
                notificationManager.notifyStaff(notifyMessage);
            }
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                enableElytra(playerUUID);
                Player onlinePlayer = Bukkit.getPlayer(playerUUID);
                if (onlinePlayer != null && onlinePlayer.isOnline() && configManager.isEnablePlayerMessage()) {
                    onlinePlayer.sendMessage(ChatColor.GREEN + "Elitra aktif");
                }
            }
        }.runTaskLater(plugin, configManager.getDisableDurationSeconds() * 20L);
    }

    /**
     * Re-enable a player's elytra
     *
     * @param playerUUID The UUID of the player
     */
    public void enableElytra(UUID playerUUID) {
        disabledElytraPlayers.remove(playerUUID);
        plugin.getCriticalHitTracker().clearCriticalHits(playerUUID);
    }

    /**
     * Check if a player's elytra is currently disabled
     *
     * @param playerUUID The UUID of the player
     * @return true if the player's elytra is disabled
     */
    public boolean isElytraDisabled(UUID playerUUID) {
        return disabledElytraPlayers.getOrDefault(playerUUID, false);
    }
}