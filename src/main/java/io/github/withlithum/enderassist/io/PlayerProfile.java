package io.github.withlithum.enderassist.io;

import org.dizitart.no2.Nitrite;
import org.dizitart.no2.collection.NitriteId;
import org.dizitart.no2.repository.annotations.Entity;
import org.dizitart.no2.repository.annotations.Id;

import java.util.UUID;

@Entity(value = "player-profile")
public class PlayerProfile {
    public PlayerProfile(UUID uuid) {
        this.uuid = uuid;
        this.id = NitriteId.createId(uuid.toString());
    }

    private boolean showStatus;

    private UUID uuid;

    @Id
    private NitriteId id;

    public UUID uuid() {
        return uuid;
    }

    public void uuid(UUID value) {
        uuid = value;
        this.id = NitriteId.createId(uuid.toString());
    }

    public boolean showStatus() {
        return showStatus;
    }

    public void showStatus(boolean bool) {
        showStatus = bool;
    }
}
