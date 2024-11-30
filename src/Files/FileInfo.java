package Files;

import Communication.GlobalConfig;

import java.io.*;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class FileInfo implements Serializable, Comparable<FileInfo> {
    public String name;
    public String filehash; // Array que armazena o hash do arquivo
    public int blockNumber; // Número de blocos que o arquivo será dividido
    public int fileSize;
    public List<FileBlockInfo> fileBlockManagers;
    final int blocksize = 102400;

    public FileInfo(File file){
         File save = new File( file + ".ser");
         if(file.getName().endsWith(".ser") || file.isDirectory()){ // Verifica se o arquivo é um .ser ou um diretório
             return;
         }
         if(save.exists()){
             try {
                 // Cria um fluxo de entrada para ler o arquivo save
                 ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(save));
                 FileInfo restoredSearchTaskManager = (FileInfo) objectInputStream.readObject();
                 this.name = restoredSearchTaskManager.name;
                 this.fileSize = restoredSearchTaskManager.fileSize;
                 this.blockNumber = restoredSearchTaskManager.blockNumber;
                 this.filehash = restoredSearchTaskManager.filehash;
                 this.fileBlockManagers = restoredSearchTaskManager.fileBlockManagers;
             }catch (Exception e){
                 System.out.println(e);
                 e.printStackTrace();
             }
         } else{
             // Se o arquivo de salvamento não existe
             System.out.println("File does not exist");  // Atribui o arquivo recebido ao atributo
             this.name = file.getName();
             this.fileSize = (int) file.length();
             this.blockNumber = (int) Math.ceil( (double) fileSize / blocksize) ;  // Calcula o número de blocos
             this.filehash = getFileHash(file);   // Obtém o hash do arquivo
             this.fileBlockManagers = new ArrayList<>();
             splitFile(file);
             saveFileData();
         }

     }

    void saveFileData(){
        try{
            // Metodo para guardar os dados do SearchTaskManager num arquivo .ser
            String filename = this.name;
            GlobalConfig gc = GlobalConfig.getInstance();
            // Cria um fluxo de saída para guardar o objeto SearchTaskManager
            FileOutputStream fileOutputStream = new FileOutputStream(gc.getDefaultPath() + filename + ".ser");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(this);  // Serializa e escreve o objeto no arquivo
            objectOutputStream.close();
        }catch(IOException e){
            e.printStackTrace();
        }
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

    void splitFile(File file){
        for(int i = 0; i < blockNumber ; i++ ){ // Itera sobre o número de blocos
            int start = i * blocksize == 0 ? 0 : i * blocksize + 1;  // Calcula o byte inicial do bloco
            int end = Math.min(blocksize * (i + 1), this.fileSize);  // Calcula o byte final do bloco
            this.fileBlockManagers.add(new FileBlockInfo(file, start, end));
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
