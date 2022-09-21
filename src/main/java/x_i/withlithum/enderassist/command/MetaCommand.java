package x_i.withlithum.enderassist.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.TextComponent;
import org.bukkit.plugin.java.JavaPlugin;
import x_i.withlithum.enderassist.EnderAssist;

import static net.minecraft.commands.Commands.*;

public final class MetaCommand {
    private MetaCommand() {}

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(literal("enderassist")
                .then(literal("version")
                        .executes(MetaCommand::versionExecute)));
    }

    public static int versionExecute(CommandContext<CommandSourceStack> context) {
        var descriptor = JavaPlugin.getPlugin(EnderAssist.class).getDescription();

        context.getSource().sendSuccess(new TextComponent(descriptor.getVersion()), false);
        return 1;
    }
}
