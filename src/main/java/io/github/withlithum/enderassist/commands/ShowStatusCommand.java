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
import io.github.withlithum.enderassist.io.PlayerProfile;
import io.github.withlithum.enderassist.io.PlayerProfileManager;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ShowStatusCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!Commands.expectEmpty(args, sender, label)) return false;
        if (!(sender instanceof Player)) {
            PlayerUtil.sendAlert(sender, "此命令只能由玩家执行");
            return false;
        }
        UUID uuid = ((Player) sender).getUniqueId();

        PlayerProfile profile = PlayerProfileManager.get(uuid);
        profile.showStatus(!profile.showStatus());

        Component result = PlayerUtil.getInfo(PlayerUtil.msg("switch-status")).append(Commands.onOff(profile.showStatus()));
        sender.sendMessage(result);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return new ArrayList<>();
    }
}
