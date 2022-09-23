package x_i.withlithum.enderassist;

import org.bukkit.plugin.java.JavaPlugin;
import x_i.withlithum.enderassist.command.Commands;
import x_i.withlithum.enderassist.listeners.Listeners;
import x_i.withlithum.enderassist.managers.ConfigManager;
import x_i.withlithum.enderassist.managers.MessageManager;
import x_i.withlithum.enderassist.managers.profiles.ProfileManager;
import x_i.withlithum.enderassist.support.PingUpdateTask;
import x_i.withlithum.enderassist.support.SyncTask;

/**
 * 定义本插件的入口点。
 */
public class EnderAssist extends JavaPlugin {
    private MessageManager messageManager;
    private SyncTask task;
    private PingUpdateTask update;
    private ProfileManager profileManager;

    public SyncTask getSyncTask() {
        return task;
    }

    public ProfileManager profiles() {
        return profileManager;
    }

    @Override
    public void onEnable() {
        getLog4JLogger().info("Instantiated");
        ConfigManager.create(this);
        profileManager = new ProfileManager(this);
        profileManager.init();

        Commands.init();

        messageManager = new MessageManager(this.getDataFolder());
        messageManager.init();

        var messenger = getServer().getMessenger();
        messenger.registerOutgoingPluginChannel(this, SyncTask.SATURATION_KEY);
        messenger.registerOutgoingPluginChannel(this, SyncTask.EXHAUSTION_KEY);

        task = new SyncTask(this);
        task.runTaskTimer(this, 0, 1);

        update = new PingUpdateTask();
        update.runTaskTimer(this, 0, 3000);

        Listeners.init(this);
    }

    @Override
    public void onDisable() {
        profileManager.saveAll();

        if (task != null) {
            task.cancel();
            task = null;
        }

        if (update != null) {
            update.cancel();
            update = null;
        }
    }

    public MessageManager msgManager() {
        return messageManager;
    }
}