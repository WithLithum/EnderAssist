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

package io.github.withlithum.enderassist;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class PlayerUtil {
    private PlayerUtil() {}

    private static String infoFormat;
    private static String invalidArgsFormat;
    private static String alertFormat;

    private static FileConfiguration cfg;

    public static String msg(String id) {
        return cfg.getString("messages." + id);
    }

    public static void reload() {
        cfg = JavaPlugin.getPlugin(EnderAssist.class).getConfig();
        infoFormat = cfg.getString("message-format.info", "&a✿ &b%s");
        invalidArgsFormat = cfg.getString("message-format.invalid-args", "&c⚠ &e%1$s: &f&l参数无效: &c%3%s");
        alertFormat = cfg.getString("message-format.alert", "&c⚠ &e%s");
    }

    public static void sendAlert(CommandSender sender, String message) {
        sendFormat(alertFormat, sender, message);
    }

    public static void sendFormat(String format, CommandSender sender, String message) {
        sender.sendMessage(getFormat(format, message));
    }

    public static Component getInfo(String message) {
        return getFormat(infoFormat, message);
    }

    public static Component getFormat(String format, String message) {
        String formatted = String.format(format, message);
        return LegacyComponentSerializer.legacy('&').deserialize(formatted);
    }

    public static void sendInfo(CommandSender sender, String message) {
        sendFormat(infoFormat, sender, message);
    }

    public static void sendInvalidArgs(CommandSender sender, String commandName, String message) {
        String formatted = String.format(invalidArgsFormat, commandName, message);
        Component comp = LegacyComponentSerializer.legacy('&').deserialize(formatted);
        sender.sendMessage(comp);
    }
}
