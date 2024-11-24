package Search;

import Files.FileSearchManager;

import java.io.Serializable;

public class FileSearchResult implements Serializable {
    private WordSearchMessage wordSearchMessage;
    private FileSearchManager fileSearchManager;
    private String ip;
    private int port;


    public FileSearchResult(WordSearchMessage wordSearchMessage, FileSearchManager fileSearchManager, String ip, int port) {
        this.wordSearchMessage = wordSearchMessage;
        this.fileSearchManager = fileSearchManager;
        this.ip = ip;
        this.port = port;
    }
    @Override
    public String toString() {
        return (fileSearchManager.name);
    }

    /*
    Debug porpose
    @Override
    public String toString() {
        return ( "Word:" + wordSearchMessage.getMessage() + " |Hash:" + fileSearchManager.filehash + " |FileSize:" + fileSearchManager.fileSize + " |FileName:" + fileSearchManager.name + " |IP:" + ip + " |Port:" + port  );
    }

     */
}
