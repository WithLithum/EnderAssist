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
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;

import java.util.Objects;

public final class Commands {
    private Commands() {}
    private static EnderAssist assist;

    public static void register(EnderAssist assist)
    {
        Commands.assist = assist;
        get("bed").setExecutor(new BedCommand());
        get("enderassist").setExecutor(new MetaCommand());
        get("tps").setExecutor(new TpsCommand());
    }

    public static PluginCommand get(String name)
    {
        return Objects.requireNonNull(assist.getCommand(name));
    }

    public static boolean expectEmpty(String[] args, CommandSender sender, String cmdName) {
        if (args.length != 0) {
            PlayerUtil.sendInvalidArgs(sender, cmdName, "期望无参数");
            return false;
        }

        return true;
    }
}
