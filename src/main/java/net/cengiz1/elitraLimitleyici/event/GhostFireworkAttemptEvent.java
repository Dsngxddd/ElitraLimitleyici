package net.cengiz1.elitraLimitleyici.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GhostFireworkAttemptEvent extends Event implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();
    private final Player player;
    private final int warningCount;
    private boolean cancelled;

    public GhostFireworkAttemptEvent(Player player, int warningCount) {
        this.player = player;
        this.warningCount = warningCount;
        this.cancelled = false;
    }

    /**
     * Get the player who attempted to use a ghost firework
     *
     * @return The player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Get the current warning count for this player
     *
     * @return The warning count
     */
    public int getWarningCount() {
        return warningCount;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}