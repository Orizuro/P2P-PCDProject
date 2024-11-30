package Communication;

import java.io.Serializable;

public class MessageWrapper implements Serializable {
    private String serverIp;
    private int serverPort;
    private Command command;
    private Object data;     // Variável que armazena os dados associados à mensagem
    //, String senderIp
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
