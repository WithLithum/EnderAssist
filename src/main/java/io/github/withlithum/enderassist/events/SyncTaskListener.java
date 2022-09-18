package io.github.withlithum.enderassist.events;

import io.github.withlithum.enderassist.support.SyncTask;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class SyncTaskListener implements Listener {
    private final SyncTask task;

    public SyncTaskListener(SyncTask task) {
        this.task = task;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        task.onPlayerLogIn(event.getPlayer());
    }

    public void onPlayerLeave(PlayerQuitEvent event) {
        task.onPlayerLogOut(event.getPlayer());
    }
}
