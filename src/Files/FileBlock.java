package Files;

import java.io.File;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

class FileBlock implements Serializable {
    int startByte;
    int endByte;
    byte[] hash;

    FileBlock(int startByte, int endByte){
        this.startByte = startByte;
        this.endByte = endByte;
    }

    public void hashblock(File file) {
        try (RandomAccessFile fileStream = new RandomAccessFile(file, "r")) {    // Cria um arquivo de leitura
            long length = this.endByte - this.startByte;
            byte[] bytes = new byte[(int) length];
            fileStream.seek(this.startByte);    // Move o ponteiro do arquivo para o início do bloco
            fileStream.readFully(bytes);     // Lê o bloco de bytes do arquivo
            this.hash = sha256Hash(bytes);    // Calcula o hash dos bytes lidos
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] sha256Hash(byte[] bytes) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        return md.digest(bytes);
    }

    @Override
    public String toString(){    // StringBuilder para a representação hexadecimal
        StringBuilder hexString = new StringBuilder();
        for (byte b : this.hash) {     // Itera sobre os bytes do hash
            String hex = Integer.toHexString(0xff & b);    // Converte cada byte para representação hexadecimal
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return  "Start: "+ this.startByte + " End: "+ this.endByte+  " // SHA-256 hash (hex): " + hexString;
    }
}