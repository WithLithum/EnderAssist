package x_i.withlithum.enderassist.support;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import x_i.withlithum.enderassist.EnderAssist;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Provides the AppleSkin synchronisation service.
 * @author jmattingley23
 */
public class SyncTask extends BukkitRunnable {

    public static final String SATURATION_KEY = "appleskin:saturation_sync";
    public static final String EXHAUSTION_KEY = "appleskin:exhaustion_sync";

    private static final float MINIMUM_EXHAUSTION_CHANGE_THRESHOLD = 0.01F;

    private final EnderAssist appleSkinSpigotPlugin;
    private final Map<UUID, Float> previousSaturationLevels;
    private final Map<UUID, Float> previousExhaustionLevels;

    public SyncTask(EnderAssist assist) {
        this.appleSkinSpigotPlugin = assist;
        previousSaturationLevels = new HashMap<>();
        previousExhaustionLevels = new HashMap<>();
    }

    @Override
    public void run() {
        for(Player player : appleSkinSpigotPlugin.getServer().getOnlinePlayers()) {
            updatePlayer(player);
        }
    }

    private void updatePlayer(Player player) {
        float saturation = player.getSaturation();
        Float previousSaturation = previousSaturationLevels.get(player.getUniqueId());
        if(previousSaturation == null || saturation != previousSaturation) {
            player.sendPluginMessage(appleSkinSpigotPlugin, SATURATION_KEY, ByteBuffer.allocate(Float.BYTES).putFloat(saturation).array());
            previousSaturationLevels.put(player.getUniqueId(), saturation);
        }

        float exhaustion = player.getExhaustion();
        Float previousExhaustion = previousExhaustionLevels.get(player.getUniqueId());
        if(previousExhaustion == null || Math.abs(exhaustion - previousExhaustion) >= MINIMUM_EXHAUSTION_CHANGE_THRESHOLD) {
            player.sendPluginMessage(appleSkinSpigotPlugin, EXHAUSTION_KEY, ByteBuffer.allocate(Float.BYTES).putFloat(exhaustion).array());
            previousExhaustionLevels.put(player.getUniqueId(), exhaustion);
        }
    }

    @SuppressWarnings("unused")
    public void refresh(Player player) {
        previousSaturationLevels.remove(player.getUniqueId());
        previousExhaustionLevels.remove(player.getUniqueId());
    }
}
