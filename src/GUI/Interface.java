package GUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class Interface {

    private JFrame frame;
    private JFrame popFrame;
    private DefaultListModel<String> searchResultsModel; // Modelo para a JList

    public Interface() {
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
            JTextField textSearchWindow = new JTextField(" ");
            JButton buttonSearch = new JButton("Procurar");

            topPanel.add(instructionsSearchWindow, BorderLayout.WEST);
            topPanel.add(textSearchWindow, BorderLayout.CENTER);
            topPanel.add(buttonSearch, BorderLayout.EAST);

        frame.add(topPanel, BorderLayout.NORTH);


        // Layout do Painel lateral direito
        JPanel rightPanel = new JPanel(new GridLayout(2,1));

            JButton buttonDownload = new JButton("Descarregar");
            JButton buttonNode = new JButton("Ligar a Nó");

            rightPanel.add(buttonDownload, BorderLayout.NORTH);
            rightPanel.add(buttonNode, BorderLayout.SOUTH);

        frame.add(rightPanel, BorderLayout.EAST);


        // Layout do Painel lateral esquerdo e central
        JPanel leftPanel = new JPanel(new BorderLayout());

            searchResultsModel = new DefaultListModel<>();
            JList<String> searchResultsList = new JList<>(searchResultsModel);

                JScrollPane scrollPane = new JScrollPane(searchResultsList);
                leftPanel.add(scrollPane, BorderLayout.CENTER);

        frame.add(leftPanel, BorderLayout.CENTER);


        // Action Listener do Botão "Procurar"
        buttonSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchTerm = textSearchWindow.getText();
                // (...)

            }
        });

        // Action Listener do Botão "Descarregar"
        buttonDownload.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedFile = searchResultsList.getSelectedValue();
                if (selectedFile != null) {
                    // (...)
                } else {
                    JOptionPane.showMessageDialog(frame, "Selecione um ficheiro primeiro.");
                }
            }
        });


        // Action Listener do Botão "Ligar a Nó"
        buttonNode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // (...)
            }
        });        
        
    }












    public static void main(String[] args) {
        Interface gui = new Interface();
        gui.open();
    }




}
