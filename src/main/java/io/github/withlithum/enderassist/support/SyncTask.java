/*
MIT License

Copyright (c) 2021 jmattingley23

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

package io.github.withlithum.enderassist.support;

import io.github.withlithum.enderassist.EnderAssist;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

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
