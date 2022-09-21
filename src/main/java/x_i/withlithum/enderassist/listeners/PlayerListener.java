package x_i.withlithum.enderassist.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import x_i.withlithum.enderassist.Game;
import x_i.withlithum.enderassist.support.SyncTask;

public class PlayerListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Game.getAssist().getSyncTask().refresh(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Game.getAssist().getSyncTask().refresh(event.getPlayer());
    }
}
