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

import io.github.withlithum.enderassist.EnderAssist;
import io.github.withlithum.enderassist.PlayerUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class MetaCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length != 1) {
            PlayerUtil.sendInvalidArgs(sender, label,
                    "期望 'version' 或 'reload'");
            return false;
        }

        if (args[0].equals("reload")) {
            JavaPlugin.getPlugin(EnderAssist.class).reloadConfig();
            PlayerUtil.reload();

            PlayerUtil.sendInfo(sender, "已重新载入配置文件！");
            return true;
        } else if (args[0].equals("version")) {
            PluginDescriptionFile descriptionFile = JavaPlugin.getPlugin(EnderAssist.class).getDescription();
            PlayerUtil.sendInfo(sender, "版本为: " + descriptionFile.getVersion());
            return true;
        } else {
            PlayerUtil.sendInvalidArgs(sender, label,
                    "期望 'version' 或 'reload'，但找到了 " + args[0]);
            return false;
        }
    }
}
