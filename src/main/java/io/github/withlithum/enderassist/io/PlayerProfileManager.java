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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.github.withlithum.enderassist.EnderAssist;
import org.slf4j.Logger;
import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class PlayerProfileManager {
    private PlayerProfileManager() {}
    private static File dataFile;

    static final Gson gson = new GsonBuilder().enableComplexMapKeySerialization()
            .create();
    static Logger logger;
    static Map<UUID, PlayerProfile> profiles = new HashMap<>();
    static boolean ready;

    public static void init(EnderAssist assist) {
        logger = assist.getSLF4JLogger();
        dataFile = new File(assist.getDataFolder(), "profiles.json");

        if (dataFile.exists()) {
            try (JsonReader fis = new JsonReader(new InputStreamReader(Files.newInputStream(dataFile.toPath())))) {
                Type type = new TypeToken<Map<UUID, PlayerProfile>>() {}.getType();
                profiles = gson.fromJson(fis, type);
            } catch (FileNotFoundException x) {
                logger.error("Failed to read", x);
            } catch (IOException x) {
                logger.error("Failed to read because of IO Exception", x);
            }
        }

        // legacy support
        ready = true;
    }

    public static void ensure(UUID uuid) {
        get(uuid);
    }

    public static PlayerProfile get(UUID uuid) {
        return profiles.computeIfAbsent(uuid, x -> new PlayerProfile(uuid));
    }

    public static void put(UUID uuid, PlayerProfile profile) {
        profiles.put(uuid, profile);
    }

    /**
     * Saves the NO2 database to the disk. If not yet initialised or have no unsaved changes
     * this method fails silently.
     */
    public static void save() {
        try (JsonWriter jw = new JsonWriter(new OutputStreamWriter(Files.newOutputStream(dataFile.toPath())))) {
            gson.toJson(jw);
        } catch (IOException x) {
            logger.error("Failed to save", x);
        }
    }

    /**
     * Saves all unsaved changes and closes the database.
     */
    public static void shutdown() {
        if (!ready) return;

        save();
    }
}
