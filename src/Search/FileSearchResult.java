package Search;

import Files.SearchTaskManager;

import java.io.Serializable;

public class FileSearchResult implements Serializable {
    private WordSearchMessage wordSearchMessage;
    private SearchTaskManager searchTaskManager;
    private String ip;
    private int port;


    public FileSearchResult(WordSearchMessage wordSearchMessage, SearchTaskManager searchTaskManager, String ip, int port) {
        this.wordSearchMessage = wordSearchMessage;
        this.searchTaskManager = searchTaskManager;
        this.ip = ip;
        this.port = port;
    }
    @Override
    public String toString() {
        return (searchTaskManager.name);
    }

    public SearchTaskManager getFileInfo() {
        return searchTaskManager;
    }

    /*
    Debug porpose
    @Override
    public String toString() {
        return ( "Word:" + wordSearchMessage.getMessage() + " |Hash:" + searchTaskManager.filehash + " |FileSize:" + searchTaskManager.fileSize + " |FileName:" + searchTaskManager.name + " |IP:" + ip + " |Port:" + port  );
    }

     */
}
