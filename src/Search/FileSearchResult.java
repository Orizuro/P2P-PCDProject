package Search;

import Files.FileManager;

import java.io.Serializable;

public class FileSearchResult implements Serializable {
    private WordSearchMessage wordSearchMessage;
    private FileManager fileManager;
    private String ip;
    private int port;


    public FileSearchResult(WordSearchMessage wordSearchMessage, FileManager fileManager, String ip, int port) {
        this.wordSearchMessage = wordSearchMessage;
        this.fileManager = fileManager;
        this.ip = ip;
        this.port = port;
    }
    @Override
    public String toString() {
        return (fileManager.name);
    }

    public FileManager getFileInfo() {
        return fileManager;
    }

    /*
    Debug porpose
    @Override
    public String toString() {
        return ( "Word:" + wordSearchMessage.getMessage() + " |Hash:" + fileManager.filehash + " |FileSize:" + fileManager.fileSize + " |FileName:" + fileManager.name + " |IP:" + ip + " |Port:" + port  );
    }

     */
}
