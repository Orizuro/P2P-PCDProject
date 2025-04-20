package Download;

import java.io.Serializable;
import java.util.Arrays;

public final class FileBlockAnswerMessage implements Serializable {
    private final String fileHash;
    private final String dtmUID;
    private final int blockId;
    private final byte[] data;
    private final String senderIP;  // Novo campo
    private final int senderPort;   // Novo campo


    public FileBlockAnswerMessage(String fileHash, byte[] data, int blockId, String dtmUID, String senderIP, int senderPort) {
        this.fileHash = fileHash;
        this.dtmUID = dtmUID;
        this.blockId = blockId;
        this.data = Arrays.copyOf(data, data.length); // Evita alterações externas - Imutabilidade para maior segurança
        this.senderIP = senderIP;
        this.senderPort = senderPort;
    }

    public byte[] getData() {
        return Arrays.copyOf(data, data.length); // Evita modificações externas - Imutabilidade para maior segurança
    }

    public int getBlockId() {
        return blockId;
    }

    public String getDtmUID() {
        return dtmUID;
    }

    public String getSenderIP() {
        return senderIP;
    }

    public int getSenderPort() {
        return senderPort;
    }

    @Override
    public String toString() {
        return "FileBlockAnswerMessage{" +
                "fileHash='" + fileHash + '\'' +
                '}';
    }

}
