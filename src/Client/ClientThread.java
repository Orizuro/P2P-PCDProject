package Client;

import Communication.Command;

import java.io.IOException;
import java.util.UUID;

public class ClientThread extends Thread implements Comparable<ClientThread> {
    private final SocketClient socketClient;
    private final String clientName; // Client's name for identification
    private final ClientManager clientManager; // Reference to the client manager

    public ClientThread(ClientManager clientManager, String ip, int port) {
        this.clientManager = clientManager;
        this.socketClient = new SocketClient(ip, port);
        this.clientName = UUID.randomUUID().toString();;

        // Register this thread with the client manager
        clientManager.addClientThread(this);
        socketClient.startSocket();
        this.start();
    }

    @Override
    public void run() {
        try {
            while (true) {
                this.clientManager.receive(socketClient.receiveObject(), this);
            }
        } catch (InterruptedException e) {
            System.out.println(clientName + " has been interrupted.");
        } catch (Exception e) {
            System.out.println("Error in " + clientName + ": " + e.getMessage());
        } finally {
            clientManager.removeClientThread(this);
            try {
                socketClient.stopConnection();
            } catch (IOException | InterruptedException e) {
                System.out.println("Error closing connection for " + clientName + ": " + e.getMessage());
            }
        }
    }

    // Method to send a message to this client
    public void sendObject(Command command, Object message) throws IOException, InterruptedException {

        socketClient.sendObject(command, message);
    }

    public String getClientName() {
        return clientName;
    }

    @Override
    public int compareTo(ClientThread other) {
        return this.clientName.compareTo(other.clientName);
    }


}
