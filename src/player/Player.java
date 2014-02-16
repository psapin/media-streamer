package player;

import java.net.InetAddress;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.mrl.RtspMrl;
import uk.co.caprica.vlcj.player.MediaMeta;
import uk.co.caprica.vlcj.player.MediaPlayer;
import control.Settings;
import control.Streamer;


public class Player {

	private final EmbeddedMediaPlayerComponent mpc;
	private MediaPlayer mp;
	private Streamer s;
	
	public Player(Streamer streamer) {
		s = streamer;
		mpc = new EmbeddedMediaPlayerComponent();
		mp = mpc.getMediaPlayer();
		/*//marquee attempt
		mp.enableMarquee(true);
		mp.setMarqueeSize(100);
		mp.setMarqueeColour(Color.WHITE);
		mp.setMarqueeTimeout(2000);
		mp.setMarqueePosition(libvlc_marquee_position_e.bottom);*/
	}
	
	/**
	 * getTargetMRL - formats a new MRL for the playback device to point to
	 * @return mrl - the mrl to play from
	 */
	private String getTargetMRL() {
		//InetAddress addr = s.getTargetBrowseAddress();
		//String mrl = "rtsp://"+addr.getHostAddress();
		//String stuff = ':'+Settings.STREAM_PORT+"/stream";
		return "rtsp://127.0.1.1:1235/stream";
	}
	
	/**
	 * openPlayer - opens the PlayerFrame on the playback machine
	 * @return opening JFrame
	 */
	public String openPlayer() {
		s.playMedia();
		return "opening JFrame";
	}
	
	/**
	 * startStream - starts the RTSPPlayer on the browse machine
	 * @param filePath the absolute file path of the media to play
	 * @return opening RTSP stream
	 */
	public String startStream(String filePath) {
		s.startStream(filePath);
		return "opening RTSP stream";
	}

	// returns mpc
	public EmbeddedMediaPlayerComponent getMPC() {	return mpc; 	}

	/**
	 * start - starts the stream remotely then plays the stream on the playing device
	 * @param filePath the absolute file path of the media to play
	 * @return playing ...
	 */
	public String start(String filePath) {
		System.out.println("Playing "+getTargetMRL());
		System.out.println( startStream(filePath) );
		mp.playMedia(getTargetMRL());
		//mp.setRepeat(true);
		//mp.playMedia(filePath);
		if(mp.isMute())
			mp.mute(true);
		return "playing "+filePath;
	}
	
	public String play() {
		mp.setRate(1);
		mp.play();	
		//setMarquee("Play");
		return "play";
	}

	public String pause() {
		mp.pause();
		return ("Pause");
	}

	public String mute() {
		mp.mute(mp.isMute() ? false : true);
		return ("Mute");
	}

	public String stop() {
		// TODO Auto-generated method stub
		return ("Stop");
	}

	public String setVolume(int value) {
		mp.setVolume(value);
		return ("Set Volume to " + value + "%");
	}
	
	// returns the current volume (0-200)
	public int getVolume() {	
		return mp.getVolume();	
	}

	public String setProgress(int value) {
		// TODO Auto-generated method stub
		return ("Set Progress to " + value/5 + "%");
	}
	
	// fast forwards. if at 1x, playback at 2x, if at 2x playback at 4x,
	// if at 4x playback at 8x, if at 8x playback at 1x
	public void ff() {
		float rate = mp.getRate();
		if(!mp.isMute())
			mute();
		if(rate <= 1.0)
			mp.setRate(2);
		else if (rate <= 2)
			mp.setRate(4);
		else if (rate <= 4)
			mp.setRate(8);
		else {
			if(mp.isMute())
				mp.mute(false);
			mp.setRate(1);
		}
	}
	
	// returns the metadata of the current media (not used yet, for marquee maybe)
	public MediaMeta getMetaData() {
		mp.parseMedia();
		if(mp.isMediaParsed())
			return mp.getMediaMeta();
		return null;
	}
	
	// returns the title and the mrl as title,mrl
	public String titleMRL() {
		String title = getMetaData().getTitle();
		String mrl = mp.mrl();
		return title+","+mrl;
	}

	public String getMediaLength() {
		return getTimeInHHMMSS(mp.getLength());
	}
	
	public String getCurrentTime() {
		return getTimeInHHMMSS(mp.getTime());
	}
	
	public String getTimeRemaining() {
		return "-"+getTimeInHHMMSS(mp.getLength()-mp.getTime());
	}
	
	/**
	 * getTimeInHHMMSS - helper method to convert millisecond time to string
	 * @param time  the time to convert (in milliseconds)
	 * @return the time remaining in HH:MM:SS format
	 */
	public String getTimeInHHMMSS(long time) {
		int seconds = (int) (time/1000);
		int minutes = seconds/60;
		int hours = minutes/60;
		return hours+":"+minutes+":"+seconds;
	}

}
