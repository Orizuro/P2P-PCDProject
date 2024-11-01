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
    private ObjectOutputStream out;    // FLuxo de saída de dados
    private ObjectInputStream in;    // FLuxo de entrada de dados
    private final String ip;
    private final int port;
    private boolean ready = false;  // Para gerir o estado da comunicação

    public SocketClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public synchronized void startSocket(){
        try{
            this.clientSocket = new Socket(ip, port);   // Conecta-se ao servidor com determinado IP e por uma determinada porta
            System.out.println("Connected to " + ip + ":" + port);
            this.out = new ObjectOutputStream(clientSocket.getOutputStream());  // Iniciado o fluxo de saída
            this.in = new ObjectInputStream(clientSocket.getInputStream());   // Iniciadlo o fluxo de entrada
            this.ready = true;  // Definida a conexão como pronta
            notify();
        }catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void sendObject(Command command,  Object object) throws IOException, InterruptedException {
        while (!ready) {    // Aguarda até que a conexão esteja pronta
            wait();
        }
        MessageWrapper message = new MessageWrapper(this.ip, command, object); // "Embrulha" e envia uma mensagem
        this.out.writeObject(message);   // Envia a mensagem
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
        sendObject(Terminate,null);    // Envia o comando "Já acabei"
        Thread.sleep(100);   // Espera 100 milisegundos antes de fechar o SocketClient
        this.clientSocket.close();   // Fecha o SocketClient
        System.out.println("Connection closed client");
    }

    /*
     try {
            message = (MessageWrapper) in.readObject();    // Recebe e "desembrulha" uma mensagem
            switch (message.getCommand()) {
                case Command.FileSearchResult: {
                    FileSearchResult[] data =  (FileSearchResult[])  message.getData();    // Converte os dados recebidos para um ficheiro/uma lista de FileSearchResult
                    for (FileSearchResult file : data) {
                        System.out.println(file.toString());    // Imprime os resultados/os dados do ficheiro FileSearchResult
                    }
                    break;
                }
                default: {
                    System.out.println(message.getData().toString());     // Exibe outro tipo de dados (?sasha? - que outro tipo de dados são estes?)
                    break;
                }
            }
        }
     */

}

