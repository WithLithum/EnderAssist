package x_i.withlithum.enderassist.listeners;

import org.bukkit.Location;
import org.bukkit.Raid;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.raid.RaidFinishEvent;
import org.bukkit.event.raid.RaidSpawnWaveEvent;
import org.bukkit.event.raid.RaidStopEvent;
import x_i.withlithum.enderassist.Game;

import java.util.Collection;

public class WorldListener implements Listener {
    @EventHandler
    public void onRaidEnd(RaidFinishEvent event) {
        if (event.getRaid().getStatus() != Raid.RaidStatus.VICTORY) {
            return;
        }

        Location loc = event.getRaid().getLocation();

        Game.broadcastMsg(Game.messages().getRaw("events.raid.win"), loc.getBlockX(),
                loc.getBlockY(),
                loc.getBlockZ());
    }

    @EventHandler
    public void onRaidStop(RaidStopEvent event) {
        if (event.getRaid().getStatus() == Raid.RaidStatus.LOSS) {
            Location loc = event.getRaid().getLocation();

            Game.broadcastMsg(Game.messages().getRaw("events.raid.lost"),
                    loc.getX(),
                    loc.getY(),
                    loc.getZ());
        }
    }

    @EventHandler
    public void onRaidWave(RaidSpawnWaveEvent event) {
        Collection<Player> players = event.getRaid().getLocation().getNearbyPlayers(96);
        Raid raid = event.getRaid();

        for (Player player : players) {
            var pl = Game.fromBukkit(player);
            Game.sendMsg(pl, Game.messages().getRaw("events.raid.update"),
                    raid.getSpawnedGroups(),
                    raid.getTotalGroups());
        }
    }

}
