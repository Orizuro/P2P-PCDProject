package Client;

import Communication.Command;
import Communication.MessageWrapper;
import Download.FileBlockAnswerMessage;
import Files.DownloadTaskManager;
import Search.FileSearchResult;

import java.io.IOException;
import java.util.*;

public class ClientManager {
    private Map<ClientThread, Boolean> clientThreads;
    private HashMap<String, List<FileSearchResult>> FileSearchDB;
    private Map<DownloadTaskManager, Boolean> downloadThreads;

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

    public synchronized void sendAll(Command command, Object message) { //Manda mensagem para todos os clients
        for (ClientThread clientThread : clientThreads.keySet()) {
            try {
                clientThreads.replace(clientThread, true);
                clientThread.sendObject(command, message);
            } catch (InterruptedException e) {
                System.out.println("Error sending message to " + clientThread.getClientName() + ": " + e.getMessage());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
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
                //TODO store files blocks in DownloadTaskManager
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

    public void startDownloadThreads(List<FileSearchResult> fileAvailable) throws IOException, InterruptedException {
        List<ClientThread> availableDownloadThreads = new ArrayList<>();
        int maxThreads = 5;
        int currentThreads = 0;
        while( currentThreads < maxThreads){
            for (FileSearchResult file : fileAvailable) {
                availableDownloadThreads.add(addClientThread( file.getIp(),file.getPort()));
                currentThreads++;
                if(currentThreads >= maxThreads){  
                    break;
                }
            }
        }

        DownloadTaskManager dtm =  new DownloadTaskManager(this, fileAvailable);
        this.downloadThreads.put(dtm, true);
        dtm.startDownload(availableDownloadThreads);
    }

}
