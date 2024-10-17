package NameNotFound;

import java.io.Serializable;

public class FileSearchResult implements Serializable {
    private WordSearchMessage wordSearchMessage;
    private byte[] hash;
    private int fileSize;
    private String fileName;
    private String ip;
    private int port;

}
