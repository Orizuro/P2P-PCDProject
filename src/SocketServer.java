import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer extends Thread {

    private ServerSocket serverSocket;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private int port;
    private boolean running = true;

    public SocketServer(int port) {
        this.port = port;
    }

    public void run(){
        try {
            serverSocket = new ServerSocket(port);
            socket = serverSocket.accept();
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Server listening on port " + port);

        while(running){
            String greeting = null;
            try {
                greeting = in.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            out.println(greeting);
        }
        System.out.println("Server closed");

    }

    public void socketStop() throws IOException {
        this.running= false;
        this.in.close();
        this.out.close();
        this.socket.close();
        this.serverSocket.close();
    }
}
