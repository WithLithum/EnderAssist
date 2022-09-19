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
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.*;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.raid.RaidTriggerEvent;

import java.util.*;

public class PlayerListener implements Listener {
    private static final Map<UUID, Long> LastDeath = new HashMap<>();

    @EventHandler
    public void onTriggerRaid(RaidTriggerEvent event) {
        Bukkit.getServer().broadcast(PlayerUtil.getInfo(String.format(PlayerUtil.msg("raid_msg"),
                event.getPlayer().getName())));
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null) {
            return;
        }

        BlockData data = event.getClickedBlock().getBlockData();
        if ((data.getMaterial() == Material.WHEAT || data.getMaterial() == Material.POTATOES
            || data.getMaterial() == Material.CARROTS || data.getMaterial() == Material.BEETROOTS)
        && data instanceof Ageable ageable && ageable.getAge() == 7){
            Location location = event.getClickedBlock().getLocation();
            World world = event.getClickedBlock().getWorld();
            var drop = event.getClickedBlock().getDrops();
            ageable.setAge(0);
            event.getClickedBlock().setBlockData(ageable);

            for (var dr : drop) {
                if (PlayerUtil.isSeed(dr.getType())) {
                    dr.setAmount(dr.getAmount() - 1);
                }

                world.dropItem(location, dr);
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.getPlayer().sendPlayerListHeader(Component.text("---> ").color(NamedTextColor.AQUA)
                .append(Component.text("EnderServ").color(NamedTextColor.WHITE).style(Style.style()
                        .decoration(TextDecoration.BOLD, true).build()))
                .append(Component.text(" <---").decoration(TextDecoration.BOLD, false)
                        .color(NamedTextColor.AQUA)));

        event.getPlayer().sendPlayerListFooter(Component.text("---").color(NamedTextColor.WHITE)
                .append(Component.text(" ms").color(NamedTextColor.AQUA)));
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
