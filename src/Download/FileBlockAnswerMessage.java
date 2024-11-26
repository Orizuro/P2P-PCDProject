package Download;

import java.io.Serializable;
import java.util.Arrays;

public final class FileBlockAnswerMessage implements Serializable {
    private final String fileHash;
    private final int offset;
    private final byte[] data;
    private final int length;

    public FileBlockAnswerMessage(String fileHash, int offset, byte[] data, int length) {
        this.fileHash = fileHash;
        this.offset = offset;
        this.data = Arrays.copyOf(data, data.length); // Evita alterações externas - Imutabilidade para maior segurança
        this.length = length;
    }

    public String getFileHash() {
        return fileHash;
    }

    public int getOffset() {
        return offset;
    }

    public byte[] getData() {
        return Arrays.copyOf(data, data.length); // Evita modificações externas - Imutabilidade para maior segurança
    }

    public int getLength() {
        return length;
    }

    @Override
    public String toString() {
        return "FileBlockAnswerMessage{" +
                "fileHash='" + fileHash + '\'' +
                ", offset=" + offset +
                ", length=" + length +
                '}';
    }
}
