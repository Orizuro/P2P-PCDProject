package GUI;

import Client.ClientManager;
import Client.ClientThread;
import Communication.Command;
import Search.FileSearchResult;
import Search.WordSearchMessage;
import Server.RunnableSocketServer;
import Server.SocketServer;

import Client.ClientManager;
import Client.SocketClient;
import Communication.Command;
import Search.FileSearchResult;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.*;

public class MainInterface {

    private JFrame frame;
    private DefaultListModel<String> searchResultsModel; // Modelo para a JList
    ClientManager clientManager;

    public MainInterface(ClientManager clientManage) {
        this.clientManager = clientManage;
        frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // para que o botao de fechar a janela termine a aplicacao
        addFrameContent();
        frame.pack(); // para que a janela se redimensione de forma a ter todo o seu conteudo visivel
        frame.setSize(500,300); //para iniciar a janela com um tamanho apropriado (pederá ser redimensionável)
        frame.setLocationRelativeTo(null); //para centrar a janela
    }

    public void open() {
        // para abrir a janela (torna-la visivel)
        frame.setVisible(true);
    }

    private void addFrameContent() {

        frame.setLayout(new BorderLayout());

        // Layout do Painel superior
        JPanel topPanel = new JPanel(new GridLayout(1,3)); //para ficar centrado como o exemplo dado pelo prof tem que se usar a GridLayout, embora também possamos usar aqui a BorderLayout e ficaria melhor visualmente

            JLabel instructionsSearchWindow = new JLabel("Texto a procurar: ");
            JTextField message = new JTextField("");
            JButton buttonSearch = new JButton("Procurar");

            topPanel.add(instructionsSearchWindow, BorderLayout.WEST);
            topPanel.add(message, BorderLayout.CENTER);
            topPanel.add(buttonSearch, BorderLayout.EAST);

        frame.add(topPanel, BorderLayout.NORTH);


        // Layout do Painel lateral esquerdo e central
        JPanel leftPanel = new JPanel(new BorderLayout());

        searchResultsModel = new DefaultListModel<>();
        JList<String> searchResultsList = new JList<>(searchResultsModel);

        JScrollPane scrollPane = new JScrollPane(searchResultsList);
        leftPanel.add(scrollPane, BorderLayout.CENTER);

        frame.add(leftPanel, BorderLayout.CENTER);


        // Layout do Painel lateral direito
        JPanel rightPanel = new JPanel(new GridLayout(2,1));

            JButton buttonDownload = new JButton("Descarregar");
            JButton buttonNode = new JButton("Ligar a Nó");

            rightPanel.add(buttonDownload, BorderLayout.NORTH);
            rightPanel.add(buttonNode, BorderLayout.SOUTH);

        frame.add(rightPanel, BorderLayout.EAST);


        // Action Listener do Botão "Procurar"
        buttonSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchResultsModel.clear();
                String searchTerm = message.getText().trim();
                clientManager.sendAll(Command.WordSearchMessage, new WordSearchMessage(searchTerm));
                buttonSearch.setEnabled(false);
                SwingWorker<Void, FileSearchResult> worker = new SwingWorker<>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        Thread.sleep(500);
                        List<FileSearchResult[]> list = clientManager.getData();
                        for (FileSearchResult[] resultsArray : list) {
                            for (FileSearchResult result : resultsArray) {
                                publish(result);
                            }
                        }
                        return null;
                    }
                    @Override
                    protected void process(List<FileSearchResult> chunks) {
                        for (FileSearchResult result : chunks) {
                            searchResultsModel.addElement(result.toString());
                        }
                    }
                    @Override
                    protected void done() {
                        buttonSearch.setEnabled(true);
                    }
                };

                // Execute the worker
                worker.execute();
            }
        });


        // Action Listener do Botão "Descarregar"
        buttonDownload.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedFile = searchResultsList.getSelectedValue();
                if (selectedFile != null) {
                    // (...)
                    JOptionPane.showMessageDialog(frame, "Ficheiro descarregado com sucesso");
                } else {
                    JOptionPane.showMessageDialog(frame, "Selecione um ficheiro primeiro.");
                }
            }
        });


        // Action Listener do Botão "Ligar a Nó"
        buttonNode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PopUpInterface guiPopUp = new PopUpInterface(clientManager);
                guiPopUp.open();
            }
        });
    }


    public static void main(String[] args) {
        SocketServer server =new SocketServer(6666);
        Thread thread_server = new Thread(new RunnableSocketServer(server));
        thread_server.start();
        ClientManager clientManager = new ClientManager();
        MainInterface gui = new MainInterface(clientManager);
        gui.open();
    }


}
