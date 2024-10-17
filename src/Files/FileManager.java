package Files;

import Communication.GlobalConfig;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FileManager implements Serializable {
    public File file;
    public byte[] filehash;
    int blockNumber;
    public int fileSize;
    FileBlock[] fileBlocks = null;
    final int blocksize = 102400;

    public FileManager(File file){
         File save = new File(file.getName() + ".ser");
         if(file.getName().endsWith(".ser") || file.isDirectory()){
             return;
         }
         if(save.exists()){
             try {
                 ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(save.getName()));
                 FileManager restoredFileManager = (FileManager) objectInputStream.readObject();
                 this.file = restoredFileManager.file;
                 this.fileSize = restoredFileManager.fileSize;
                 this.blockNumber = restoredFileManager.blockNumber;
                 this.filehash = restoredFileManager.filehash;
                 this.fileBlocks = restoredFileManager.fileBlocks;
             }catch (Exception e){
                 System.out.println(e);
                 e.printStackTrace();
             }
         }
         else{
             this.file = file;
             this.fileSize = (int) file.length();
             this.blockNumber = (int) Math.ceil( (double) fileSize / blocksize) ;
             this.filehash = getFileHash();
             this.fileBlocks = new FileBlock[blockNumber];
             saveFileData();
         }

     }

    void saveFileData(){
        try{
            String filename = file.getName();
            GlobalConfig gc = new GlobalConfig();
            FileOutputStream fileOutputStream = new FileOutputStream(gc.getDefaultPath() + filename + ".ser");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(this);
            objectOutputStream.close();
        }catch(IOException e){
            e.printStackTrace();
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
