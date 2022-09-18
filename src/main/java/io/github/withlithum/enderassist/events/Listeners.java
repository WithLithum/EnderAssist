package io.github.withlithum.enderassist.events;

import io.github.withlithum.enderassist.EnderAssist;
import org.bukkit.plugin.PluginManager;

public final class Listeners {
    private Listeners() {}

    public static void register(EnderAssist assist) {
        PluginManager pm = assist.getServer().getPluginManager();

        pm.registerEvents(new PlayerListener(), assist);
        pm.registerEvents(new ServerListener(), assist);
    }
}
