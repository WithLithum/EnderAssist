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
        String formatted = String.format(format, message);
        Component comp = LegacyComponentSerializer.legacy('&').deserialize(formatted);

        sender.sendMessage(comp);
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
