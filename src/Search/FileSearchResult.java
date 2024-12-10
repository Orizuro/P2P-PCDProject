package Search;

import Files.FileInfo;

import java.io.Serializable;

public class FileSearchResult implements Serializable {
    private final WordSearchMessage wordSearchMessage;
    private final FileInfo searchTaskManager;
    private final String ip;
    private final int port;


    public FileSearchResult(WordSearchMessage wordSearchMessage, FileInfo searchTaskManager, String ip, int port) {
        this.wordSearchMessage = wordSearchMessage;
        this.searchTaskManager = searchTaskManager;
        this.ip = ip;
        this.port = port;
    }

    @Override
    public String toString() {
        return (searchTaskManager.name);
    }

    public FileInfo getFileInfo() {
        return searchTaskManager;
    }

    public String getIp(){
        return this.ip;
    }

    public int getPort(){
        return this.port;
    }

}
