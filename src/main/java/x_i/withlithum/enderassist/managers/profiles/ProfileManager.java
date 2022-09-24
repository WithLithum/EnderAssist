package x_i.withlithum.enderassist.managers.profiles;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import x_i.withlithum.enderassist.EnderAssist;

import javax.annotation.Nullable;
import java.io.*;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ProfileManager {

    private final File profilesFolder;

    private static final Logger LOGGER = LogManager.getLogger("EA-ProfileMgr");
    private final Map<UUID, PlayerProfile> cache = new HashMap<>();

    private boolean writable = true;

    public ProfileManager(EnderAssist assist) {
        profilesFolder = new File(assist.getDataFolder(), "profiles");
    }

    public void init() {
        if (!profilesFolder.exists() && !profilesFolder.mkdir()) {
            writable = false;
            LOGGER.warn("无法创建文件夹，将不会写入玩家档案文件");
            return;
        }

        if (!profilesFolder.canRead() || !profilesFolder.canWrite()) {
            writable = false;
            LOGGER.warn("读/写权限不正确，将不会写入玩家档案文件");
        }
    }

    public void put(UUID uuid, PlayerProfile profile) {
        cache.put(uuid, profile);
    }

    @Nullable
    public PlayerProfile getIfExists(UUID uuid) {
        if (cache.containsKey(uuid)) {
            return cache.get(uuid);
        }

        var file = new File(profilesFolder, String.format("%s.dat", uuid.toString()));

        if (!file.exists()) {
            return null;
        }

        var result = new PlayerProfile();

        try (var dis = new DataInputStream(Files.newInputStream(file.toPath()))) {
            result.read(dis);
            return result;
        } catch (IOException e) {
            writable = false;
            LOGGER.warn("IO错误", e);
            return null;
        }
    }

    public PlayerProfile get(UUID uuid) {
        if (cache.containsKey(uuid)) {
            return cache.get(uuid);
        }

        var file = new File(profilesFolder, String.format("%s.dat", uuid.toString()));

        if (!file.exists()) {
            var result = create(uuid);
            save(result, uuid);
            cache.put(uuid, result);
            return result;
        }

        var result = new PlayerProfile();

        try (var dis = new DataInputStream(Files.newInputStream(file.toPath()))) {
            result.read(dis);
            return result;
        } catch (IOException e) {
            writable = false;
            LOGGER.warn("IO错误", e);
            return create(uuid);
        }
    }

    public void save(PlayerProfile profile, UUID uuid) {
        var file = new File(profilesFolder, String.format("%s.dat", uuid.toString()));

        try (var dos = new DataOutputStream(Files.newOutputStream(file.toPath()))) {
            profile.write(dos);
        } catch (IOException e) {
            LOGGER.warn(e);
        }
    }

    public void saveAll() {
        if (!writable) {
            return;
        }

        for (var profile : cache.entrySet()) {
            save(profile.getValue(), profile.getKey());
        }
    }

    public PlayerProfile create(UUID uuid) {
        var result = new PlayerProfile();
        cache.put(uuid, result);
        return result;
    }
}
