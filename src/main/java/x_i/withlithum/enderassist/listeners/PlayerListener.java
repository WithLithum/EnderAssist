package x_i.withlithum.enderassist.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import x_i.withlithum.enderassist.Game;

public class PlayerListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Game.getAssist().getSyncTask().refresh(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        var lastSeen = System.currentTimeMillis();
        var player = Game.fromBukkit(event.getPlayer());

        var uuid = player.getUUID();
        var profile = Game.getAssist().profiles().get(uuid);
        profile.lastSeen(lastSeen);
        Game.getAssist().profiles().put(uuid, profile);

        Game.getAssist().getSyncTask().refresh(event.getPlayer());
    }
}
