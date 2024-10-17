package NameNotFound;

import Files.FileManager;

import java.io.Serializable;

public class FileSearchResult implements Serializable {
    private WordSearchMessage wordSearchMessage;
    private byte[] hash;
    private int fileSize;
    private String fileName;
    private String ip;
    private int port;


    public FileSearchResult(WordSearchMessage wordSearchMessage, FileManager fileManager, String ip, int port) {
        this.wordSearchMessage = wordSearchMessage;
        this.hash = fileManager.filehash;
        this.fileSize = fileManager.fileSize;
        this.fileName = fileManager.file.getName();
        this.ip = ip;
        this.port = port;
    }

    @Override
    public String toString() {
        return ( "Word:" + wordSearchMessage.getWord() + " |Hash:" + hash + " |FileSize:" + fileSize + " |FileName:" + fileName + " |IP:" + ip + " |Port:" + port  );
    }
}
