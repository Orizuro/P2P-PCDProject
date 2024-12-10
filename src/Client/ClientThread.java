package Client;

import Communication.Command;

import java.io.IOException;
import java.util.UUID;

public class ClientThread extends Thread implements Comparable<ClientThread> {
    private final SocketClient socketClient;
    private final String clientName;
    private final ClientManager clientManager;
    private volatile boolean isRunning = true;

    public ClientThread(ClientManager clientManager, String ip, int port) {
        this.clientManager = clientManager;
        this.socketClient = new SocketClient(ip, port);
        this.clientName = UUID.randomUUID().toString();
        socketClient.startSocket();
        this.start();
    }

    @Override
    public void run() {
        try {
            while (isRunning) {
                this.clientManager.receive(socketClient.receiveObject(), this);
            }
        } catch (InterruptedException e) {
            System.out.println(clientName + " has been interrupted.");
        } catch (Exception e) {
            System.out.println("Error in " + clientName + ": " + e.getMessage());
        }
    }

    public synchronized void sendObject(Command command, Object message) throws IOException, InterruptedException {
        socketClient.sendObject(command, message);
    }

    public String getClientName() {
        return clientName;
    }

    public void terminate() throws IOException, InterruptedException {
        isRunning = false; // Signal the thread to stop.
        interrupt(); // Interrupt the thread if it's blocked.
        socketClient.stopConnection();
        System.out.println(clientName + " has been terminated.");
    }

    @Override
    public int compareTo(ClientThread other) {
        return this.clientName.compareTo(other.clientName);
    }


}
