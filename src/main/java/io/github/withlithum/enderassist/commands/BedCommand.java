package io.github.withlithum.enderassist.commands;

import io.github.withlithum.enderassist.PlayerUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BedCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length != 0) {
            PlayerUtil.sendInvalidArgs(sender, label, "不应有参数");
            return false;
        }

        if (!(sender instanceof Player)) {
            PlayerUtil.sendAlert(sender, "此命令只能由玩家执行");
            return false;
        }

        Player player = (Player)sender;
        Location spawn = player.getBedSpawnLocation();
        if (spawn == null) {
            PlayerUtil.sendAlert(sender, PlayerUtil.msg("spawn_not_valid"));
            return true;
        }

        player.teleport(spawn);

        return true;
    }
}
