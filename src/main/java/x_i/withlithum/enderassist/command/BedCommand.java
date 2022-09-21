package x_i.withlithum.enderassist.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.event.player.PlayerTeleportEvent;
import x_i.withlithum.enderassist.Game;

import static net.minecraft.commands.Commands.*;

public final class BedCommand {
    private BedCommand() {}

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(literal("bed")
                .requires(context -> context.source instanceof ServerPlayer)
                .executes(BedCommand::execute));
    }

    public static int execute(CommandContext<CommandSourceStack> context) {
        // 执行条件：不是玩家 -> 执行失败
        if (!(context.getSource().source instanceof ServerPlayer player)) {
            context.getSource().sendFailure(Game.messages().get("commands.generic.player_only"));
            return 0;
        }

        // 获取位置和维度
        var level = Game.getServer().getLevel(player.getRespawnDimension());
        var spawnPos = player.getBukkitEntity().getBedSpawnLocation();

        // 执行条件：维度或生成地点无效 -> 执行失败
        if (level == null || spawnPos == null) {
            context.getSource().sendFailure(Game.messages().get("commands.bed.spawn_invalid"));
            return 0;
        }

        // 执行成功
        player.teleportTo(level, spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(), spawnPos.getYaw(), spawnPos.getPitch(),
                PlayerTeleportEvent.TeleportCause.COMMAND);

        return 1;
    }
}
