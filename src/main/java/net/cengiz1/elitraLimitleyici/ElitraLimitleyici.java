package net.cengiz1.elitraLimitleyici;

import net.cengiz1.elitraLimitleyici.config.ConfigManager;
import net.cengiz1.elitraLimitleyici.hit.CriticalHitTracker;
import net.cengiz1.elitraLimitleyici.listener.PlayerListener;
import net.cengiz1.elitraLimitleyici.manager.ElytraManager;
import net.cengiz1.elitraLimitleyici.manager.FireworkManager;
import net.cengiz1.elitraLimitleyici.manager.NotificationManager;
import org.bukkit.plugin.java.JavaPlugin;

public class ElitraLimitleyici extends JavaPlugin {

    private ConfigManager configManager;
    private CriticalHitTracker criticalHitTracker;
    private ElytraManager elytraManager;
    private NotificationManager notificationManager;
    private FireworkManager fireworkManager;

    @Override
    public void onEnable() {
        configManager = new ConfigManager(this);
        configManager.loadConfig();
        notificationManager = new NotificationManager(this, configManager);
        elytraManager = new ElytraManager(this, configManager, notificationManager);
        criticalHitTracker = new CriticalHitTracker(this, configManager, elytraManager);
        fireworkManager = new FireworkManager(this, configManager, notificationManager);
        getServer().getPluginManager().registerEvents(
                new PlayerListener(this, criticalHitTracker, fireworkManager), this);
        getLogger().info("ElitraLimitleyici has been enabled!");
        if (configManager.isPreventGhostFireworks()) {
            getLogger().info("Ghost firework prevention is enabled!");
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("ElitraLimitleyici has been disabled!");
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public CriticalHitTracker getCriticalHitTracker() {
        return criticalHitTracker;
    }

    public ElytraManager getElytraManager() {
        return elytraManager;
    }

    public NotificationManager getNotificationManager() {
        return notificationManager;
    }

    public FireworkManager getFireworkManager() {
        return fireworkManager;
    }
}