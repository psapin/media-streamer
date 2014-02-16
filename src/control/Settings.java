package control;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;

public class Settings {

	public static String NAME = "Patrick's ";
	public static final String TYPE = System.getProperty("os.name") + " " + System.getProperty("os.version");
	public static final int SOCKET_PORT = 1234;
	public static final int STREAM_PORT = 1235;
	//public static final String ROOT = System.getProperty("user.dir");
	public static final String ROOT = "/Users/patrick/Music";
	
	private static final String[] FILETYPES_LIST = {".asx",".dts",".gxf",".m2v",".m3u",".m4v",".mpeg1",".mpeg2",
		".mts",".mxf",".ogm",".pls",".bup",".a52",".aac",".b4s",".cue",".divx",".dv",".flv",".m1v",".m2ts",".mkv",
		".mov",".mpeg4",".oma",".spx",".ts",".vlc",".vob",".xspf",".dat",".bin",".ifo",".part",".3g2",".avi",".mpeg",
		".mpg",".flac",".m4a",".mp1",".ogg",".wav",".xm",".3gp",".srt",".wmv",".ac3",".asf",".mod",".mp2",".mp3",
		".mp4",".wma",".mka",".m4p"};
	public static final HashSet<String> ACCEPTED_FILETYPES = new HashSet<String>(Arrays.asList(FILETYPES_LIST));
	
	public static boolean readSettings(String filePath) {
		
		File properties = new File(filePath);
		/* read in file
		 * read name, socket port, stream port, any other settings
		 * make new properties class? or set static Settings fields
		 */
		return true;
	}
}
