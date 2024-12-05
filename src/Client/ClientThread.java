package Client;

import Communication.Command;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class ClientThread  implements Comparable<ClientThread>{
    private final SocketClient socketClient;
    private final String clientName;
    private final ClientManager clientManager;
    private final String ip;
    private final int port;

    public ClientThread(ClientManager clientManager, String ip, int port)  {
        if (ip == null || ip.isEmpty() || port <= 0) {
            throw new IllegalArgumentException("Invalid IP address or port.");
        }
        this.clientManager = clientManager;
        this.ip = ip;
        this.port = port;
        this.socketClient = new SocketClient(ip, port);
        this.clientName = UUID.randomUUID().toString();
        socketClient.startSocket();
    }

    public CompletableFuture<Object> sendAndWaitAsync(Command command, Object message) {
        CompletableFuture<Object> future = new CompletableFuture<>();

        new Thread(() -> {
            try {
                synchronized (socketClient) {
                    socketClient.sendObject(command, message);
                    Object response = socketClient.receiveObject();
                    future.complete(response);
                }
            } catch (Exception e) {
                future.completeExceptionally(e);
            }
        }).start();

        return future;
    }

    public void sendObject(Command command, Object message) throws IOException, InterruptedException {
        synchronized (socketClient) {
            socketClient.sendObject(command, message);
        }
    }

    public String getClientName() {
        return clientName;
    }

    public void close() {
        try {
            socketClient.stopConnection();
            System.out.println(clientName + " connection closed.");
        } catch (IOException | InterruptedException e) {
            System.err.println("Error closing connection for " + clientName + ": " + e.getMessage());
        }
    }

    @Override
    public int compareTo(ClientThread other) {
        return this.clientName.compareTo(other.clientName);
    }
}
