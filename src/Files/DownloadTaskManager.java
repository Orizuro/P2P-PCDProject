package Files;

import Client.ClientManager;
import Client.ClientThread;
import Communication.Command;
import Download.FileBlockAnswerMessage;
import Download.FileBlockRequestMessage;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

public class DownloadTaskManager extends Thread {
    private ClientManager clientManager;
    private FileInfo  fileInfo;
    private Map<Integer, Boolean> blockStatus;
    private Map<Integer, FileBlockAnswerMessage> fileData = new TreeMap<>();
    private final String uid = UUID.randomUUID().toString();
    private final AtomicLong totalTime;
    List<ClientThread> availableThreads = new ArrayList<>();
    private final ExecutorService threadPool;


    public DownloadTaskManager(ClientManager clientmanager, FileInfo  fileInfo) {
        this.clientManager = clientmanager;
        this.fileInfo = fileInfo;
        this.threadPool = Executors.newFixedThreadPool(5);
        this.totalTime = new AtomicLong(0);
        this.blockStatus = new ConcurrentHashMap<>();
    }

    @Override
    public void run() {
        int pointer = 0;
        while (blockStatus.size() != fileInfo.blockNumber){
            //System.out.println(pointer);

            for (ClientThread thread : availableThreads) {
                if (pointer >= fileInfo.blockNumber) break;
                int finalPointer = pointer;
                threadPool.execute(() -> {
                    System.out.println("Requesting block " + finalPointer);
                    blockStatus.put(finalPointer, true);
                    try {
                        clientManager.sendThread(thread, Command.DownloadMessage, new FileBlockRequestMessage(fileInfo.fileBlockManagers.get(finalPointer), fileInfo.filehash, uid, finalPointer));
                        System.out.println(thread.getClientName());
                    }catch (Exception e){
                        throw new RuntimeException(e);
                    }
                });
                pointer++;
            }
        }
        while (! isFinished()){
            try {
                sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        fileInfo.writeFile(fileData);
        for(ClientThread thread : availableThreads){
            thread.close();
        }
        System.out.println("File downloaded");
    }

    public void startDownload() {
        this.start();
    }

    public void addFileblock(int blockId, FileBlockAnswerMessage fileBlock){
        fileData.put(blockId,fileBlock);
    }
    public String getUid() {
        return uid;
    }

    public boolean isFinished(){
        return fileData.size() == fileInfo.blockNumber;
    }

    public void addDownloadThread(ClientThread clientThread) {
        availableThreads.add(clientThread);
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
