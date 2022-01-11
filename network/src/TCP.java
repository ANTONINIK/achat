import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class TCP {
    private final Socket socket;
    private final Thread rxThread;
    private final BufferedReader in;
    private final BufferedWriter out;
    private final TCPListener eventListener;

    public TCP(TCPListener eventListener, String ipAddr, int port)throws IOException{
        this(new Socket(ipAddr,port),eventListener);
    }

    public TCP(Socket socket, TCPListener eventListener) throws IOException{
        this.socket=socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
        this.eventListener = eventListener;
        rxThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    eventListener.onConnectionReady(TCP.this);
                    while (!rxThread.isInterrupted()) {
                        eventListener.onReceiveString(TCP.this, in.readLine());
                    }
                } catch (IOException e) {
                    eventListener.onException(TCP.this,e);
                } finally {
                    eventListener.onDisconnect(TCP.this);
                }
            }
        });
        rxThread.start();
    }
    public synchronized void sendMessage(String value){
        try {
            out.write(value +"\r\n");
            out.flush();
        }
        catch (IOException e){
            eventListener.onException(TCP.this,e);
            disconnect();
        }
    }
    public synchronized void disconnect(){
        rxThread.interrupt();
        try{
            socket.close();
        } catch (IOException e){
            eventListener.onException(TCP.this,e);
        }
    }

    @Override
    public String toString(){
        return "TCP: " + socket.getInetAddress() + ": " + socket.getPort();
    }
}
