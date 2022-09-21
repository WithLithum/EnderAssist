package x_i.withlithum.enderassist.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.dedicated.DedicatedServer;
import org.bukkit.Bukkit;
import x_i.withlithum.enderassist.Minecraft;

public final class Commands {
    private Commands() {}

    public static void init() {
        var dispatcher = Minecraft.getServer().getCommands().getDispatcher();

        PingCommand.register(dispatcher);
        MetaCommand.register(dispatcher);
    }
}
