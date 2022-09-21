package x_i.withlithum.enderassist.listeners;

import x_i.withlithum.enderassist.EnderAssist;

public final class Listeners {
    private Listeners() {}

    public static void init(EnderAssist assist) {
        var manager = assist.getServer().getPluginManager();

        manager.registerEvents(new EntityListener(), assist);
        manager.registerEvents(new WorldListener(), assist);
        manager.registerEvents(new PlayerListener(), assist);
    }
}
