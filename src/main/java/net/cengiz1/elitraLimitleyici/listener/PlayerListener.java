package net.cengiz1.elitraLimitleyici.listener;

import net.cengiz1.elitraLimitleyici.ElitraLimitleyici;
import net.cengiz1.elitraLimitleyici.hit.CriticalHitTracker;
import net.cengiz1.elitraLimitleyici.manager.FireworkManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    private final ElitraLimitleyici plugin;
    private final CriticalHitTracker criticalHitTracker;
    private final FireworkManager fireworkManager;

    public PlayerListener(ElitraLimitleyici plugin, CriticalHitTracker criticalHitTracker, FireworkManager fireworkManager) {
        this.plugin = plugin;
        this.criticalHitTracker = criticalHitTracker;
        this.fireworkManager = fireworkManager;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getDamager();
        if (!player.isGliding()) {
            return;
        }
        if (criticalHitTracker.isCriticalHit(player)) {
            if (criticalHitTracker.recordCriticalHit(player)) {
                plugin.getElytraManager().disableElytra(player);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerToggleGlide(EntityToggleGlideEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getEntity();
        if (event.isGliding() && plugin.getElytraManager().isElytraDisabled(player.getUniqueId())) {
            event.setCancelled(true);
            if (plugin.getConfigManager().isEnablePlayerMessage()) {
                String message = ChatColor.translateAlternateColorCodes('&',
                        plugin.getConfigManager().getPlayerNotificationMessage()
                                .replace("%seconds%", String.valueOf(plugin.getConfigManager().getDisableDurationSeconds())));
                player.sendMessage(message);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (plugin.getConfigManager().isPreventGhostFireworks()) {
            fireworkManager.resetWarnings(event.getPlayer().getUniqueId());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (plugin.getConfigManager().isPreventGhostFireworks()) {
            fireworkManager.resetWarnings(event.getPlayer().getUniqueId());
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!plugin.getConfigManager().isPreventGhostFireworks()) {
            return;
        }
        Player player = event.getPlayer();
        if (player.isGliding() && event.getAction().name().contains("RIGHT_CLICK")) {
            if (!fireworkManager.hasFireworkInHand(player)) {
                event.setCancelled(true);
                fireworkManager.handleGhostFireworkAttempt(player);
            }
        }
    }
}