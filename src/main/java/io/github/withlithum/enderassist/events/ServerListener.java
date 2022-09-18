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

import io.github.withlithum.enderassist.PlayerUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Raid;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.raid.RaidFinishEvent;
import org.bukkit.event.raid.RaidSpawnWaveEvent;
import org.bukkit.event.raid.RaidStopEvent;

import java.util.Collection;
import java.util.List;

public class ServerListener implements Listener {
    @EventHandler
    public void onRaidEnd(RaidFinishEvent event) {
        if (event.getRaid().getStatus() != Raid.RaidStatus.VICTORY) {
            return;
        }

        Component component;
        List<Player> winners = event.getWinners();
        Location loc = event.getRaid().getLocation();

        if (winners.isEmpty())
        {
            component = PlayerUtil.getInfo(String.format(PlayerUtil.msg("raid_end_win"),
                    loc.getBlockX(),
                    loc.getBlockY(),
                    loc.getBlockZ()));
        }
        else
        {
            component = PlayerUtil.getInfo(String.format(PlayerUtil.msg("raid_end_win_pl"),
                    winners.get(0),
                    loc.getBlockX(),
                    loc.getBlockY(),
                    loc.getBlockZ()));
        }

        Bukkit.getServer().broadcast(component);
    }

    @EventHandler
    public void onRaidStop(RaidStopEvent event) {
        if (event.getRaid().getStatus() == Raid.RaidStatus.LOSS) {
            Location loc = event.getRaid().getLocation();

            Bukkit.getServer().broadcast(PlayerUtil.getInfo(String.format(PlayerUtil.msg("raid_end_fail"),
                    loc.getBlockX(),
                    loc.getBlockY(),
                    loc.getBlockZ())));
        }
    }

    @EventHandler
    public void onRaidWave(RaidSpawnWaveEvent event) {
        Collection<Player> players = event.getRaid().getLocation().getNearbyPlayers(96);
        Raid raid = event.getRaid();

        for (Player player : players) {
            PlayerUtil.sendInfo(player, String.format(PlayerUtil.msg("raid_update"),
                    raid.getLocation().getBlockX(),
                    raid.getLocation().getBlockY(),
                    raid.getLocation().getBlockZ(),
                    raid.getSpawnedGroups(),
                    raid.getTotalGroups()));
        }
    }
}
