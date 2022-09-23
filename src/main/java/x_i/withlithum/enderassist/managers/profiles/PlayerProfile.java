package x_i.withlithum.enderassist.managers.profiles;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PlayerProfile {
    private long lastSeen;

    public long lastSeen() {
        return lastSeen;
    }

    public void lastSeen(long value) {
        lastSeen = value;
    }

    public void write(DataOutputStream stream) throws IOException {
        stream.writeLong(lastSeen);
    }

    public void read(DataInputStream stream) throws IOException {
        lastSeen = stream.readLong();
    }
}
