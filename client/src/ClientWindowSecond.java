import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ClientWindowSecond extends JFrame {

    private static final int WIDTH = 350;
    private static final int HEIGHT = 150;

    private final JTextField fieldNickname = new JTextField("Nickname");
    private final JTextField fieldIpAddr = new JTextField("localhost");
    private final JTextField fieldPort = new JTextField("8080");

    public ClientWindowSecond(ClientWindowFirst clientWindowFirst) {
        super("Input data");
        clientWindowFirst.setEnabled(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(WIDTH,HEIGHT);
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);

        JLabel titleLabel = new JLabel("Enter the necessary data");
        add(titleLabel, BorderLayout.NORTH);

        JPanel fieldInput = new JPanel(new GridLayout(3, 1));
        fieldInput.add(fieldNickname);
            fieldInput.add(fieldIpAddr);
            fieldInput.add(fieldPort);
        add(fieldInput,BorderLayout.CENTER);

        JButton enterButton = new JButton("OK");
        enterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ClientWindowFirst.nickname =fieldNickname.getText();
                ClientWindowFirst.port =Integer.parseInt(fieldPort.getText());
                ClientWindowFirst.ip_address =fieldIpAddr.getText();
                clientWindowFirst.startProcess();
                clientWindowFirst.setEnabled(true);
                ClientWindowSecond.this.dispose();
            }
        });
        add(enterButton,BorderLayout.SOUTH);
        setVisible(true);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                clientWindowFirst.setEnabled(true);
            }
        });
    }
}