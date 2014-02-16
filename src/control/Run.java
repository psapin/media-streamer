package control;

import com.sun.jna.NativeLibrary;

public class Run {
	public static void main(String args[]) {
		//if on OS X
		if(System.getProperty("os.name").equals("Mac OS X"))
			NativeLibrary.addSearchPath("vlc", "/Applications/VLC.app/Contents/MacOS/lib");
		
		Control run = new Control();
		run.start();
	}
}
