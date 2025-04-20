package Files;

import java.io.*;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FileBlockInfo implements Serializable {
    public int startByte;
    public int endByte;
    public String fileName;

    FileBlockInfo(File file, int startByte, int endByte) {
        this.startByte = startByte;
        this.endByte = endByte;
        this.fileName = file.getName();
    }

    public byte[] readFileBytesInRange(File file) {
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r")) {
            int length = endByte - startByte ;
            byte[] buffer = new byte[length];
            randomAccessFile.seek(startByte);
            randomAccessFile.readFully(buffer);
            return buffer;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String toString(){    // StringBuilder para a representação hexadecimal
        return  "File: " + this.fileName + " Start: "+ this.startByte + " End: "+ this.endByte;
    }
}