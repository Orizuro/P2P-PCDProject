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
    private final Map<ClientThread, Boolean> clientThreads;
    private final HashMap<String, List<FileSearchResult>> FileSearchDB;
    private final Map<String,DownloadTaskManager> downloadThreads = new HashMap<>();
    private final ExecutorService threadPool = Executors.newFixedThreadPool(5); // Configurable pool size
    private final List<ClientManagerListener> listeners = new ArrayList<>();


    public ClientManager() {
        this.clientThreads = new TreeMap<>();
        this.FileSearchDB = new HashMap<>();
    }

    public void addClientThread(String ip, int port) {
        ClientThread newThread = new ClientThread(this, ip, port);
        clientThreads.put(newThread, false);
    }

    public void removeClientThread(ClientThread clientThread) {
        clientThreads.remove(clientThread);
    }

    public void sendAll(Command command, Object message) {
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

    public  void receive(MessageWrapper message, ClientThread clientThread) {
        switch (message.getCommand()) {
            case Command.FileSearchResult: {
                FileSearchResult[] received = (FileSearchResult[])  message.getData();
                for (FileSearchResult file : received) {
                    addToFileSearchResult(file);
                }
                clientThreads.replace(clientThread, false);
                break;
            }
            case Command.DownloadResult:{
                FileBlockAnswerMessage received = (FileBlockAnswerMessage)  message.getData();
                System.out.println("Cliente received block :" + received.getBlockId());
                downloadThreads.get(received.getDtmUID()).addFileblock(received.getBlockId(),received);
                break;
            }
            default: {
                System.out.println(message.getData().toString() + Thread.currentThread().getName());
                break;
            }
        }

    }

    public  HashMap<String, List<FileSearchResult>> getData() {
        return this.FileSearchDB;
    }

    private void addToFileSearchResult(FileSearchResult file) {
        if(this.FileSearchDB.containsKey(file.getFileInfo().filehash)){
            this.FileSearchDB.get(file.getFileInfo().filehash).add(file);
        }else{
            this.FileSearchDB.put(file.getFileInfo().filehash, new ArrayList<>() {{ add(file);}});
        }
        notifyListeners();
    }

    public void resetFileSearchDB(){
        this.FileSearchDB.clear();
    }

    public String searchFileByName(String name){
        for(List<FileSearchResult> fs   : FileSearchDB.values() ){
            if(fs.getFirst().getFileInfo().name.equals(name)){
                return fs.getFirst().getFileInfo().filehash;
            }
        }
        return null;
    }

    public DownloadTaskManager startDownloadThreads(String name) {
        List<FileSearchResult> fsr = FileSearchDB.get(searchFileByName(name));
        DownloadTaskManager dtm = new DownloadTaskManager(this, fsr.getFirst().getFileInfo(),fsr);
        this.downloadThreads.put(dtm.getUid(), dtm);
        dtm.startDownload();
        return dtm;

    }

    public void addListener(ClientManagerListener listener) {
        listeners.add(listener);
    }

    private void notifyListeners() {
        for (ClientManagerListener listener : listeners) {
            listener.onRequestComplete();
        }
    }

}
