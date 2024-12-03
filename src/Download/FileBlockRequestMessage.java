package Download;

import Communication.GlobalConfig;
import Files.FileBlockInfo;
import Files.FileInfo;

import java.io.File;
import java.io.Serializable;

public class FileBlockRequestMessage implements Serializable {
    private FileBlockInfo requestBlock;
    private String fileHash;
    private String dtmUID;
    private int blockID;

    public FileBlockRequestMessage(FileBlockInfo requestBlock, String fileHash, String dtmUID, int blockID) {
        this.blockID = blockID;
        this.requestBlock = requestBlock;
        this.fileHash = fileHash;
        this.dtmUID = dtmUID;
    }

    public int getBlockID() {
        return blockID;
    }

    public String getDtmUID(){
        return dtmUID;
    }

    public String getFileHash() {
        return fileHash;
    }

    public byte[] getBlock(){
        GlobalConfig gc = GlobalConfig.getInstance();
        File[] files = gc.getFilesInDirectory();
        for (File file : files) {
            FileInfo info = new FileInfo(file);
            if(info.filehash.equals(fileHash)){
                return requestBlock.readFileBytesInRange(file);
            }
        }
        return null;
    }

}
