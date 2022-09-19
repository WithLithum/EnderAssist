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

package io.github.withlithum.enderassist.support;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

@SuppressWarnings("unused")
public class PingUpdateTask extends BukkitRunnable {

    @Override
    public void run() {
        for (var player : Bukkit.getServer().getOnlinePlayers()) {
            player.sendPlayerListFooter(Component.text(player.getPing()).color(NamedTextColor.WHITE)
                    .append(Component.text(" ms").color(NamedTextColor.AQUA)));
        }
    }
}
