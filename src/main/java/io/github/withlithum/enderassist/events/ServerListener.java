package io.github.withlithum.enderassist.events;

import io.github.withlithum.enderassist.io.PlayerProfileManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldSaveEvent;

public class ServerListener implements Listener {
    @EventHandler
    public void onServerSave(WorldSaveEvent event) {
        PlayerProfileManager.save();
    }
}
