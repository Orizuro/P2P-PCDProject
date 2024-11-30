package Files;

import Client.ClientManager;
import Client.ClientThread;
import Communication.Command;
import Download.FileBlockAnswerMessage;
import Download.FileBlockRequestMessage;
import Search.FileSearchResult;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class DownloadTaskManager {
    private ClientManager clientManager;
    private List<FileSearchResult>  fileSearchResult;
    private Map<Integer, Boolean> blockStatus;
    private Map<Integer, FileBlockAnswerMessage> fileData;
    private final AtomicLong totalTime;


    public DownloadTaskManager(ClientManager clientmanager, List<FileSearchResult>  fileSearchResult) {
        this.clientManager = clientmanager;
        this.fileSearchResult = fileSearchResult;
        this.totalTime = new AtomicLong(0);
        this.blockStatus = new ConcurrentHashMap<>();
    }

    public Thread startDownload(List<ClientThread> availableThreads) throws IOException, InterruptedException {
        //TODO Refactor to use @override run() (extends threads)
        Thread newThread = new Thread(() -> {
            FileInfo fileInfo = fileSearchResult.getFirst().getFileInfo();
            int pointer = 0;
            while (blockStatus.size() < fileInfo.blockNumber) {
                for (ClientThread thread : availableThreads) {
                    if(!clientManager.isThreadBusy(thread)){
                        System.out.println("Asking block " + pointer);
                        blockStatus.put(pointer,true);
                        try {
                            clientManager.sendThread(thread,Command.DownloadMessage, new FileBlockRequestMessage(fileInfo.fileBlockManagers.get(pointer),fileInfo.filehash,fileInfo.name, pointer));
                        } catch (IOException | InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        pointer++;
                    }
                }
            }
        });
         newThread.start();
         return newThread;

    }

    /*
    @Override
    public synchronized String toString() {
        return "FileDownloadResult{" +
                "fileName='" + fileName + '\'' +
                ", totalTime=" + totalTime +
                ", blockStats=" + blockStatus +
                ", success=" + success +
                '}';
    }
     */
}
