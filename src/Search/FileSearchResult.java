package Search;

import Files.FileSearchManager;

import java.io.Serializable;

public class FileSearchResult implements Serializable {
    private WordSearchMessage wordSearchMessage;
    private FileSearchManager fileSearchManager;
    private String ip;
    private int port;


    public FileSearchResult(WordSearchMessage wordSearchMessage, FileSearchManager fileManager, String ip, int port) {
        this.wordSearchMessage = wordSearchMessage;
        this.fileSearchManager = fileManager;
        this.ip = ip;
        this.port = port;
    }

    public FileSearchManager getFileInfo(){
        return this.fileSearchManager;
    }

    @Override
    public String toString() {
        return (fileSearchManager.name);
    }

    /*
    Debug porpose
    @Override
    public String toString() {
        return ( "Word:" + wordSearchMessage.getMessage() + " |Hash:" + fileManager.filehash + " |FileSize:" + fileManager.fileSize + " |FileName:" + fileManager.name + " |IP:" + ip + " |Port:" + port  );
    }

     */
}
