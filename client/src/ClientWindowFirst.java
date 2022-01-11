import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ClientWindowFirst extends JFrame implements ActionListener, TCPListener {

    public static String ip_address;
    public static int port;
    public static String nickname;

    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;
    private TCP connection;

    private final JTextArea log = new JTextArea();
    private final JTextField fieldInput = new JTextField();

    private ClientWindowFirst() {
        super("achat");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(WIDTH,HEIGHT);
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);

        log.setEditable(false);
        log.setLineWrap(true);

        fieldInput.addActionListener(this);

        add(log, BorderLayout.CENTER);
        add(fieldInput,BorderLayout.SOUTH);
        setVisible(true);
        new ClientWindowSecond(this);
    }

    public static void main(String[] args) { SwingUtilities.invokeLater(ClientWindowFirst::new); }

    @Override
    public void actionPerformed(ActionEvent e) {
        String msg = fieldInput.getText();
        if(msg.equals("")) return;
        fieldInput.setText(null);
        connection.sendMessage(nickname + ": " + msg);
    }

    @Override
    public void onConnectionReady(TCP tcp) {
        printMsg("Connection ready");
    }

    @Override
    public void onReceiveString(TCP tcp, String value) {
        printMsg(value);
    }

    @Override
    public void onDisconnect(TCP tcp) {
        printMsg("Connection close");
    }

    @Override
    public void onException(TCP tcp, Exception e) {
        printMsg("Connection exception: " + e);
    }

    private synchronized void printMsg(String msg){
        SwingUtilities.invokeLater(() -> {
            log.append(msg + "\n");
            log.setCaretPosition(log.getDocument().getLength());
        });
    }

    public void startProcess() {
        try {
            this.connection = new TCP(this,ip_address, port);
        } catch (IOException e) {
            this.printMsg("Connection exception: " + e);
        }
    }
}