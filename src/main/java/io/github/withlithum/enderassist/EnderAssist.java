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
import io.github.withlithum.enderassist.io.PlayerProfileManager;
import io.github.withlithum.enderassist.support.SyncTask;
import org.bukkit.plugin.java.JavaPlugin;

public final class EnderAssist extends JavaPlugin {

    private SyncTask syncTask;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();

        PlayerUtil.reload();
        PlayerProfileManager.init(this);
        Commands.register(this);

        syncTask = createSyncTask();
        Listeners.register(this, syncTask);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        PlayerProfileManager.shutdown();

        if (syncTask != null) {
            syncTask.cancel();
            syncTask = null;
        }
    }

    private SyncTask createSyncTask() {
        return new SyncTask(this);
    }
}
