package x_i.withlithum.enderassist.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import x_i.withlithum.enderassist.Game;

import static net.minecraft.commands.Commands.*;

public final class SpawnCommand {
    private SpawnCommand() {}

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(literal("spawn")
                .executes(context -> {
                    var player = context.getSource().getPlayerOrException();

                    var level = Game.getServer().overworld();
                    var spawn = level.getSharedSpawnPos();

                    player.teleportTo(level, spawn);
                    return 1;
                }));
    }
}
