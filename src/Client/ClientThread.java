package Client;

import Communication.Command;

import java.io.IOException;
import java.util.UUID;

public class ClientThread extends Thread implements Comparable<ClientThread> {
    private final SocketClient socketClient;
    private final String clientName; // Client's name for identification
    private final ClientManager clientManager; // Reference to the client manager
    private String ip;
    private int port;
    private volatile boolean isRunning = true;

    public ClientThread(ClientManager clientManager, String ip, int port) {
        this.clientManager = clientManager;
        this.ip = ip;
        this.port = port;
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

    // Method to send a message to this client
    public synchronized void sendObject(Command command, Object message) throws IOException, InterruptedException {

        socketClient.sendObject(command, message);
    }

    public String getClientName() {
        return clientName;
    }

    private void cleanup() {
        clientManager.removeClientThread(this); // Unregister from the manager.
        try {
            socketClient.stopConnection(); // Close the socket connection.
        } catch (IOException | InterruptedException e) {
            System.out.println("Error closing connection for " + clientName + ": " + e.getMessage());
        }
        System.out.println(clientName + " has been safely terminated.");
    }

    public void terminateWithoutManager() throws IOException, InterruptedException {
        isRunning = false; // Signal the thread to stop.
        interrupt(); // Interrupt the thread if it's blocked.
        socketClient.stopConnection();
        System.out.println(clientName + " has been terminated.");
    }

    public void terminate() {
        isRunning = false; // Signal the thread to stop.
        interrupt(); // Interrupt the thread if it's blocked.
        cleanup(); // Ensure resources are released.
    }


    @Override
    public int compareTo(ClientThread other) {
        return this.clientName.compareTo(other.clientName);
    }


}
