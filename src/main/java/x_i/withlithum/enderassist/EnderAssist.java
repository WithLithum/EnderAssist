package x_i.withlithum.enderassist;

import org.apache.logging.log4j.Logger;
import org.bukkit.plugin.java.JavaPlugin;
import x_i.withlithum.enderassist.command.Commands;

/**
 * 定义本插件的入口点。
 */
public class EnderAssist extends JavaPlugin {
    private Logger logger;

    @Override
    public void onEnable() {
        logger = getLog4JLogger();

        logger.info("Instantiated");
        Commands.init();
    }
}