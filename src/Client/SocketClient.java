package Client;

import java.io.*;
import java.net.*;
import Communication.Command;
import Communication.MessageWrapper;
import NameNotFound.FileSearchResult;
import NameNotFound.WordSearchMessage;

import static Communication.Command.Terminate;

public class SocketClient  {

    private Socket clientSocket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private final String ip;
    private final int port;
    private boolean ready = false;

    public SocketClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public synchronized void startSocket(){
        try{
            this.clientSocket = new Socket(ip, port);
            System.out.println("Connected to " + ip + ":" + port);
            this.out = new ObjectOutputStream(clientSocket.getOutputStream());
            this.in = new ObjectInputStream(clientSocket.getInputStream());
            this.ready = true;
            notify();
        }catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public  void sendObject(Command command,  Object object) throws IOException, InterruptedException {
        while (!ready) {
            wait();
        }
        MessageWrapper message = new MessageWrapper(this.ip, command, object);
        this.out.writeObject(message);
    }

    public synchronized MessageWrapper receiveObject() throws IOException, InterruptedException {
        while (!ready) {
            wait();
        }
        MessageWrapper message = null;
        try {
            return (MessageWrapper) in.readObject();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    public synchronized void stopConnection() throws IOException, InterruptedException {

        sendObject(Terminate,null);
        Thread.sleep(100);
        this.clientSocket.close();
        System.out.println("Connection closed client");
    }

}

