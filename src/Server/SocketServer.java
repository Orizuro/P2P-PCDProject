package Server;

import java.io.*;
import java.net.*;
import Communication.Command;
import Communication.MessageWrapper;

public class SocketServer extends Thread {

    private ServerSocket serverSocket;
    private final int port;
    private boolean running = true;

    public SocketServer(int port) {
        this.port = port;
    }

    public synchronized void startServer() {
        try {
            serverSocket = new ServerSocket(port);
            serverSocket.setReuseAddress(true);
            System.out.println("Server listening on port " + port);
            while (running) {
                Socket socket = serverSocket.accept();
                new Thread(() -> {
                    try {
                        System.out.println("Server accepted connection from " + socket.getRemoteSocketAddress() + " on thread" + Thread.currentThread().getName() );
                        handleClient(socket);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }).start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void handleClient(Socket socket) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
        while(running){
            try {
                MessageWrapper message = (MessageWrapper) in.readObject();
                switch (message.getCommand()) {
                    case Command.Terminate:{
                        return;
                    }
                    case Command.String:{
                        out.writeObject(message);
                        break;
                    }
                    default:{
                        System.out.println("Unknown command");
                        break;
                    }
                }

            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);

            }
        }
    }

    public synchronized void socketStop() throws IOException {
        this.running = false;
        //notifyAll();
        this.serverSocket.close();
        System.out.println("Connection closed server ");
    }
}
