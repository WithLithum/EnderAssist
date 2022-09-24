package x_i.withlithum.enderassist.listeners;

import net.kyori.adventure.text.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.BeetrootBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.storage.loot.LootContext;
import org.bukkit.Material;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import x_i.withlithum.enderassist.Game;

public class PlayerListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Game.getAssist().getSyncTask().refresh(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        var lastSeen = System.currentTimeMillis();
        var player = Game.fromBukkit(event.getPlayer());

        var uuid = player.getUUID();
        var profile = Game.getAssist().profiles().get(uuid);
        profile.lastSeen(lastSeen);
        Game.getAssist().profiles().put(uuid, profile);

        Game.getAssist().getSyncTask().refresh(event.getPlayer());
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null) {
            return;
        }

        var pos = Game.fromBukkit(event.getClickedBlock().getLocation());
        var st = Game.fromBukkitBlock(event.getClickedBlock());
        var bl = st.getBlock();
        var level = Game.fromBukkit(event.getClickedBlock().getWorld());

        if (bl instanceof BeetrootBlock beetroot && st.getValue(beetroot.getAgeProperty()) == 3) {
            Block.dropResources(st, new LootContext.Builder(level));
            st.setValue(beetroot.getAgeProperty(), 0);
        } else if (bl instanceof CropBlock crop && st.getValue(crop.getAgeProperty()) == 7) {
            Block.dropResources(st, new LootContext.Builder(level));
            st.setValue(crop.getAgeProperty(), 0);
        }

        level.setBlockAndUpdate(pos, st);
    }
}
