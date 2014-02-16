package networking;

import java.io.BufferedReader;
import java.io.File;
import java.io.FilenameFilter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import player.Player;
import control.Settings;
import control.Streamer;

public class StreamerSocketHandler implements Runnable{

	private Socket socket;
	private FilenameFilter filter;
	private Player p;
	
    public StreamerSocketHandler(Socket sock, Streamer streamer) {
        this.socket = sock;
        p = streamer.getPlayer();
        filter = new FilenameFilter() {
		    public boolean accept(File dir, String name) {
		    	int dotIndex = name.lastIndexOf('.');
		    	if(dotIndex == -1) return true;
		    	String extension = name.substring(dotIndex);
		    	return Settings.ACCEPTED_FILETYPES.contains(extension);
		    }
		};
    }
	
	
	@Override
	public void run() {
		try{
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String inputLine, outputLine;
			while ((inputLine = in.readLine()) != null) {
				outputLine = processInput(inputLine);
                out.println(outputLine);
                if (outputLine.equals("Bye."))
                    break;
				
				//out.println("Got it - "+inputLine);
			    //System.out.println(inputLine);
			}
		}catch (Exception e) {
			System.out.println(e);
		}
	}

	/**
	 * processInput - else if chain to handle input string from socket
	 * 				  if no matching command, return input back to sender
	 * @param input   the input string recieved from the socket
	 * @return a string to be used by the client who sent the command
	 */
	private String processInput(String input) {
		//System.out.println("Processing "+input);
		if(input.equals("getRoot")) {
			return Settings.ROOT;
		} else if (input.contains("getChildCount")) {
			//System.out.println(input.substring(input.indexOf(' ')+1));
			File parent = new File(input.substring(input.indexOf(' ')+1));
			String[] childList = parent.list(filter);
			if (childList == null) return "0";
			return ""+childList.length;
		} else if (input.contains("getChild")) {
			input = input.substring(input.indexOf(' ')+1);
			int index = Integer.parseInt(input.substring(0, input.indexOf(' ')));
			File parent = new File(input.substring(input.indexOf(' ')+1));
			String[] childList = parent.list(filter);
			if ((childList == null) || (index >= childList.length)) return null;
	        return childList[index];
		} else if (input.contains("isFile")) {
			input = input.substring(input.indexOf(' ')+1);
			return ""+(new File(input).isFile());
		} else if (input.contains("start")) {
			input = input.substring(input.indexOf(' ')+1);
			return p.start(input);
		} else if (input.contains("play")) {
			return p.play();
		} else if (input.contains("pause")) {
			return p.pause();
		} else if (input.contains("mute")) {
			return p.mute();
		} else if (input.contains("stop")) {
			return p.stop();
		} else if (input.contains("volume")) {
			input = input.substring(input.indexOf(' ')+1);
			return p.setVolume(Integer.parseInt(input));
		} else if (input.contains("progress")) {
			input = input.substring(input.indexOf(' ')+1);
			return p.setProgress(Integer.parseInt(input));
		} else if (input.contains("lengthHHMMSS")) {
			return p.getMediaLength();
		} else if (input.contains("openPlayer")) {
			return p.openPlayer();
		} 
		return input;
	}

}
