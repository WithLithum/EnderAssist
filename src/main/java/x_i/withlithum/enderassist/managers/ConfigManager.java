package x_i.withlithum.enderassist.managers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import x_i.withlithum.enderassist.EnderAssist;

public class ConfigManager {
    private static ConfigManager instance;
    private final EnderAssist assist;
    private FileConfiguration configuration;
    private static final Logger LOGGER = LogManager.getLogger("EA-CfgManager");

    public static ConfigManager getInstance() {
        return instance;
    }

    public static void create(EnderAssist assist) {
        if (instance != null) {
            instance = new ConfigManager(assist);
        } else {
            throw new IllegalStateException("Already created!");
        }
    }

    public ConfigManager(EnderAssist assist) {
        this.assist = assist;

        configuration = assist.getConfig();
    }

    public void reload() {
        LOGGER.info("重新载入配置...");
        assist.reloadConfig();
        configuration = assist.getConfig();
    }

    public Configuration configuration() {
        return configuration;
    }
}
