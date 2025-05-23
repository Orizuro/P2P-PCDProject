package Files;

import Client.ClientManager;
import Client.ClientManagerListener;
import Client.ClientThread;
import Communication.Command;
import Download.FileBlockAnswerMessage;
import Download.FileBlockRequestMessage;
import Search.FileSearchResult;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class DownloadTaskManager extends Thread {
    private final ClientManager clientManager;
    private final FileInfo  fileInfo;
    private final Map<Integer, Boolean> blockStatus;
    private final Map<Integer, FileBlockAnswerMessage> fileData = new TreeMap<>();
    private long totalTime;
    public List<FileSearchResult> availableNodes;

    private final String uid = UUID.randomUUID().toString();
    List<ClientThread> availableThreads = new ArrayList<>();
    private final int numberThreads = 5;
    ExecutorService threadPool = Executors.newFixedThreadPool(numberThreads);
    private final List<DownloadTaskManagerListener> listeners = new ArrayList<>();

    private final Map<String, Integer> blocksPerNode = new HashMap<>(); // Chave: "IP:Porta", Valor: contagem de blocos


    public DownloadTaskManager(ClientManager clientmanager, FileInfo  fileInfo, List<FileSearchResult> nodes) {
        this.clientManager = clientmanager;
        this.fileInfo = fileInfo;
        this.totalTime = 0;
        this.blockStatus = new ConcurrentHashMap<>();
        this.availableNodes = nodes;
    }

    @Override
    public void run() {
        System.out.println("File blocks :" + fileInfo.blockNumber);
        Random random = new Random();
        totalTime = System.currentTimeMillis();
        List<ClientThread> availableThreads = new ArrayList<>();
        for ( int i = 0; i < numberThreads; i++ ) {
            FileSearchResult node = availableNodes.get(random.nextInt(availableNodes.size()));
            availableThreads.add(new ClientThread( clientManager,node.getIp() , node.getPort()));
        }
        for(int pointer = 0; pointer < fileInfo.blockNumber ; pointer++){
            int finalPointer = pointer;
            blockStatus.put(finalPointer, true);
            threadPool.execute(() -> {
                ClientThread thread = availableThreads.get((fileInfo.blockNumber - finalPointer) % numberThreads);
                System.out.println((fileInfo.blockNumber - finalPointer) % numberThreads);
                try {
                    thread.sendObject( Command.DownloadMessage,
                            new FileBlockRequestMessage(fileInfo.fileBlockManagers.get(finalPointer),
                                    fileInfo.filehash, uid, finalPointer));
                } catch (IOException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        while(!isFinished()){
            try {
                sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        totalTime = System.currentTimeMillis() - totalTime;
        fileInfo.writeFile(fileData);
        System.out.println("File downloaded");

        threadPool.shutdown();
        for(ClientThread thread : availableThreads){
            try {
                thread.terminate();
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public float getTotalTime(){
        return (float) totalTime / 1000;
    }

    public String getUid() {
        return uid;
    }

    public int getTotalBlocks(){
        return fileInfo.blockNumber;
    }

    public void startDownload() {
        this.start();
    }

    public void addFileblock(int blockId, FileBlockAnswerMessage fileBlock){
        fileData.put(blockId,fileBlock);
        String nodeKey = fileBlock.getSenderIP() + ":" + fileBlock.getSenderPort();
        blocksPerNode.merge(nodeKey, 1, Integer::sum); // Incrementa dos blocos descarregados por nó
        notifyListeners(fileData.size());
    }

    // Novo métosdo para obter estatísticas (usado pela GUI):
    public Map<String, Integer> getBlocksPerNodeStats() {
        return new HashMap<>(blocksPerNode); // Retorna cópia para evitar concorrência
    }


    public boolean isFinished(){
        return fileData.size() == fileInfo.blockNumber;
    }

    public void addDownloadThread(ClientThread clientThread) {
        System.out.println("Adding thread " + availableThreads.size());
        availableThreads.add(clientThread);
    }

    public void addListener(DownloadTaskManagerListener listener) {
        listeners.add(listener);
    }


    private void notifyListeners(int blockSize) {
        for (DownloadTaskManagerListener listener : listeners) {
            float percentage = (float) blockSize /fileInfo.blockNumber;
            listener.onRequestComplete(fileInfo.name, (int) (percentage * 100));
        }
    }
}
