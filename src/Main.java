import Client.ClientManager;
import Communication.Command;
import Communication.GlobalConfig;
import Files.FileInfo;
import Search.FileSearchResult;
import Search.WordSearchMessage;
import Server.RunnableSocketServer;
import Server.SocketServer;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        //MainInterface gui = new MainInterface();
        //gui.open();
        //Files.SearchTaskManager fileManager = new Files.SearchTaskManager("./gmi.png");
        //fileManager.readAllFiles();
        //fileManager.splitFile();
        //fileManager.saveFileData();
        //Files.SearchTaskManager fileManagers = new Files.SearchTaskManager(new File("./"));
        //System.out.println( fileManagers.fileBlocks[0].toString());
        Thread.sleep(2000);
        //testsocket();
        //testfilesearch("Enunciado");
        testmultiplefilesearch("Enunc");
        //testFiels();
    }
    public synchronized static void testFiels() {
        File file = new File("./img.png");
        FileInfo searchTaskManager = new FileInfo(file);

    }

    public static void testmultiplefilesearch(String word) throws IOException, InterruptedException {
        GlobalConfig gc = GlobalConfig.getInstance();
        System.out.println("Running in thread: " + Thread.currentThread().getName());
        SocketServer server =new SocketServer(6666);

        Thread thread_server = new Thread(new RunnableSocketServer(server));

        thread_server.start();


        ClientManager clientManager = new ClientManager();

        clientManager.addClientThread("127.0.0.1", 6666);
        clientManager.addClientThread("127.0.0.1", 6666);

        try {
            Thread.sleep(1000); // Wait for 2 seconds before sending a broadcast message
           // clientManager.sendAll(Command.WordSearchMessage, new WordSearchMessage("png"));
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println("Main thread interrupted.");
        }

        System.out.println("Main thread asking ClientManager");
        HashMap<String, List<FileSearchResult>> data = clientManager.getData();

        //TODO select file to download in gui
        List<FileSearchResult> fileavailable = data.entrySet().iterator().next().getValue();
        System.out.println(fileavailable.toString());
        //clientManager.startDownloadThreads(fileavailable);



    }


}