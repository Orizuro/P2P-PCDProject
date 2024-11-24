package Search;

import Files.FileInfo;

import java.io.Serializable;

public class FileSearchResult implements Serializable {
    private WordSearchMessage wordSearchMessage;
    private FileInfo fileInfo;
    private String ip;
    private int port;


    public FileSearchResult(WordSearchMessage wordSearchMessage, FileInfo fileManager, String ip, int port) {
        this.wordSearchMessage = wordSearchMessage;
        this.fileInfo = fileManager;
        this.ip = ip;
        this.port = port;
    }

    public FileInfo getFileInfo(){
        return this.fileInfo;
    }

    @Override
    public String toString() {
        return (fileInfo.name);
    }

    /*
    Debug porpose
    @Override
    public String toString() {
        return ( "Word:" + wordSearchMessage.getMessage() + " |Hash:" + fileManager.filehash + " |FileSize:" + fileManager.fileSize + " |FileName:" + fileManager.name + " |IP:" + ip + " |Port:" + port  );
    }

     */
}
