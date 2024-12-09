package Files;

public interface DownloadTaskManagerListener{
    void onRequestComplete(String fileName, Integer blockSize);
}
