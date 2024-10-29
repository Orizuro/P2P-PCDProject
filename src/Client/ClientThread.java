package Client;

import Communication.Command;

import java.io.IOException;

public class ClientThread extends Thread {
    private final SocketClient socketClient;
    private final String clientName; // Client's name for identification
    private final ClientManager clientManager; // Reference to the client manager

    public ClientThread(ClientManager clientManager, String ip, int port, String clientName) {
        this.clientManager = clientManager;
        this.socketClient = new SocketClient(ip, port);
        this.clientName = clientName;

        // Register this thread with the client manager
        clientManager.addClientThread(this);
        socketClient.startSocket();
        this.start();
    }

    @Override
    public void run() {
         // Start the socket connection
        try {
            while (true) {
                // Simulate listening for messages from the server
                this.clientManager.receiveAll(socketClient.receiveObject());


            }
        } catch (InterruptedException e) {
            System.out.println(clientName + " has been interrupted.");
        } catch (Exception e) {
            System.out.println("Error in " + clientName + ": " + e.getMessage());
        } finally {
            // Remove this client thread from the manager when done
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
}
