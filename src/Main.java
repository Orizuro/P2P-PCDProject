import Client.ClientManager;
import Client.ClientThread;
import Communication.Command;
import Files.FileManager;
import Search.WordSearchMessage;
import Server.RunnableSocketServer;
import Server.SocketServer;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        //MainInterface gui = new MainInterface();
        //gui.open();
        //Files.FileManager fileManager = new Files.FileManager("./img.png");
        //fileManager.readAllFiles();
        //fileManager.splitFile();
        //fileManager.saveFileData();
        //Files.FileManager fileManagers = new Files.FileManager(new File("./"));
        //System.out.println( fileManagers.fileBlocks[0].toString());
        Thread.sleep(2000);
        //testsocket();
        //testfilesearch("Enunciado");
        testmultiplefilesearch("Enunc");
        //testFiels();
    }
    public synchronized static void testFiels() {
        File file = new File("./img.png");
        FileManager fileManager = new FileManager(file);

    }

    public static void testmultiplefilesearch(String word) throws IOException, InterruptedException {
        System.out.println("Running in thread: " + Thread.currentThread().getName());
        SocketServer server =new SocketServer(6666);

        Thread thread_server = new Thread(new RunnableSocketServer(server));

        thread_server.start();


        ClientManager clientManager = new ClientManager();

        // Create and start client threads
        ClientThread client1 = new ClientThread(clientManager, "127.0.0.1", 6666);
        ClientThread client2 = new ClientThread(clientManager, "127.0.0.1", 6666);
        ClientThread client3 = new ClientThread(clientManager, "127.0.0.1", 6666);



        // Example of sending a message to all clients after some time
        try {
            Thread.sleep(1000); // Wait for 2 seconds before sending a broadcast message
            clientManager.sendAll(Command.WordSearchMessage, new WordSearchMessage("i"));
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println("Main thread interrupted.");
        }
        //Client Portion
        System.out.println("Main thread asking ClientManager");
        /*
        for (FileSearchResult[] file : clientManager.getData()) {
            for(FileSearchResult result : file) {
                System.out.println(result.toString());
            }
        }

         */
        System.out.println(Thread.currentThread().getName());

    }


}