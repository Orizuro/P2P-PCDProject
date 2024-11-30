package Search;

import Files.FileInfo;

import java.io.Serializable;

public class FileSearchResult implements Serializable {
    private WordSearchMessage wordSearchMessage;
    private FileInfo searchTaskManager;
    private String ip;
    private int port;


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

    /*
    Debug porpose
    @Override
    public String toString() {
        return ( "Word:" + wordSearchMessage.getMessage() + " |Hash:" + searchTaskManager.filehash + " |FileSize:" + searchTaskManager.fileSize + " |FileName:" + searchTaskManager.name + " |IP:" + ip + " |Port:" + port  );
    }

     */
}
