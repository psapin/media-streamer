package gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeModel;

import control.Settings;
import control.Streamer;

public class FileBrowseFrame extends JFrame {

	private Streamer streamer;
	private JTree fileTree;
	private TreeModel treeModel;
	private File selected;
	private Socket socket;
	
	/**
	 * FileBrowseFrame - a frame that shows a file hierarchy tree from a remote host
	 * 					 opens a socket and uses NetworkFileTreeModel to interact with the remote files
	 * @param addr   the address to open the socket connection on
	 */
	public FileBrowseFrame(Streamer s, InetAddress addr) {
		streamer = s;
		try {
			System.out.println("Browsing on "+addr.getHostName()+" from "+InetAddress.getLocalHost().getHostAddress());
			socket = new Socket(addr.getHostName(), Settings.SOCKET_PORT);
			treeModel = new NetworkFileTreeModel(socket);
			/* -- for socket thread, replaced with NetworkFileTreeModel --
			ClientSocketHandler csh = new ClientSocketHandler(this, socket);
			Thread cshThread = new Thread(csh);
			cshThread.start();
			// -- model tests --
			model.getChildCount(model.getRoot());
			Object x = model.getChild(model.getRoot(), 3);
			model.isLeaf(x);*/
		} catch (Exception e) {
			e.printStackTrace();
		}
		//construct the JTree
		fileTree = new JTree(treeModel);
		fileTree.addTreeSelectionListener(new TreeSelectionListener() {
			@Override
			public void valueChanged(TreeSelectionEvent e) {
			   selected = (File)fileTree.getLastSelectedPathComponent(); 
			  }
			});
		
		//fill the main panel
		JPanel mp = new JPanel(new BorderLayout());
		JLabel topLabel = new JLabel("Please Select a file to play:");
		JScrollPane scrollpane = new JScrollPane(fileTree);
		JButton select = new JButton("Select");
		select.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //----- when media is selected ------
            	try {
            		PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            		
            		System.out.println("starting "+selected.getAbsolutePath());
            		out.println("start "+selected.getAbsolutePath());
					socket.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
            	streamer.mediaFileSelected(selected);
            	//System.out.println("Selected: " + selected);
            }
        });
		mp.add(topLabel, BorderLayout.NORTH);
		mp.add(scrollpane, BorderLayout.CENTER);
		mp.add(select, BorderLayout.SOUTH);
		
		//setup frame stuff, add main panel
		this.getContentPane().add(mp, "Center");
		this.setSize(600, 500);

	}
	
	//for ClientSocketHandler callback -- not in use --
	public void setTreeModel(FileTreeModel ftm) {
		treeModel = ftm;
		fileTree = new JTree(treeModel);
		JScrollPane scrollpane = new JScrollPane(fileTree);
		this.getContentPane().add(scrollpane, "Center");
		invalidate();
		validate();
		repaint();
	}
}
