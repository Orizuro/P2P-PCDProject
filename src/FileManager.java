import java.io.*;
import java.nio.file.Files;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FileManager {
    String directoryPath;
     File file;
     byte[] filehash;
     int blockNumber;
     int fileSize;
     FileBlock[] fileBlocks;
     final int blocksize = 102400;

    FileManager(String directoryPath) {
        this.directoryPath = directoryPath;
        this.file = new File(directoryPath);
        this.fileSize = (int) file.length();
        this.blockNumber = (int) Math.ceil( (double) fileSize / blocksize) ;
        this.filehash = getFileHash();
        this.fileBlocks = new FileBlock[blocksize];
    }

    public void readAllFiles() {
        File[] files = file.listFiles();
        if (files != null) {
            for (File file : files) {
                System.out.println(file.getName());
            }
        }
    }

    private byte[] getFileHash() {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            try (FileInputStream fis = new FileInputStream(this.file)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    digest.update(buffer, 0, bytesRead);
                }
            }
            return digest.digest();
        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    void splitFile(){
        for(int i = 0; i < blockNumber ; i++ ){
            this.fileBlocks[i] = new FileBlock(i * blocksize, ((i+1) * blocksize)-1);
            this.fileBlocks[i].hashblock(this.file);
        }
    }
    class FileBlock{
        int startByte;
        int endByte;
        byte[] hash;

        FileBlock(int startByte, int endByte){
            this.startByte = startByte;
            this.endByte = endByte;
        }

        public void hashblock(File file) {
            try (RandomAccessFile fileStream = new RandomAccessFile(file, "r")) {
                long length = this.endByte - this.startByte;
                byte[] bytes = new byte[(int) length];
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
    }

}
