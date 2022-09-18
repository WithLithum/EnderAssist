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

package io.github.withlithum.enderassist.commands;

import io.github.withlithum.enderassist.PlayerUtil;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BedCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length != 0) {
            PlayerUtil.sendInvalidArgs(sender, label, "不应有参数");
            return false;
        }

        if (!(sender instanceof Player)) {
            PlayerUtil.sendAlert(sender, "此命令只能由玩家执行");
            return false;
        }

        Player player = (Player)sender;
        Location spawn = player.getBedSpawnLocation();
        if (spawn == null) {
            PlayerUtil.sendAlert(sender, PlayerUtil.msg("spawn_not_valid"));
            return true;
        }

        player.teleport(spawn);

        return true;
    }
}
