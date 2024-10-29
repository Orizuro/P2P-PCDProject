package Files;

import Communication.GlobalConfig;

import java.io.*;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FileManager implements Serializable {
    public String name;
    public String filehash;
    int blockNumber;
    public int fileSize;
    FileBlock[] fileBlocks = null;
    final int blocksize = 102400;

    public FileManager(File file){
         File save = new File( file + ".ser");
         if(file.getName().endsWith(".ser") || file.isDirectory()){
             return;
         }
         if(save.exists()){
             try {
                 ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(save));
                 FileManager restoredFileManager = (FileManager) objectInputStream.readObject();
                 this.name = restoredFileManager.name;
                 this.fileSize = restoredFileManager.fileSize;
                 this.blockNumber = restoredFileManager.blockNumber;
                 this.filehash = restoredFileManager.filehash;
                 this.fileBlocks = restoredFileManager.fileBlocks;
             }catch (Exception e){
                 System.out.println(e);
                 e.printStackTrace();
             }
         } else{
             System.out.println("File does not exist");
             this.name = file.getName();
             this.fileSize = (int) file.length();
             this.blockNumber = (int) Math.ceil( (double) fileSize / blocksize) ;
             this.filehash = getFileHash(file);
             this.fileBlocks = new FileBlock[blockNumber];
             splitFile(file);
             saveFileData();
         }

     }

    void saveFileData(){
        try{
            String filename = this.name;
            GlobalConfig gc = new GlobalConfig();
            FileOutputStream fileOutputStream = new FileOutputStream(gc.getDefaultPath() + filename + ".ser");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(this);
            objectOutputStream.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }


    private  String getFileHash(File file) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            try (InputStream inputStream = Files.newInputStream(file.toPath())) {
                byte[] buffer = new byte[512];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    digest.update(buffer, 0, bytesRead);
                }
            }
            StringBuilder hexString = new StringBuilder();

            for (byte b : digest.digest()) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    void splitFile(File file){
        for(int i = 0; i < blockNumber ; i++ ){
            int start = i * blocksize == 0 ? 0 : i * blocksize + 1;
            int end = Math.min(blocksize * (i + 1), this.fileSize);
            this.fileBlocks[i] = new FileBlock(start, end);
            this.fileBlocks[i].hashblock(file);
        }
    }


    @Override
    public String toString() {
        return ("File size: " + this.fileSize + " block number: " + this.blockNumber + " hash: " + this.filehash);
    }



}
