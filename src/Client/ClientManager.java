package Client;

import Communication.Command;
import Communication.MessageWrapper;
import Download.FileBlockAnswerMessage;
import Files.DownloadTaskManager;
import Search.FileSearchResult;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientManager {
    private Map<ClientThread, Boolean> clientThreads;
    private HashMap<String, List<FileSearchResult>> FileSearchDB;
    private Map<String,DownloadTaskManager> downloadThreads = new HashMap<>();
    private final ExecutorService threadPool = Executors.newFixedThreadPool(5); // Configurable pool size


    public ClientManager() {
        this.clientThreads = new TreeMap<>();
        this.FileSearchDB = new HashMap<String, List<FileSearchResult>>();
    }

    public synchronized ClientThread addClientThread(String ip, int port) {
        ClientThread newThread = new ClientThread(this, ip, port);
        clientThreads.put(newThread, false);
        return newThread;
    }

    public synchronized void removeClientThread(ClientThread clientThread) {
        clientThreads.remove(clientThread);
    }

    public synchronized void sendAll(Command command, Object message) {
        for (ClientThread clientThread : clientThreads.keySet()) {
            threadPool.submit(() -> {
                try {
                    clientThreads.replace(clientThread, true);
                    clientThread.sendObject(command, message);
                } catch (InterruptedException | IOException e) {
                    System.out.println("Error sending message to " + clientThread.getClientName() + ": " + e.getMessage());
                } finally {
                    clientThreads.replace(clientThread, false);
                }
            });
        }
    }


    public synchronized void sendThread(ClientThread clientThread,Command command, Object message) throws IOException, InterruptedException {
        clientThreads.replace(clientThread, true);
        clientThread.sendObject(command,message);
    }


    public synchronized void receive(MessageWrapper message, ClientThread clientThread) {
        switch (message.getCommand()) {
            case Command.FileSearchResult: {
                FileSearchResult[] received = (FileSearchResult[])  message.getData();
                for (FileSearchResult file : received) {
                    addToFileSearchResult(file);
                }
                //clientThread.terminate();
                clientThreads.replace(clientThread, false);
                break;
            }
            case Command.DownloadResult:{
                FileBlockAnswerMessage received = (FileBlockAnswerMessage)  message.getData();
                System.out.println("Cliente received block :" + received.getBlockId());
                clientThreads.replace(clientThread, false);
                downloadThreads.get(received.getDtmUID()).addFileblock(received.getBlockId(),received);
                break;
            }
            default: {
                System.out.println(message.getData().toString() + Thread.currentThread().getName());
                //clientThreads.replace(clientThread, false);
                break;
            }
        }

    }

    public synchronized HashMap<String, List<FileSearchResult>> getData() {
        return this.FileSearchDB;
    }

    private void addToFileSearchResult(FileSearchResult file) {
        if(this.FileSearchDB.containsKey(file.getFileInfo().filehash)){
            this.FileSearchDB.get(file.getFileInfo().filehash).add(file);
        }else{
            this.FileSearchDB.put(file.getFileInfo().filehash, new ArrayList<FileSearchResult>() {{ add(file);}});
        }
    }

    public void resetFileSearchDB(){
        this.FileSearchDB.clear();
    }

    public boolean isWaiting() {
        return clientThreads.containsValue(true);
    }

    public boolean isThreadBusy(ClientThread clientThread) {
        return this.clientThreads.get(clientThread);
    }

    public String searchFileByName(String name){
        for(List<FileSearchResult> fs   : FileSearchDB.values() ){
            if(fs.getFirst().getFileInfo().name.equals(name)){
                return fs.getFirst().getFileInfo().filehash;
            }
        }
        return null;
    }

    public void startDownloadThreads(String name) {
        List<FileSearchResult> fsr = FileSearchDB.get(searchFileByName(name));
        ExecutorService threadPool = Executors.newFixedThreadPool(5); // Create a thread pool with max 5 threads

        DownloadTaskManager dtm = new DownloadTaskManager(this, fsr.get(0).getFileInfo());
        this.downloadThreads.put(dtm.getUid(), dtm);

        for (FileSearchResult file : fsr) {
            threadPool.execute(() -> {
                ClientThread clientThread = addClientThread(file.getIp(), file.getPort());
                dtm.addDownloadThread(clientThread);
            });
        }
        dtm.startDownload();
        threadPool.shutdown();
    }

}
