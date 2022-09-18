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

package io.github.withlithum.enderassist.io;

import io.github.withlithum.enderassist.EnderAssist;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.collection.NitriteId;
import org.dizitart.no2.mvstore.MVStoreModule;
import org.dizitart.no2.repository.ObjectRepository;
import org.slf4j.Logger;

import java.io.File;
import java.util.UUID;

public final class PlayerProfileManager {
    private PlayerProfileManager() {}

    static Logger logger;
    static MVStoreModule storeModule;
    static Nitrite db;
    static ObjectRepository<PlayerProfile> repository;
    static boolean ready;

    public static void init(EnderAssist assist) {
        logger = assist.getSLF4JLogger();
        logger.info("Initialising NO2 database");

        storeModule = MVStoreModule.withConfig()
                .filePath(new File(assist.getDataFolder(), "profiles.db").getAbsolutePath())
                .compress(true)
                .build();

        db = Nitrite.builder()
                .loadModule(storeModule)
                .openOrCreate();

        repository = db.getRepository(PlayerProfile.class, "profiles");
        logger.info("NO2 database ready");
        ready = true;
    }

    public static void ensure(UUID uuid) {
        get(uuid);
    }

    public static PlayerProfile get(UUID uuid) {
        PlayerProfile result = repository.getById(NitriteId.createId(uuid.toString()));

        if (result == null)
        {
            PlayerProfile created = new PlayerProfile(uuid);
            repository.insert(created);
            return created;
        }

        return result;
    }

    public static void put(PlayerProfile profile) {
        repository.update(profile);
    }

    /**
     * Saves the NO2 database to the disk. If not yet initialised or have no unsaved changes
     * this method fails silently.
     */
    public static void save() {
        if (!ready) return;

        if (db.hasUnsavedChanges()) {
            logger.info("Unsaved changes detected, saving NO2 database");
            db.commit();
        }
    }

    /**
     * Saves all unsaved changes and closes the database.
     */
    public static void shutdown() {
        if (!ready) return;

        if (!db.isClosed()) {
            logger.info("Closing NO2 database. This may take some time.");
            db.close();
        }
    }
}
