package x_i.withlithum.enderassist.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.TextComponent;

import static net.minecraft.commands.Commands.*;

public final class PingCommand {
    private PingCommand() {}

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(literal("ping")
                .executes((context -> {
                    context.getSource().sendSuccess(new TextComponent("Pong!"), false);
                    return 1;
                })));
    }
}
