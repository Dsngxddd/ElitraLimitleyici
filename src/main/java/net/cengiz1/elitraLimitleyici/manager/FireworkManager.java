package net.cengiz1.elitraLimitleyici.manager;

import net.cengiz1.elitraLimitleyici.ElitraLimitleyici;
import net.cengiz1.elitraLimitleyici.config.ConfigManager;
import net.cengiz1.elitraLimitleyici.event.GhostFireworkAttemptEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FireworkManager {

    private final ElitraLimitleyici plugin;
    private final ConfigManager configManager;
    private final NotificationManager notificationManager;
    private final Map<UUID, Integer> playerWarnings = new HashMap<>();
    private final Map<UUID, Long> lastWarningTime = new HashMap<>();

    public FireworkManager(ElitraLimitleyici plugin, ConfigManager configManager, NotificationManager notificationManager) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.notificationManager = notificationManager;
    }

    /**
     * Check if a player has a firework rocket in either hand
     *
     * @param player The player to check
     * @return true if the player has a firework rocket in hand
     */
    public boolean hasFireworkInHand(Player player) {
        ItemStack mainHand = player.getInventory().getItemInMainHand();
        ItemStack offHand = player.getInventory().getItemInOffHand();

        return mainHand.getType() == Material.FIREWORK_ROCKET ||
                offHand.getType() == Material.FIREWORK_ROCKET;
    }

    /**
     * Record a ghost firework attempt and take action if necessary
     *
     * @param player The player who attempted to use a ghost firework
     */
    public void handleGhostFireworkAttempt(Player player) {
        UUID playerUUID = player.getUniqueId();
        if (player.hasPermission("elitralimiter.bypass")) {
            return;
        }
        long currentTime = System.currentTimeMillis();
        long lastTime = lastWarningTime.getOrDefault(playerUUID, 0L);
        if (currentTime - lastTime < configManager.getGhostFireworkCooldownMs()) {
            return;
        }
        lastWarningTime.put(playerUUID, currentTime);
        int warnings = playerWarnings.getOrDefault(playerUUID, 0) + 1;
        playerWarnings.put(playerUUID, warnings);
        GhostFireworkAttemptEvent event = new GhostFireworkAttemptEvent(player, warnings);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            return;
        }
        if (configManager.isEnablePlayerMessage()) {
            String message = ChatColor.translateAlternateColorCodes('&',
                    configManager.getGhostFireworkPlayerMessage()
                            .replace("%count%", String.valueOf(warnings))
                            .replace("%max%", String.valueOf(configManager.getMaxGhostFireworkWarnings())));
            player.sendMessage(message);
        }
        String staffMessage = configManager.getGhostFireworkNotificationMessage()
                .replace("%player%", player.getName())
                .replace("%count%", String.valueOf(warnings))
                .replace("%max%", String.valueOf(configManager.getMaxGhostFireworkWarnings()));

        if (configManager.isEnableNotifications()) {
            plugin.getLogger().info(staffMessage);
            notificationManager.notifyStaff(staffMessage);

            if (configManager.isEnableDiscordWebhook()) {
                notificationManager.sendDiscordWebhook(staffMessage);
            }
        }
        if (warnings >= configManager.getMaxGhostFireworkWarnings()) {
            applyPunishment(player);
            resetWarnings(playerUUID);
        }
    }

    /**
     * Apply punishment to a player who exceeded the warning threshold
     *
     * @param player The player to punish
     */
    private void applyPunishment(Player player) {
        UUID playerUUID = player.getUniqueId();
        if (configManager.isGhostFireworkDisablesElytra()) {
            plugin.getElytraManager().disableElytra(player);
        }
        for (String command : configManager.getGhostFireworkPunishmentCommands()) {
            String formattedCommand = command.replace("%player%", player.getName());
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), formattedCommand);
        }
        if (configManager.isEnablePlayerMessage()) {
            String message = ChatColor.translateAlternateColorCodes('&',
                    configManager.getGhostFireworkPunishmentMessage());
            player.sendMessage(message);
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                resetWarnings(playerUUID);
            }
        }.runTaskLater(plugin, configManager.getGhostFireworkPunishmentDurationTicks());
    }

    /**
     * Reset warnings for a player
     *
     * @param playerUUID The UUID of the player
     */
    public void resetWarnings(UUID playerUUID) {
        playerWarnings.remove(playerUUID);
        lastWarningTime.remove(playerUUID);
    }

    /**
     * Get the number of warnings for a player
     *
     * @param playerUUID The UUID of the player
     * @return The number of warnings
     */
    public int getWarningCount(UUID playerUUID) {
        return playerWarnings.getOrDefault(playerUUID, 0);
    }
}