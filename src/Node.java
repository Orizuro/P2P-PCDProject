import Client.ClientManager;
import Communication.GlobalConfig;
import GUI.MainInterface;
import Server.RunnableSocketServer;
import Server.SocketServer;

public class Node {
    public static void main(String[] args) {

        // Receives the parameters from CLI
        String host = args[0];
        int port = Integer.parseInt(args[1]);

        // Parameters given
        System.out.println("Host: " + host);
        System.out.println("Port: " + port);

        // Create a node
        SocketServer server =new SocketServer(port);
        Thread thread_server = new Thread(new RunnableSocketServer(server));
        thread_server.start();
        ClientManager clientManager = new ClientManager();

        //TODO fix documents appering in /src
        GlobalConfig gc = GlobalConfig.getInstance();
        gc.setDefaultPath("documents/" + port + "/");

        MainInterface gui = new MainInterface(clientManager);
        gui.open();
    }
}