package gui;

import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;

import control.Streamer;


public class DiscoveryFrame extends JFrame {
	
	private Streamer s;
	private JLabel topLabel = new JLabel("");
	private PeerTable peerTable;
	private DefaultTableModel tableModel;
	private boolean choosingTargetPlayDevice; //if false, choosing target browse
	
	//takes a Streamer to use the closeDNS callback when exiting
	public DiscoveryFrame(Streamer stream) {
		s = stream;
		setTitle("Streamer");
	    setSize(600, 300);
	    setLocationRelativeTo(null);
	    this.addWindowListener(new java.awt.event.WindowAdapter() {
	        @Override
	        public void windowClosing(java.awt.event.WindowEvent windowEvent) {
	            s.closeDNSThread();
	        	System.exit(0);
	        }
	    });
	    peerTable = new PeerTable();
		JPanel mainP = createWindow();
		choosingTargetPlayDevice = true;
		setTopLabelText("Choose a device to play media on:");
		this.add(mainP);
	}

	private JPanel createWindow() {
		//main panel (centered)
		JPanel p = new JPanel();
		LayoutManager lm = new FlowLayout(FlowLayout.CENTER);
		p.setLayout(lm);
		//vertical flow panel
		JPanel ip = new JPanel();
		LayoutManager ilm = new BoxLayout(ip, BoxLayout.Y_AXIS);
		ip.setLayout(ilm);
		
		JScrollPane sp = new JScrollPane(peerTable);
		
		JButton selectButton = new JButton("Select Device");
		selectButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                //Execute when button is pressed
            	int selectedRow = peerTable.getSelectedRow();
            	if(selectedRow < 0 || selectedRow >= peerTable.getRowCount()) return;
            	String deviceId = (String) peerTable.getValueAt(selectedRow, 2);
            	if(deviceId != null && deviceId != "") {
                    System.out.println("selected: "+deviceId);
                    if(choosingTargetPlayDevice)
                    	s.setTargetPlayDeviceAddress(peerTable.getInetAddress(deviceId));
                    else {
                    	s.setTargetFileBrowseDeviceAddress(peerTable.getInetAddress(deviceId));
                    	//file browse frame opening goes here! (call a function from s)
                    	s.browse();
                    }
                    choosingTargetPlayDevice = false;
                    setTopLabelText("Choose a device to browse for media on: ");
            	}
            }
        });
		//add it all to the panel
		ip.add(topLabel);
		ip.add(sp);
		ip.add(selectButton);
		p.add(ip);
		return p;
	}
	
	public void setTopLabelText(String text) {
		topLabel.setText(text);
	}
	
	public PeerTable getPeerTable() {
		return peerTable;
	}
	
	
}
