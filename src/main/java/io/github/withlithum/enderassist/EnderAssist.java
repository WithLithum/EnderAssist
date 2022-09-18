package io.github.withlithum.enderassist;

import io.github.withlithum.enderassist.commands.Commands;
import io.github.withlithum.enderassist.events.Listeners;
import io.github.withlithum.enderassist.io.PlayerProfileManager;
import io.github.withlithum.enderassist.support.SyncTask;
import org.bukkit.plugin.java.JavaPlugin;

public final class EnderAssist extends JavaPlugin {

    private SyncTask syncTask;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();

        PlayerUtil.reload();
        PlayerProfileManager.init(this);
        Commands.register(this);

        syncTask = createSyncTask();
        Listeners.register(this, syncTask);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        PlayerProfileManager.shutdown();

        if (syncTask != null) {
            syncTask.cancel();
            syncTask = null;
        }
    }

    private SyncTask createSyncTask() {
        return new SyncTask(this);
    }
}
