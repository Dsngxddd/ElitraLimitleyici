package net.cengiz1.elitraLimitleyici.listener;

import net.cengiz1.elitraLimitleyici.ElitraLimitleyici;
import net.cengiz1.elitraLimitleyici.hit.CriticalHitTracker;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityToggleGlideEvent;

public class PlayerListener implements Listener {

    private final ElitraLimitleyici plugin;
    private final CriticalHitTracker criticalHitTracker;

    public PlayerListener(ElitraLimitleyici plugin, CriticalHitTracker criticalHitTracker) {
        this.plugin = plugin;
        this.criticalHitTracker = criticalHitTracker;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getDamager();
        if (!player.isGliding()) {
            return;
        }if (criticalHitTracker.isCriticalHit(player)) {
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

}