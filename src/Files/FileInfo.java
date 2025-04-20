package Files;

import Communication.GlobalConfig;
import Download.FileBlockAnswerMessage;

import java.io.*;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FileInfo implements Serializable, Comparable<FileInfo> {
    public String name;
    public String filehash; // Array que armazena o hash do arquivo
    public int blockNumber; // Número de blocos que o arquivo será dividido
    public int fileSize;
    public List<FileBlockInfo> fileBlockManagers;
    final int blocksize = 10240;

    public FileInfo(File file){
         if( file.isDirectory()){
             return;
         }
         this.name = file.getName();
         this.fileSize = (int) file.length();
         this.blockNumber = (int) Math.ceil( (double) fileSize / blocksize) ;  // Calcula o número de blocos
         this.filehash = getFileHash(file);   // Obtém o hash do arquivo
         this.fileBlockManagers = new ArrayList<>();
         splitFile(file);

     }

    private  String getFileHash(File file) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            try (InputStream inputStream = Files.newInputStream(file.toPath())) { // Cria um fluxo de entrada para ler o arquivo
                byte[] buffer = new byte[512];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {  // Lê até o final do arquivo
                    digest.update(buffer, 0, bytesRead);   // Atualiza o digest com os bytes lidos
                }
            }
            StringBuilder hexString = new StringBuilder();   // StringBuilder para a representação hexadecimal
            for (byte b : digest.digest()) { // Itera sobre os bytes do hash
                String hex = Integer.toHexString(0xff & b); // Converte cada byte para representação hexadecimal
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

    void splitFile(File file) {
        for (int i = 0; i < blockNumber; i++) { // Iterate over the number of blocks
            int start = i * blocksize;  // Calculate the start byte of the block
            int end = Math.min(blocksize * (i + 1), this.fileSize);  // Calculate the end byte of the block
            this.fileBlockManagers.add(new FileBlockInfo(file, start, end));
        }
    }


    void writeFile(Map<Integer, FileBlockAnswerMessage> data) {
        GlobalConfig gc = GlobalConfig.getInstance();
        String outputPath = gc.getDefaultPath() + this.name; // Get the output file path
        File outputFile = new File(outputPath);

        try (OutputStream outputStream = new FileOutputStream(outputFile)) {
            for(int i = 0; i < data.size(); i++){
                byte[] blockData = data.get(i).getData();
                outputStream.write(blockData);
            }
            System.out.println("File successfully written to: " + outputPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return ("File size: " + this.fileSize + " block number: " + this.blockNumber + " hash: " + this.filehash);
    }
    @Override
    public int compareTo(FileInfo other) {
        return this.filehash.compareTo(other.filehash);
    }



}
