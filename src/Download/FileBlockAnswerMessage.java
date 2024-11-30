package Download;

import java.io.Serializable;
import java.util.Arrays;

public final class FileBlockAnswerMessage implements Serializable {
    private final String fileHash;
    private final int blockId;
    private final byte[] data;

    public FileBlockAnswerMessage(String fileHash, byte[] data, int blockId) {
        this.fileHash = fileHash;
        this.blockId = blockId;
        this.data = Arrays.copyOf(data, data.length); // Evita alterações externas - Imutabilidade para maior segurança
    }

    public String getFileHash() {
        return fileHash;
    }

    public byte[] getData() {
        return Arrays.copyOf(data, data.length); // Evita modificações externas - Imutabilidade para maior segurança
    }

    public int getBlockId() {
        return blockId;
    }


    @Override
    public String toString() {
        return "FileBlockAnswerMessage{" +
                "fileHash='" + fileHash + '\'' +
                '}';
    }
}
