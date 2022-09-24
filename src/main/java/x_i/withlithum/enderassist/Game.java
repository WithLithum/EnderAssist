package x_i.withlithum.enderassist;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_17_R1.CraftServer;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_17_R1.block.CraftBlock;
import org.bukkit.craftbukkit.v1_17_R1.entity.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import x_i.withlithum.enderassist.managers.MessageManager;

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

    public static void broadcast(Component message) {
        getServer().getPlayerList().broadcastMessage(message, ChatType.SYSTEM, ZERO_UUID);
    }

    public static Component formatMsg(String message, Object @NotNull ... values) {
        return Component.Serializer.fromJson(String.format(message, values));
    }

    public static void sendMsg(@NotNull Player player, String message, Object @NotNull ... values) {
        var component = formatMsg(message, values);

        player.sendMessage(component, ZERO_UUID);
    }

    public static void broadcastMsg(String message, Object @NotNull ... values) {
        var component = formatMsg(message, values);

        getServer().getPlayerList().broadcastMessage(component, ChatType.SYSTEM, ZERO_UUID);
    }

    public static Entity fromBukkitEntity(org.bukkit.entity.Entity source) {
        return ((CraftEntity) source).getHandle();
    }

    public static BlockState fromBukkitBlock(Block block) {
        return ((CraftBlock) block).getNMS();
    }

    public static Player fromBukkit(org.bukkit.entity.Player source) {
        return ((CraftPlayer) source).getHandle();
    }

    public static ServerLevel fromBukkit(World world) {
        return ((CraftWorld) world).getHandle();
    }

    public static BlockPos fromBukkit(Location location) {
        return new BlockPos(location.getX(), location.getY(), location.getZ());
    }

    public static MessageManager messages() {
        return getAssist().msgManager();
    }
}
