package Client;

import Communication.Command;
import Communication.MessageWrapper;
import Search.FileSearchResult;

import java.io.IOException;
import java.util.*;

public class ClientManager {
    private final Map<ClientThread, Boolean> clientThreads;
    private HashMap<String, List<FileSearchResult>> FileSearchDB;

    public ClientManager() {
        this.clientThreads = new TreeMap<>();
        this.FileSearchDB = new HashMap<String, List<FileSearchResult>>();
    }

    public synchronized void addClientThread(ClientThread clientThread) {
        clientThreads.put(clientThread, false);
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

    public synchronized void receive(MessageWrapper message, ClientThread clientThread) {
        clientThreads.replace(clientThread, false);
        switch (message.getCommand()) {
            case Command.FileSearchResult: {
                FileSearchResult[] received = (FileSearchResult[])  message.getData();
                for (FileSearchResult file : received) {
                    addToFileSearchResult(file);
                }
                break;
            }
            default: {
                System.out.println(message.getData().toString() + Thread.currentThread().getName());
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

}
