package x_i.withlithum.enderassist.managers.profiles;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Objects;

public class PlayerProfile {
    private long lastSeen;

    private String userName;

    public long lastSeen() {
        return lastSeen;
    }

    public void lastSeen(long value) {
        lastSeen = value;
    }

    public void userName(String str) {
        this.userName = Objects.requireNonNull(str);
    }

    public String userName() {
        return userName;
    }

    public void write(DataOutputStream stream) throws IOException {
        stream.writeLong(lastSeen);
        stream.writeUTF(userName);
    }

    public void read(DataInputStream stream) throws IOException {
        lastSeen = stream.readLong();
        userName = stream.readUTF();
    }
}
