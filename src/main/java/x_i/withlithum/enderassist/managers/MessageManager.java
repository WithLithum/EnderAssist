package x_i.withlithum.enderassist.managers;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import x_i.withlithum.enderassist.Game;

import java.io.File;
import java.io.IOException;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class MessageManager {
    private static final Logger LOGGER = LogManager.getLogger("EA-MsgManager");
    private final File messagesFile;
    private final YamlConfiguration configFile;
    private static final String CONFIG_FILE_NAME = "messages.yml";
    private final Map<String, Component> cache = new HashMap<>();

    public MessageManager(File dataFolder) {
        LOGGER.info("创建消息管理器实例");
        messagesFile = new File(dataFolder, CONFIG_FILE_NAME);
        configFile = new YamlConfiguration();
    }

    public void init() {
        LOGGER.info("初始化消息管理器中");

        reload();
    }

    public Component get(String key) {
        if (cache.containsKey(key)) {
            return cache.get(key);
        }

        var entry = configFile.getString(key);

        if (entry == null) {
            return new TextComponent(key);
        }

        var result = Component.Serializer.fromJson(entry);
        cache.put(key, result);
        return result;
    }

    public void reload() {
        cache.clear();

        if (!messagesFile.exists()) {
            Game.getAssist().saveResource(CONFIG_FILE_NAME, false);
        }

        try {
            configFile.load(messagesFile);
        } catch (IOException e) {
            LOGGER.warn("加载消息文件失败", e);
        } catch (InvalidConfigurationException e) {
            Game.getAssist().saveResource(CONFIG_FILE_NAME, false);
        }
    }
}
