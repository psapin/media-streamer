package networking;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import control.Streamer;

public class SocketManager implements Runnable {

	private int port;
	private boolean allowStreaming;
	private Streamer s;
	
	//constructor for port and allowStreaming settings
	public SocketManager(int portNum, boolean stream) {
		port = portNum;
		allowStreaming = stream;
	}
	
	//constructor for just port given
	public SocketManager(int portNum, Streamer streamer) {
		port = portNum;
		s = streamer;
		allowStreaming = true;
	}
	
	//default constructor
	public SocketManager() {
		port = -1;
		allowStreaming = false;
	}
	
	@Override
	public void run() {
		
		if(allowStreaming) {
			ServerSocket serverSocket = null;
			try {
			     serverSocket = new ServerSocket(port, 100);
			    while (true) { //loop forever
			        Socket socket = serverSocket.accept();
			        System.out.println("Connected, opening StreamerSocketHandler thread");
			        new Thread(new StreamerSocketHandler(socket, s)).start();    
			    }
			} catch (IOException exp) {
			    exp.printStackTrace();
			} finally {
			    try {
			        serverSocket.close();
			    } catch (Exception e) {
			    }
			}
		}
	}

}
