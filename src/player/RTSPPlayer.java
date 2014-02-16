package player;

import java.net.InetAddress;
import java.net.UnknownHostException;

import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.headless.HeadlessMediaPlayer;
import control.Settings;

public class RTSPPlayer {

	public RTSPPlayer (String media) {
		
        String options = "";
		try {
			options = formatRtspStream(InetAddress.getLocalHost().getHostAddress(), Settings.STREAM_PORT, "stream");
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}
        

		System.out.println("Streaming '" + media + "' to '" + options + "'");
		
        MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory(media);
        HeadlessMediaPlayer mediaPlayer = mediaPlayerFactory.newHeadlessMediaPlayer();
        
        //this line is why the project does not work still.
        mediaPlayer.playMedia(media,
            options,
            ":no-sout-rtp-sap",
            ":no-sout-standard-sap",
            ":sout-all",
            ":sout-keep"
        );

        System.out.println("Stream set up successfully");
        
        // Don't exit
        try {
			Thread.currentThread().join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//from a vlcj test file, formats for rtsp
	private static String formatRtspStream(String serverAddress, int serverPort, String id) {
        StringBuilder sb = new StringBuilder(60);
        sb.append(":sout=#rtp{sdp=rtsp://");
        sb.append(serverAddress);
        sb.append(':');
        sb.append(serverPort);
        sb.append('/');
        sb.append(id);
        sb.append("}");
        return sb.toString();
    }
}
