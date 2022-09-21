package x_i.withlithum.enderassist.support;

import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.protocol.game.ClientboundTabListPacket;
import org.bukkit.scheduler.BukkitRunnable;
import x_i.withlithum.enderassist.Game;

public class PingUpdateTask extends BukkitRunnable {
    @Override
    public void run() {
        for (var player : Game.getServer().getPlayerList().getPlayers()) {
            var ping = player.latency;

            var packet = new ClientboundTabListPacket(Game.messages().get("player_list.header"),
                    Game.formatMsg(Game.messages().getRaw("player_list.footer"),
                            ping));

            player.connection.send(packet);
        }
    }
}
