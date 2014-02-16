package gui;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

public class NetworkFileTreeModel implements TreeModel {

	private String rootPath;
	private Socket socket;
	private PrintWriter out;
	private BufferedReader in;
    
    
	public NetworkFileTreeModel(Socket s) throws IOException {
		socket = s;
		out = new PrintWriter(socket.getOutputStream(), true);
		in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		
		rootPath = getNetworkRoot();
		//root = rootFile;
	}
	
	/**
	 * getNetworkRoot - finds the root file as described in the remote machine's settings
	 * @return the root file path
	 * @throws IOException
	 */
	private String getNetworkRoot() throws IOException {
		out.println("getRoot");
		String returned = in.readLine();
		//System.out.println("Recieved "+returned);
		return returned;
	}
	

	/**
	 * getChild - returns a File object of the child of the given index
	 * @param parentNode	the node whose child you want
	 * @param idx     		the index of the child you want
	 */
	@Override
	public Object getChild(Object parentNode, int idx) {
		try {
			out.println("getChild "+idx+" "+parentNode);
			String returned = in.readLine();
			//System.out.println("Recieved "+returned);
			return new File((File)parentNode, returned);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * getChildCount - returns the number of children nodes of the parent
	 * @param parentNode   the node whose number of children you want
	 */
	@Override
	public int getChildCount(Object parentNode) {
		try {
			out.println("getChildCount "+parentNode);
			String returned = in.readLine();
			//System.out.println("Recieved "+returned);
			return Integer.parseInt(returned);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return -1;
	}

	
	/**
	 * getRoot - returns the root of the tree model
	 */
	@Override
	public Object getRoot() {
		return new File(rootPath);
	}

	/**
	 * isLeaf - if it's a file, it's a leaf.
	 * @param leaf  the object whose leafyness needs checking
	 */
	@Override
	public boolean isLeaf(Object leaf) {
		try {
			out.println("isFile "+leaf);
			String returned = in.readLine();
			//System.out.println("Recieved "+returned);
			return Boolean.parseBoolean(returned);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public int getIndexOfChild(Object parentNode, Object childNode) {
		/*try {
			out.println("getIndexOfChild "+parentNode);
			String returned = in.readLine();
			System.out.println("Recieved "+returned);
			return Integer.parseInt(returned);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return -1;
		
		File pathName = (File) parentNode;
	    File childName = (File) childNode;
	    if(pathName.isDirectory())
	    {
	        String[] fileNames = pathName.list();
	        for (int i = 0; i < fileNames.length; i++) {
	            if(pathName.compareTo(childName) == 0)
	            {
	                return i;
	            }
	        }
	    }
	    return -1;*/
		return 0;
	}

	@Override
	public void removeTreeModelListener(TreeModelListener arg0) { }
	
	@Override
	public void addTreeModelListener(TreeModelListener arg0) { }

	@Override
	public void valueForPathChanged(TreePath arg0, Object arg1) { }

}
