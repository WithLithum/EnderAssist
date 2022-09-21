package x_i.withlithum.enderassist.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.TextComponent;
import org.bukkit.plugin.java.JavaPlugin;
import x_i.withlithum.enderassist.EnderAssist;
import x_i.withlithum.enderassist.Game;
import x_i.withlithum.enderassist.managers.ConfigManager;

import static net.minecraft.commands.Commands.*;

public final class MetaCommand {
    private MetaCommand() {}

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(literal("enderassist")
                .then(literal("version")
                        .executes(MetaCommand::versionExecute))
                .then(literal("reload")
                        .requires(s -> s.hasPermission(2))
                        .executes(MetaCommand::reloadExecute)));
    }

    public static int reloadExecute(CommandContext<CommandSourceStack> context) {
        ConfigManager.getInstance().reload();
        Game.messages().reload();
        context.getSource().sendSuccess(Game.messages().get("commands.reload.success"), true);
        return 1;
    }

    public static int versionExecute(CommandContext<CommandSourceStack> context) {
        var descriptor = JavaPlugin.getPlugin(EnderAssist.class).getDescription();

        context.getSource().sendSuccess(new TextComponent(descriptor.getVersion()), false);
        return 1;
    }
}
