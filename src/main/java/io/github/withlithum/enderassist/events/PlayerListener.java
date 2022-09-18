/*
 * Copyright (C) 2022 WithLithum.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.withlithum.enderassist.events;

import io.github.withlithum.enderassist.io.PlayerProfileManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.*;

public class PlayerListener implements Listener {
    private static final Map<UUID, Long> LastDeath = new HashMap<>();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        PlayerProfileManager.ensure(event.getPlayer().getUniqueId());
    }

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
