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
    int firstbytestohash = 100;

    FileBlock(int startByte, int endByte){
        this.startByte = startByte;
        this.endByte = endByte;
    }

    public void hashblock(File file) {
        try (RandomAccessFile fileStream = new RandomAccessFile(file, "r")) {
            int length = (Math.min(this.endByte - this.startByte, firstbytestohash));
            byte[] bytes = new byte[length];
            fileStream.seek(this.startByte);
            fileStream.readFully(bytes);
            this.hash = sha256Hash(bytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] sha256Hash(byte[] bytes) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        return md.digest(bytes);
    }

    @Override
    public String toString(){
        StringBuilder hexString = new StringBuilder();
        for (byte b : this.hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return  "Start: "+ this.startByte + " End: "+ this.endByte+  " // SHA-256 hash (hex): " + hexString;
    }
}