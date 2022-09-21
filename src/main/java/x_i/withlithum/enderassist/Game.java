package x_i.withlithum.enderassist;

import net.minecraft.server.dedicated.DedicatedServer;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_17_R1.CraftServer;
import org.bukkit.plugin.java.JavaPlugin;
import x_i.withlithum.enderassist.managers.ConfigManager;
import x_i.withlithum.enderassist.managers.MessageManager;

public final class Game {
    private Game() {}
    private static EnderAssist assist;

    public static DedicatedServer getServer() {
        return ((CraftServer) Bukkit.getServer()).getServer();
    }

    public static EnderAssist getAssist() {
        if (assist == null) {
            assist = JavaPlugin.getPlugin(EnderAssist.class);
        }

        return assist;
    }

    public static MessageManager messages() {
        return getAssist().msgManager();
    }
}
