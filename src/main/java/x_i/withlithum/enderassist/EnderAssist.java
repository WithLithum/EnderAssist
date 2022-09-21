package x_i.withlithum.enderassist;

import org.apache.logging.log4j.Logger;
import org.bukkit.plugin.java.JavaPlugin;
import x_i.withlithum.enderassist.command.Commands;
import x_i.withlithum.enderassist.managers.ConfigManager;
import x_i.withlithum.enderassist.managers.MessageManager;

import java.io.File;

/**
 * 定义本插件的入口点。
 */
public class EnderAssist extends JavaPlugin {
    private Logger logger;
    private MessageManager messageManager;

    @Override
    public void onEnable() {
        logger = getLog4JLogger();

        logger.info("Instantiated");
        ConfigManager.create(this);
        Commands.init();

        messageManager = new MessageManager(this.getDataFolder());
        messageManager.init();
    }

    public MessageManager msgManager() {
        return messageManager;
    }
}