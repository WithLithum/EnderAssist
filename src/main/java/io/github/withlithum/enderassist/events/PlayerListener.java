package io.github.withlithum.enderassist.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.*;

public class PlayerListener implements Listener {
    private static final Map<UUID, Long> LastDeath = new HashMap<>();

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        UUID uuid = event.getEntity().getUniqueId();
        long current = System.currentTimeMillis();
        boolean keep = true;
        boolean keepExp = true;

        long last = LastDeath.get(uuid);
        LastDeath.put(uuid, current);

        if (!LastDeath.containsKey(uuid)) {
            // do nothing
            return;
        }

        if (current - last <= 60000) {
            keepExp = false;
        }

        if (current - last <= 120000) {
            keep = false;
        }

        if (keepExp) {
            event.setShouldDropExperience(false);
            event.setKeepLevel(true);
        }

        if (keep) {
            event.setKeepInventory(true);
        }
    }
}
