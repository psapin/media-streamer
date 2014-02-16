package control;
import gui.ControlFrame;
import gui.DiscoveryFrame;
import gui.FileBrowseFrame;
import gui.PeerTable;
import gui.PlayerFrame;

import java.io.File;
import java.net.InetAddress;

import player.Player;
import player.RTSPPlayer;


public class Streamer {
	
	private DiscoveryFrame df;
	private FileBrowseFrame fbf;
	private ControlFrame cf;
	private PlayerFrame pf;
	private InetAddress targetPlayAddress;
	private InetAddress targetBrowseAddress;
	private File mediaFile;
	private Control runner;
	private RTSPPlayer rtsp;
	private Player player;
	
	public Streamer(Control r) {
		runner = r;
		player = new Player(this);
	}
	
	public void createPeerSelectFrame() {
		df = new DiscoveryFrame(this);
		df.setVisible(true);
	}
	
	//returns the peer table from the discovery frame
	public PeerTable getPeerTable() {
		return df.getPeerTable();
	}
	
	public void setTargetPlayDeviceAddress(InetAddress addr) {
		targetPlayAddress = addr;
	}
	
	public void setTargetFileBrowseDeviceAddress(InetAddress addr) {
		targetBrowseAddress = addr;
	}
	
	public void closeDNSThread() {
		runner.closeDNSThread();
	}

	public void browse() {
		fbf = new FileBrowseFrame(this, targetBrowseAddress);
		fbf.setVisible(true);
	}
	
	/**
	 * mediaFileSelected - called from FileBrowseFrame, sets the selected file and closes existing windows
	 * 					   starts playback of media file
	 * @param f	  the file that has been selected for playing
	 */
	public void mediaFileSelected(File f){
		mediaFile = f;
    	fbf.dispose();
		df.dispose();
		//System.out.println("Playing "+f.getName()+" from "+targetBrowseAddress.getHostName()+" to "+targetPlayAddress.getHostName());
		openControl();
	}
	
	/**
	 * openControl - called by mediaFileSelected, opens ControlFrame 
	 */
	private void openControl(){
		//play mediaFile
		cf = new ControlFrame(this, targetPlayAddress, mediaFile);
		cf.setVisible(true);
	}
	
	public void playMedia() {
		pf = new PlayerFrame(player);
		pf.setVisible(true);
	}
	
	public Player getPlayer() {
		return player;
	}

	public void startStream(String filePath) {
		rtsp = new RTSPPlayer(filePath);
		
	}

	public InetAddress getTargetBrowseAddress() {
		return targetBrowseAddress;
	}
	
}
