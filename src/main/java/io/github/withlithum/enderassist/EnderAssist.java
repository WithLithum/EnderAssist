/*
 * Copyright (C) 2022 WithLithum.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.withlithum.enderassist;

import io.github.withlithum.enderassist.commands.Commands;
import io.github.withlithum.enderassist.events.Listeners;
import io.github.withlithum.enderassist.support.PingUpdateTask;
import io.github.withlithum.enderassist.support.SyncTask;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.Messenger;

public final class EnderAssist extends JavaPlugin {

    private SyncTask syncTask;
    private PingUpdateTask updateTask;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();

        PlayerUtil.reload();
        Commands.register(this);

        syncTask = createSyncTask();
        updateTask = new PingUpdateTask();
        Listeners.register(this, syncTask);

        syncTask.runTaskTimer(this, 0L, 1L);
        updateTask.runTaskTimer(this, 0L, 60L);

        Messenger messenger = getServer().getMessenger();
        messenger.registerOutgoingPluginChannel(this, SyncTask.SATURATION_KEY);
        messenger.registerOutgoingPluginChannel(this, SyncTask.EXHAUSTION_KEY);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        if (syncTask != null) {
            syncTask.cancel();
            syncTask = null;
        }

        if (updateTask != null) {
            updateTask.cancel();
            updateTask = null;
        }
    }

    private SyncTask createSyncTask() {
        return new SyncTask(this);
    }
}
