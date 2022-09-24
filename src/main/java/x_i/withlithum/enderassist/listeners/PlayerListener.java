package x_i.withlithum.enderassist.listeners;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.BeetrootBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
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

    public void handleHarvest(PlayerInteractEvent event, BlockPos pos, BlockState st, CropBlock crop, ServerLevel level) {
        Block.dropResources(st, new LootContext.Builder(level)
                .withParameter(LootContextParams.ORIGIN, Game.vector(pos))
                .withParameter(LootContextParams.TOOL, ItemStack.EMPTY));
        st.setValue(crop.getAgeProperty(), 0);
        level.destroyBlock(pos, false);
        level.setBlock(pos, st.setValue(crop.getAgeProperty(), 0), 0);
        level.blockUpdated(pos, crop);
        event.setUseInteractedBlock(Event.Result.DENY);
        event.getPlayer().swingMainHand();
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null) {
            return;
        }

        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        var pos = Game.fromBukkit(event.getClickedBlock().getLocation());
        var st = Game.fromBukkitBlock(event.getClickedBlock());
        var bl = st.getBlock();
        var level = Game.fromBukkit(event.getClickedBlock().getWorld());

        if (bl instanceof BeetrootBlock beetroot && st.getValue(beetroot.getAgeProperty()) == 3) {
            handleHarvest(event, pos, st, beetroot, level);
        } else if (bl instanceof CropBlock crop && st.getValue(crop.getAgeProperty()) == 7) {
            handleHarvest(event, pos, st, crop, level);
        }
    }
}
