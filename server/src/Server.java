import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class Server implements TCPListener {

	private final ArrayList<TCP> connections = new ArrayList<>();

	public Server() {
		System.out.println("Server running");
		try(ServerSocket serverSocket = new ServerSocket(8080)){
			while (true){
				try {
					new TCP(serverSocket.accept(),this);
				} catch (IOException e ) {
					System.out.println("TCP exception: " + e);
				}
			}
		} catch (IOException e){
			throw new RuntimeException(e);
		}
	}


	public static void main(String[] args) {
		new Server();
	}

	@Override
	public synchronized void onConnectionReady(TCP tcp) {
		connections.add(tcp);
		sendAllConnections("Client connected: " + tcp);
	}

	@Override
	public synchronized void onReceiveString(TCP tcp, String value) {
		sendAllConnections(value);
	}

	@Override
	public synchronized void onDisconnect(TCP tcp) {
		connections.remove(tcp);
		sendAllConnections("Client disconnected: " + tcp);
	}

	@Override
	public synchronized void onException(TCP tcp, Exception e) {
		System.out.println("TCP exception: " + e);
	}

	private void sendAllConnections(String value){
		System.out.println(value);
		for(TCP in : connections) {
			in.sendMessage(value);
		}
	}
}