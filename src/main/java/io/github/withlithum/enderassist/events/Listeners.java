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

package io.github.withlithum.enderassist.events;

import io.github.withlithum.enderassist.EnderAssist;
import io.github.withlithum.enderassist.support.SyncTask;
import org.bukkit.plugin.PluginManager;

public final class Listeners {
    private Listeners() {}

    public static void register(EnderAssist assist, SyncTask task) {
        PluginManager pm = assist.getServer().getPluginManager();

        pm.registerEvents(new PlayerListener(), assist);
        pm.registerEvents(new ServerListener(), assist);
        pm.registerEvents(new EntityListener(), assist);
        pm.registerEvents(new SyncTaskListener(task), assist);
    }
}
