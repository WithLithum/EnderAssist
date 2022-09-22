package x_i.withlithum.enderassist;

import org.bukkit.plugin.java.JavaPlugin;
import x_i.withlithum.enderassist.command.Commands;
import x_i.withlithum.enderassist.listeners.Listeners;
import x_i.withlithum.enderassist.managers.ConfigManager;
import x_i.withlithum.enderassist.managers.MessageManager;
import x_i.withlithum.enderassist.support.SyncTask;

/**
 * 定义本插件的入口点。
 */
public class EnderAssist extends JavaPlugin {
    private MessageManager messageManager;
    private SyncTask task;

    public SyncTask getSyncTask() {
        return task;
    }

    @Override
    public void onEnable() {
        getLog4JLogger().info("Instantiated");
        ConfigManager.create(this);
        Commands.init();

        messageManager = new MessageManager(this.getDataFolder());
        messageManager.init();

        var messenger = getServer().getMessenger();
        messenger.registerOutgoingPluginChannel(this, SyncTask.SATURATION_KEY);
        messenger.registerOutgoingPluginChannel(this, SyncTask.EXHAUSTION_KEY);

        task = new SyncTask(this);
        task.runTaskTimer(this, 0, 1);

        Listeners.init(this);
    }

    public MessageManager msgManager() {
        return messageManager;
    }
}