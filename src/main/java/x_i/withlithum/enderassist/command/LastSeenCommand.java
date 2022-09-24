package x_i.withlithum.enderassist.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import x_i.withlithum.enderassist.Game;

import java.text.DateFormat;
import java.util.Date;

import static net.minecraft.commands.Commands.*;

public final class LastSeenCommand {
    private LastSeenCommand() {}

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(literal("lastseen")
                .then(argument("victim", EntityArgument.player())
                        .executes(LastSeenCommand::execute)));
    }

    private static int execute(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        var player = EntityArgument.getPlayer(context, "victim");
        var profile = Game.getAssist().profiles().getIfExists(player.getUUID());

        if (profile == null) {
            throw EntityArgument.NO_PLAYERS_FOUND.create();
        }

        var lastSeen = profile.lastSeen();
        if (lastSeen == 0) {
            context.getSource().sendSuccess(Game.messages().get("commands.lastseen.success.not_leaved"), false);
        } else {
            context.getSource().sendSuccess(Game.formatMsg(Game.messages().getRaw("commands.lastseen.success.ok"), DateFormat.getDateTimeInstance()
                    .format(new Date(lastSeen))), false);
        }

        return 1;
    }
}
