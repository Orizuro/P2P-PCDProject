package Communication;

import java.io.Serializable;

public class NewConnectionRequest_NotUsedYet implements Serializable {
    private String clientHost;
    private int clientPort;

    public NewConnectionRequest_NotUsedYet(String senderHost, int senderPort) {
        this.clientHost = senderHost;
        this.clientPort = senderPort;
    }

    public String getSenderHost() {
        return clientHost;
    }

    public int getSenderPort() {
        return clientPort;
    }
}
