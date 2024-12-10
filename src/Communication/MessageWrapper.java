package Communication;

import java.io.Serializable;

public class MessageWrapper implements Serializable {
    private final String serverIp;
    private final int serverPort;
    private final Command command;
    private final Object data;     // Variável que armazena os dados associados à mensagem

    public MessageWrapper(String serverIp, int serverPort, Command command, Object data) {
        this.serverIp = serverIp;
        this.serverPort = serverPort;
        this.command = command;
        this.data = data;
    }

    public String getServerIp() {
        return serverIp;
    }

    public int getServerPort() {
        return serverPort;
    }

    public Command getCommand() {
        return command;
    }

    public Object getData() {
        return data;
    }

}
