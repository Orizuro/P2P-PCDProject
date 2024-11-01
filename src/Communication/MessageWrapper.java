package Communication;

import java.io.Serializable;
import java.io.StringReader;

public class MessageWrapper implements Serializable {
    private String receiverIp;
    //private String senderIp;
    private Command command;
    private Object data;     // Variável que armazena os dados associados à mensagem
    //, String senderIp
    public MessageWrapper(String receiverIp, Command command, Object data) {
        this.receiverIp = receiverIp;
        //this.senderIp = senderIp;
        this.command = command;
        this.data = data;
    }

    public String getReceiver() {
        return receiverIp;
    }
    /*
    public String getSender() {
        return senderIp;
    }
    */
    public Command getCommand() {
        return command;
    }

    public Object getData() {
        return data;
    }

}
