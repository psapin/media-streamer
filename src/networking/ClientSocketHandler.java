package networking;

import gui.FileBrowseFrame;
import gui.FileTreeModel;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientSocketHandler implements Runnable{

	private Socket socket;
	private FileBrowseFrame fbf;

    public ClientSocketHandler(FileBrowseFrame frame, Socket s) {
        this.socket = s;
        this.fbf = frame;
    }
	
	
	@Override
	public void run() {
		
		try {
			System.out.println("running client socket");
			ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
			//System.out.println("made inputstream, making outputstream");
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			System.out.println("writing getTree");
			out.writeObject("getTree");
			fbf.setTreeModel((FileTreeModel)in.readObject());

			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
