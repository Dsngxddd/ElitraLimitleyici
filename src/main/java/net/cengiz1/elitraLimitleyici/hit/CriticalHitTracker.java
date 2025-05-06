package net.cengiz1.elitraLimitleyici.hit;

import net.cengiz1.elitraLimitleyici.ElitraLimitleyici;
import net.cengiz1.elitraLimitleyici.config.ConfigManager;
import net.cengiz1.elitraLimitleyici.manager.ElytraManager;
import org.bukkit.entity.Player;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.UUID;

public class CriticalHitTracker {

    private final ElitraLimitleyici plugin;
    private final ConfigManager configManager;
    private final ElytraManager elytraManager;
    private final Map<UUID, LinkedList<Instant>> playerCritHits = new HashMap<>();

    public CriticalHitTracker(ElitraLimitleyici plugin, ConfigManager configManager, ElytraManager elytraManager) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.elytraManager = elytraManager;
    }

    /**
     * Record a critical hit for a player and check if they've exceeded the limit
     *
     * @param player The player who made the critical hit
     * @return true if the player has exceeded the critical hit limit
     */
    public boolean recordCriticalHit(Player player) {
        UUID playerUUID = player.getUniqueId();
        if (player.hasPermission("elitralimiter.bypass")) {
            return false;
        }
        if (elytraManager.isElytraDisabled(playerUUID)) {
            return false;
        }
        LinkedList<Instant> critHits = playerCritHits.computeIfAbsent(playerUUID, k -> new LinkedList<>());
        critHits.add(Instant.now());
        Instant cutoff = Instant.now().minus(configManager.getTimeWindowSeconds(), ChronoUnit.SECONDS);
        critHits.removeIf(timestamp -> timestamp.isBefore(cutoff));
        if (critHits.size() >= configManager.getMaxCriticalHits()) {
            critHits.clear();
            return true;
        }

        return false;
    }

    /**
     * Clear critical hit records for a player
     *
     * @param playerUUID The UUID of the player
     */
    public void clearCriticalHits(UUID playerUUID) {
        playerCritHits.remove(playerUUID);
    }

    /**
     * Checks if a hit is a critical hit in Minecraft
     *
     * @param player The player
     * @return true if the conditions for a critical hit are met
     */
    public boolean isCriticalHit(Player player) {
        return player.getFallDistance() > 0.0F &&
                !player.isOnGround() &&
                !player.isInsideVehicle() &&
                !player.isSwimming() &&
                !player.isClimbing();
    }
}