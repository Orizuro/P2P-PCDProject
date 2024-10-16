import java.io.*;
import java.net.*;
import java.util.Objects;

public class SocketClient extends Thread {

    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private String ip;
    private int port;

    @Override
    public void start(){
        try{
            this.clientSocket = new Socket(ip, port);
            System.out.println("Connected to " + ip + ":" + port);
            this.out = new PrintWriter(clientSocket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public SocketClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public synchronized void sendMessage(String msg) throws  IOException {
        this.out.println(msg);
        System.out.println("Sent message to " + ip + ":" + port);
    }

    public synchronized void receiveMessage() throws  IOException {
        String resp = null;
        while (in.ready()) {
            resp = in.readLine();
            System.out.println("Response: " + resp);
        }
        System.out.println("No lines in buffer");
    }

    public synchronized void stopConnection() throws IOException {
        this.in.close();
        this.out.close();
        this.clientSocket.close();
        System.out.println("Connection closed");
    }

}
