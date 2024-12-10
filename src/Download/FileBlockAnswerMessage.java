package Download;

import java.io.Serializable;
import java.util.Arrays;

public final class FileBlockAnswerMessage implements Serializable {
    private final String fileHash;
    private final String dtmUID;
    private final int blockId;
    private final byte[] data;

    public FileBlockAnswerMessage(String fileHash, byte[] data, int blockId, String dtmUID) {
        this.fileHash = fileHash;
        this.dtmUID = dtmUID;
        this.blockId = blockId;
        this.data = Arrays.copyOf(data, data.length); // Evita alterações externas - Imutabilidade para maior segurança
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


    @Override
    public String toString() {
        return "FileBlockAnswerMessage{" +
                "fileHash='" + fileHash + '\'' +
                '}';
    }
}
