package x_i.withlithum.enderassist.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
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
                .then(argument("victim", StringArgumentType.string())
                        .executes(LastSeenCommand::execute)));
    }

    private static int execute(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        try {
            var str = StringArgumentType.getString(context, "victim");
            var profile = Game.getAssist().profiles().search(str);

            if (profile == null) {
                throw EntityArgument.NO_PLAYERS_FOUND.create();
            }

            var player = Game.getServer().getPlayerList().getPlayer(profile.getKey());
            if (player != null && player.isRealPlayer) {
                context.getSource().sendSuccess(Game.formatMsg(Game.messages().getRaw("commands.lastseen.success.online"), str), false);
                return 1;
            }

            var lastSeen = profile.getValue().lastSeen();
            if (lastSeen == 0) {
                context.getSource().sendSuccess(Game.messages().get("commands.lastseen.success.not_leaved"), false);
            } else {
                context.getSource().sendSuccess(Game.formatMsg(Game.messages().getRaw("commands.lastseen.success.ok"), str, DateFormat.getDateTimeInstance()
                        .format(new Date(lastSeen))), false);
            }

            return 1;
        }
        catch (Exception ex) {
            Game.getAssist().getLog4JLogger().error("Exception thrown on LastSeen", ex);
            throw ex;
        }
    }
}
