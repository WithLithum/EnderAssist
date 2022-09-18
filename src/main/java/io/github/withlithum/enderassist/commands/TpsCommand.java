package io.github.withlithum.enderassist.commands;

import io.github.withlithum.enderassist.EnderAssist;
import io.github.withlithum.enderassist.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class TpsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!Commands.expectEmpty(args, sender, label)) return false;

        PlayerUtil.sendInfo(sender, String.format(PlayerUtil.msg("tps_result"), Bukkit.getTPS()[0]));
        return true;
    }
}
