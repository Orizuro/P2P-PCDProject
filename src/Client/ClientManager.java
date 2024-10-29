package Client;

import Communication.Command;
import Communication.MessageWrapper;
import NameNotFound.FileSearchResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClientManager {
    private final List<ClientThread> clientThreads;
    private final List<FileSearchResult[]> data;

    public ClientManager() {
        clientThreads = new ArrayList<>();
        data = new ArrayList<>();
    }

    public synchronized void addClientThread(ClientThread clientThread) {
        clientThreads.add(clientThread);
    }

    public synchronized void removeClientThread(ClientThread clientThread) {
        clientThreads.remove(clientThread);
    }

    public synchronized void sendAll(Command command, Object message) {
        for (ClientThread clientThread : clientThreads) {
            try {
                clientThread.sendObject(command, message);
            } catch (InterruptedException e) {
                System.out.println("Error sending message to " + clientThread.getClientName() + ": " + e.getMessage());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public synchronized void receiveAll(MessageWrapper message) {
        switch (message.getCommand()) {
            case Command.FileSearchResult: {
                FileSearchResult[] received = (FileSearchResult[])  message.getData();
                this.data.add(received);
                for (FileSearchResult file : received) {
                    System.out.println(file.toString());
                }
                System.out.println(Thread.currentThread().getName());
                break;
            }
            default: {
                System.out.println(message.getData().toString() + Thread.currentThread().getName());
                break;
            }
        }
    }

    public synchronized List<FileSearchResult[]> getData() {

        return this.data;
    }
}
