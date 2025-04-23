package Communication;

import java.io.Serializable;

public class NewConnectionRequest_NotUsedYet implements Serializable {
    private String hostname;
    private int port;

    public NewConnectionRequest_NotUsedYet(String senderHost, int senderPort) {
        this.hostname = senderHost;
        this.port = senderPort;
    }

    public String getSenderHost() {
        return hostname;
    }

    public int getSenderPort() {
        return port;
    }
}
