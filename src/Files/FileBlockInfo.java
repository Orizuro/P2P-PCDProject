package Files;

import java.io.*;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FileBlockInfo implements Serializable {
    public int startByte;
    public int endByte;
    public String hash;

    FileBlockInfo(File file, int startByte, int endByte) {
        this.startByte = startByte;
        this.endByte = endByte;
        this.hash = hashblock(file);
    }

    public byte[] readFileBytesInRange(File file) {
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r")) {
            int length = endByte - startByte ;//+ 1; // Length of the bytes to read
            byte[] buffer = new byte[length];    // Buffer to hold the bytes

            randomAccessFile.seek(startByte);    // Move to the start byte
            randomAccessFile.readFully(buffer); // Read the specific range into the buffer

            return buffer;
        } catch (IOException e) {
            e.printStackTrace();
            return null; // Return null if an error occurs
        }
    }

    public String hashblock(File file) {
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            try (InputStream inputStream = Files.newInputStream(file.toPath())) { // Cria um fluxo de entrada para ler o arquivo
                inputStream.skip(startByte);
                byte[] buffer = new byte[512];
                long remainingBytes = endByte - startByte + 1; // Total bytes to read
                int bytesRead;
                while (remainingBytes > 0 && (bytesRead = inputStream.read(buffer, 0, (int) Math.min(buffer.length, remainingBytes))) != -1) {
                    digest.update(buffer, 0, bytesRead); // Update the digest with the bytes read
                    remainingBytes -= bytesRead; // Reduce the number of remaining bytes to read
                }
            }
            StringBuilder hexString = new StringBuilder();
            for (byte b : digest.digest()) { // Itera sobre os bytes do hash
                String hex = Integer.toHexString(0xff & b); // Converte cada byte para representação hexadecimal
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
        }catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }



    @Override
    public String toString(){    // StringBuilder para a representação hexadecimal
        return  "Start: "+ this.startByte + " End: "+ this.endByte+  " // SHA-256 hash (hex): " + hash;
    }
}