package io.github.withlithum.enderassist.events;

import io.github.withlithum.enderassist.EnderAssist;
import io.github.withlithum.enderassist.support.SyncTask;
import org.bukkit.plugin.PluginManager;

public final class Listeners {
    private Listeners() {}

    public static void register(EnderAssist assist, SyncTask task) {
        PluginManager pm = assist.getServer().getPluginManager();

        pm.registerEvents(new PlayerListener(), assist);
        pm.registerEvents(new ServerListener(), assist);
        pm.registerEvents(new SyncTaskListener(task), assist);
    }
}
