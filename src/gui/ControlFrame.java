package gui;

import java.awt.BorderLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Hashtable;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import control.Settings;
import control.Streamer;

public class ControlFrame extends JFrame {
	
	public static final int VOLUME_MIN = 0;
	public static final int VOLUME_MAX = 200;
	public static final int VOLUME_INIT = 100;
	public static final int PROGRESS_MIN = 0;
	public static final int PROGRESS_MAX = 500;
	public static final int PROGRESS_INIT = 0;
	
	private InetAddress addr;
	private Streamer s;
	private File file;
	
	private Socket socket;
	private PrintWriter out;
	private BufferedReader in;
	
	public ControlFrame(Streamer streamer, InetAddress targetPlayAddress,
			File mediaFile) {
		addr = targetPlayAddress;
		s = streamer;
		file = mediaFile;
		
		try {
			System.out.println("Controlling "+addr.getHostName()+" from "+InetAddress.getLocalHost().getHostAddress());
			socket = new Socket(addr.getHostName(), Settings.SOCKET_PORT);
			out = new PrintWriter(socket.getOutputStream(), true);
			in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		setTitle("Stream Control");
	    setSize(600, 300);
	    setLocationRelativeTo(null);
	    this.addWindowListener(new java.awt.event.WindowAdapter() {
	        @Override
	        public void windowClosing(java.awt.event.WindowEvent windowEvent) {
	            s.closeDNSThread();
	        	System.exit(0);
	        }
	    });
	    //change this to dispose on close maybe? or write new operation to close threads safely
	    this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		JPanel mainP = createWindow();
		this.add(mainP);
		
		sendCommand("openPlayer");
		//sendCommand("start "+file.getAbsolutePath());
		
	}
	
	/**
	 * createWindow - creates all the gui components into one main JPanel
	 * @return the main panel
	 */
	private JPanel createWindow() {
		//main panel (centered)
		JPanel pan = new JPanel();
		LayoutManager lm = new BorderLayout();
		pan.setLayout(lm);
		//vertical flow panel
		JPanel ip = new JPanel();
		LayoutManager ilm = new BoxLayout(ip, BoxLayout.X_AXIS);
		ip.setLayout(ilm);
		
		JButton playButton = new JButton("Play");
		playButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	sendCommand("play");
            }
        });
		JButton pauseButton = new JButton("Pause");
		pauseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	sendCommand("pause");
            }
        });
		JButton muteButton = new JButton("Mute");
		muteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	sendCommand("mute");
            }
        });
		JButton stopButton = new JButton("Stop");
		stopButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	sendCommand("stop");
            }
        });
		
		//create the volume panel
		JPanel volPan = new JPanel();
		LayoutManager volLm = new BoxLayout(volPan, BoxLayout.Y_AXIS);
		volPan.setLayout(volLm);
		volPan.add(new JLabel("Volume"));
		volPan.add(createVolumeSlider());
		
		
		//add it all to the panel
		ip.add(playButton);
		ip.add(pauseButton);
		ip.add(stopButton);
		ip.add(muteButton);
		pan.add(ip, BorderLayout.CENTER);
		pan.add(volPan, BorderLayout.EAST);
		pan.add(createProgressSlider(), BorderLayout.SOUTH);
		return pan;
	}
	
	/**
	 * sendCommand - sends a string command to the socket
	 * @param command  the command to send
	 * @return returns the output of the command
	 */
	private String sendCommand(String command) {
		try {
			out.println(command);
			String returned = in.readLine();
			System.out.println("Recieved "+returned);
			return returned;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "Nothing Read";
	}
	
	/**
	 * createVolumeSLider - creates the volume JSlider
	 */
	private JSlider createVolumeSlider() {
		JSlider volumeSlider = new JSlider(JSlider.VERTICAL,
                VOLUME_MIN, VOLUME_MAX, VOLUME_INIT);
		volumeSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				sendCommand("volume "+(int)((JSlider)e.getSource()).getValue());
			}
		});
		volumeSlider.setMajorTickSpacing(25);
		volumeSlider.setPaintTicks(true);

		//Create the label table
		Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
		labelTable.put( new Integer( 0 ), new JLabel("0%") );
		labelTable.put( new Integer( 100 ), new JLabel("100%") );
		labelTable.put( new Integer( VOLUME_MAX ), new JLabel("200%") );
		volumeSlider.setLabelTable( labelTable );
		volumeSlider.setPaintLabels(true);
		return volumeSlider;
	}
	
	/**
	 * createProgressSLider - creates the playback progress JSlider
	 */
	private JSlider createProgressSlider() {
		JSlider progressSlider = new JSlider(JSlider.HORIZONTAL,
                PROGRESS_MIN, PROGRESS_MAX, PROGRESS_INIT);
		progressSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				sendCommand("progress "+((int)((JSlider)e.getSource()).getValue()));
			}
		});
		progressSlider.setMajorTickSpacing(50);
		progressSlider.setPaintTicks(true);

		//Create the label table
		Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
		labelTable.put( new Integer( 0 ), new JLabel("00:00:00") );
		labelTable.put( new Integer( PROGRESS_MAX ), new JLabel(sendCommand("lengthHHMMSS")) );
		progressSlider.setLabelTable( labelTable );
		progressSlider.setPaintLabels(true);
		return progressSlider;
	}
}
