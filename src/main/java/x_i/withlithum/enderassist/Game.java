package x_i.withlithum.enderassist;

import net.minecraft.commands.CommandSource;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_17_R1.CraftServer;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftProjectile;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import x_i.withlithum.enderassist.managers.ConfigManager;
import x_i.withlithum.enderassist.managers.MessageManager;

import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;

public final class Game {
    private Game() {}
    private static EnderAssist assist;

    private static final UUID ZERO_UUID = UUID.fromString("00000000-0000-0000-0000-000000000000");

    public static DedicatedServer getServer() {
        return ((CraftServer) Bukkit.getServer()).getServer();
    }

    public static EnderAssist getAssist() {
        if (assist == null) {
            assist = JavaPlugin.getPlugin(EnderAssist.class);
        }

        return assist;
    }

    public static void broadcastTrader(@NotNull Entity entity) {
        var component = new TextComponent(String.format(messages().getRaw("events.trader_spawn"),
                entity.getX(),
                entity.getY(),
                entity.getZ()));

        getServer().getPlayerList().broadcastMessage(component, ChatType.SYSTEM, entity.getUUID());
    }

    public static void broadcast(Component message) {
        getServer().getPlayerList().broadcastMessage(message, ChatType.SYSTEM, ZERO_UUID);
    }

    public static void broadcastPatrol(@NotNull Entity entity) {
        var component = new TextComponent(String.format(messages().getRaw("events.patrol_spawn"),
                entity.getX(),
                entity.getY(),
                entity.getZ()));

        getServer().getPlayerList().broadcastMessage(component, ChatType.SYSTEM, entity.getUUID());
    }

    public static void broadcastMsg(String message, double x, double y, double z) {
        var component = new TextComponent(String.format(message,
                x,
                y,
                z));

        getServer().getPlayerList().broadcastMessage(component, ChatType.SYSTEM, ZERO_UUID);
    }

    public static Component formatMsg(String message, Object @NotNull ... values) {
        var vl = values.clone();
        return new TextComponent(String.format(message, vl));
    }

    public static void sendMsg(@NotNull Player player, String message, Object @NotNull ... values) {
        var component = formatMsg(message, values);

        player.sendMessage(component, ZERO_UUID);
    }

    public static void broadcastMsg(String message, Object @NotNull ... values) {
        var vl = values.clone();
        var component = new TextComponent(String.format(message, vl));

        getServer().getPlayerList().broadcastMessage(component, ChatType.SYSTEM, ZERO_UUID);
    }

    public static Entity fromBukkitEntity(org.bukkit.entity.Entity source) {
        return ((CraftEntity) source).getHandle();
    }

    public static Player fromBukkit(org.bukkit.entity.Player source) {
        return ((CraftPlayer) source).getHandle();
    }

    public static Projectile fromBukkit(org.bukkit.entity.Projectile source) {
        return ((CraftProjectile) source).getHandle();
    }

    public static MessageManager messages() {
        return getAssist().msgManager();
    }
}
