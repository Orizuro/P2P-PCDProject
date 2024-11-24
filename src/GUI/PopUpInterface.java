package GUI;

import Client.ClientManager;
import Client.ClientThread;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PopUpInterface {

    private JFrame frame;
    private DefaultListModel<String> searchResultsModel; // Modelo para a JList
    private ClientManager clientManager;

    public PopUpInterface(ClientManager clientManage) {
        this.clientManager = clientManage;
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

    private final String addressInstructions = "Endereço: ";
    private final String portInstructions = "Porta: ";
    //ClientManager clientManager = new ClientManager();

    private void addPopUpFrameContent() {

        frame.setLayout(new FlowLayout(5));

        JPanel centralPanel = new JPanel(new FlowLayout()); //para ficar centrado como o exemplo dado pelo prof tem que se usar a GridLayout, embora também possamos usar aqui a BorderLayout e ficaria melhor visualmente

        JLabel address = new JLabel(addressInstructions);
        JTextField adressSearchWindow = new JTextField(20);

        centralPanel.add(address);
        centralPanel.add(adressSearchWindow);

        JLabel port = new JLabel(portInstructions);
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
                String address = adressSearchWindow.getText();
                int node = Integer.parseInt(portSearchWindow.getText());
                if (address != null && node >= 0) {
                    ClientThread client1 = new ClientThread(clientManager, address, node);
                } else {
                    JOptionPane.showMessageDialog(frame, "Foram inseridos dados incorretos, por favor tente novamente.");
                }
            }
        });
    }

}
