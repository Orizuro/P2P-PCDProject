package Files;

import Communication.GlobalConfig;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FileManager implements Serializable {
    public File file;
    public byte[] filehash;   // Array que armazena o hash do arquivo
    int blockNumber;   // Número de blocos que o arquivo será dividido
    public int fileSize;
    FileBlock[] fileBlocks = null;
    final int blocksize = 102400;

    public FileManager(File file){
         File save = new File(file.getName() + ".ser");
         if(file.getName().endsWith(".ser") || file.isDirectory()){    // Verifica se o arquivo é um .ser ou um diretório
             return;
         }
         if(save.exists()){
             try {
                 // Cria um fluxo de entrada para ler o arquivo save
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
             // Se o arquivo de salvamento não existe
             this.file = file;     // Atribui o arquivo recebido ao atributo
             this.fileSize = (int) file.length();
             this.blockNumber = (int) Math.ceil( (double) fileSize / blocksize);    // Calcula o número de blocos
             this.filehash = getFileHash();    // Obtém o hash do arquivo
             this.fileBlocks = new FileBlock[blockNumber];
             saveFileData();
         }

     }

    void saveFileData(){
        try{
            // Metodo para guardar os dados do FileManager num arquivo .ser
            String filename = file.getName();
            GlobalConfig gc = new GlobalConfig();
            // Cria um fluxo de saída para guardar o objeto FileManager
            FileOutputStream fileOutputStream = new FileOutputStream(gc.getDefaultPath() + filename + ".ser");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(this);       // Serializa e escreve o objeto no arquivo
            objectOutputStream.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }


    private byte[] getFileHash() {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            try (FileInputStream fis = new FileInputStream(this.file)) {    // Cria um fluxo de entrada para ler o arquivo
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {    // Lê até o final do arquivo
                    digest.update(buffer, 0, bytesRead);    // Atualiza o digest com os bytes lidos
                }
            }
            return digest.digest();
        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    void splitFile(){
        for(int i = 0; i < blockNumber ; i++ ){ // Itera sobre o número de blocos
            int start = i * blocksize == 0 ? 0 : i * blocksize + 1;   // Calcula o byte inicial do bloco
            int end = Math.min(blocksize * (i + 1), this.fileSize);   // Calcula o byte final do bloco
            this.fileBlocks[i] = new FileBlock(start, end);    // Cria um novo FileBlock com os limites
            this.fileBlocks[i].hashblock(this.file);    // Calcula o hash do bloco
        }
    }

    @Override
    public String toString() {    // StringBuilder para a representação hexadecimal
        StringBuilder hexString = new StringBuilder();
        for (byte b : this.filehash) {    // Itera sobre os bytes do hash
            String hex = Integer.toHexString(0xff & b);     // Converte cada byte para representação hexadecimal
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return ("File size: " + this.fileSize + " block number: " + this.blockNumber + " hash: " + hexString);
    }


}
