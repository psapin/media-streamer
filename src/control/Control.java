package control;

import java.io.IOException;

import javax.swing.JOptionPane;

import networking.SocketManager;
import dns.JmDNSClient;
import enums.RunType;

public class Control {
	
	private Thread mdnsThread;
	private Thread smThread;

	/**
	 * start - starts up all the pieces of the program
	 * starts the dns and socket threads, streamer
	 * typing q in the console will quit
	 */
	public void start() {
		
		JmDNSClient mdns;
		Streamer s =new Streamer(this);
		SocketManager sm;
		
		int runAs = getRunConfig();
		if(runAs == RunType.STREAM.fId) { //Just Allow Streaming
			mdns = new JmDNSClient(Settings.NAME, Settings.TYPE);
		} else { //if runAs is 1 or 2
			
			s.createPeerSelectFrame();
			if(runAs == RunType.PLAY.fId)
				mdns = new JmDNSClient(s.getPeerTable());
			else
				mdns = new JmDNSClient(s.getPeerTable(), Settings.NAME, Settings.TYPE);
		}
		
		System.out.println("Starting mDNS stuff...");
		mdnsThread = new Thread(mdns);
		mdnsThread.start();
		
		System.out.println("Starting socket server...");
		sm = new SocketManager(Settings.SOCKET_PORT, s);
		smThread = new Thread(sm);
		smThread.start();
		
		
		System.out.println("Input q to quit");
		int b;
		try {
			while ((b = System.in.read()) != -1 && (char) b != 'q') {
			    /* Stub */
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mdnsThread.interrupt();
	}
	
	/**
	 * getRunConfig - asks the user how to run the program
	 * @return returns the int value of the option chosen
	 */
	private static int getRunConfig() {
		Object[] options = {"Just Allow Streaming", //0
				"Play Media",						//1
				"Play Media and Allow Streaming",}; //2
		int r = JOptionPane.showOptionDialog(null,
				"Choose What to Run: ",
				"Streamer",
				JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				null,
				options,
				options[2]);
		if (r == -1) System.exit(0);
		return r;
	}
	
	public void closeDNSThread() {
		mdnsThread.interrupt();
	}
	
	public void closeSocketThread() {
		smThread.interrupt();
	}

}
