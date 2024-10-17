package Communication;

import java.io.Serializable;

public class MessageWrapper implements Serializable {
    private String sender;
    private Command command;
    private Object data;

    public MessageWrapper(String sender, Command command, Object data) {
        this.sender = sender;
        this.command = command;
        this.data = data;
    }

    public String getSender() {
        return sender;
    }

    public Command getCommand() {
        return command;
    }

    public Object getData() {
        return data;
    }

}
