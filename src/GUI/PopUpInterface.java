package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PopUpInterface {

    private JFrame frame;
    private DefaultListModel<String> searchResultsModel; // Modelo para a JList

    public PopUpInterface() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // para que o botao de fechar a janela termine a aplicacao
        addPopUpFrameContent();
        frame.setSize(590,90); // janela com um tamanho fixo
        frame.setLocationRelativeTo(null); //para centrar a janela
    }


    public void open() {
        // para abrir a janela (torna-la visivel)
        frame.setVisible(true);
    }


    private void addPopUpFrameContent() {

        frame.setLayout(new FlowLayout(5));

        JPanel centralPanel = new JPanel(new FlowLayout()); //para ficar centrado como o exemplo dado pelo prof tem que se usar a GridLayout, embora também possamos usar aqui a BorderLayout e ficaria melhor visualmente

        JLabel adress = new JLabel("Endereço: ");
        JTextField adressSearchWindow = new JTextField(20);

        centralPanel.add(adress);
        centralPanel.add(adressSearchWindow);

        JLabel port = new JLabel("Porta: ");
        JTextField portSearchWindow = new JTextField(6);

        centralPanel.add(port);
        centralPanel.add(portSearchWindow);

        JButton buttonCancel = new JButton("Cancelar");
        JButton buttonOK = new JButton("OK");

        centralPanel.add(buttonCancel);
        centralPanel.add(buttonOK);

        frame.add(centralPanel, BorderLayout.CENTER);


        // Action Listener do Botão "Cancelar"
        buttonCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });


        // Action Listener do Botão "OK"
        buttonOK.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String adress = adressSearchWindow.getText();
                int port = Integer.parseInt(portSearchWindow.getText());
                // (...)
            }
        });

    }


    public static void main(String[] args) {
        PopUpInterface gui = new PopUpInterface();
        gui.open();
    }

}
