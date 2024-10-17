import java.io.*;
import java.nio.file.Files;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class FileManager implements Serializable {
     String directoryPath;
     File file;
     byte[] filehash;
     int blockNumber;
     int fileSize;
     FileBlock[] fileBlocks;
     final int blocksize = 102400;


     FileManager(File file){
         try {
             ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file.getName()));
             FileManager restoredFileManager = (FileManager) objectInputStream.readObject();
             this.directoryPath = restoredFileManager.directoryPath;
             this.file = restoredFileManager.file;
             this.fileSize = restoredFileManager.fileSize;
             this.blockNumber = restoredFileManager.blockNumber;
             this.filehash = restoredFileManager.filehash;
             this.fileBlocks = restoredFileManager.fileBlocks;
         }catch (Exception e){
             e.printStackTrace();
         }

     }

    FileManager(String directoryPath) {
        this.directoryPath = directoryPath;
        this.file = new File(directoryPath);
        this.fileSize = (int) file.length();
        this.blockNumber = (int) Math.ceil( (double) fileSize / blocksize) ;
        this.filehash = getFileHash();
        this.fileBlocks = new FileBlock[blockNumber];
    }

    void saveFileData(){
        try{
            String filename = file.getName();
            FileOutputStream fileOutputStream = new FileOutputStream(filename + ".ser");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(this);
            objectOutputStream.close();
        }catch(IOException e){
            e.printStackTrace();
        }
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
            int start = i * blocksize == 0 ? 0 : i * blocksize + 1;
            int end = Math.min(blocksize * (i + 1), this.fileSize);
            this.fileBlocks[i] = new FileBlock(start, end);
            this.fileBlocks[i].hashblock(this.file);
        }
    }
    @Override
    public String toString() {
        StringBuilder hexString = new StringBuilder();
        for (byte b : this.filehash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return ("File size: " + this.fileSize + " block number: " + this.blockNumber + " hash: " + hexString);
    }



}
