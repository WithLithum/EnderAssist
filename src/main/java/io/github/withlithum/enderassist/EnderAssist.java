package io.github.withlithum.enderassist;

import io.github.withlithum.enderassist.commands.Commands;
import io.github.withlithum.enderassist.io.PlayerProfileManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class EnderAssist extends JavaPlugin {

    @Override
    public void onEnable() {
        this.saveDefaultConfig();

        PlayerUtil.reload();
        PlayerProfileManager.init(this);
        Commands.register(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        PlayerProfileManager.shutdown();
    }
}
