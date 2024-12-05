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
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.*;

import static java.lang.Thread.sleep;

public class MainInterface {

    private JFrame frame;
    private DefaultListModel<String> searchResultsModel; // Modelo para a JList
    ClientManager clientManager;

    public MainInterface(ClientManager clientManage) {
        this.clientManager = clientManage;
        frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // para que o botao de fechar a janela termine a aplicacao
        addFrameContent();
        frame.pack(); // para que a janela se redimensione de forma a ter tod o seu conteudo visivel
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
                String searchTerm = message.getText().trim();
                if(searchTerm.isEmpty()) {
                    return;
                }
                searchResultsModel.clear();
                clientManager.resetFileSearchDB();
                try {
                    clientManager.sendAll(Command.WordSearchMessage, new WordSearchMessage(searchTerm));
                } catch (ExecutionException ex) {
                    throw new RuntimeException(ex);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
                buttonSearch.setEnabled(false);

                SwingWorker<Void, Void> worker = new SwingWorker<>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        while (clientManager.isWaiting()) {
                            sleep(100);
                        }
                        return null;
                    }
                    @Override
                    protected void done() {
                        HashMap<String, List<FileSearchResult>> data = clientManager.getData();
                        if(data.isEmpty()) {
                            searchResultsModel.addElement("Ficheiro não encontrado");
                            buttonSearch.setEnabled(true);
                            return;
                        }
                        for(List<FileSearchResult> file : clientManager.getData().values()){
                            searchResultsModel.addElement(file.getFirst().toString() + "<" + file.size() + ">");
                        }
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
                    String modifiedString = selectedFile.substring(0, selectedFile.length() - 3);
                    clientManager.startDownloadThreads(modifiedString);
                    JOptionPane.showMessageDialog(frame, "Download iniciado com sucesso.");
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





}
