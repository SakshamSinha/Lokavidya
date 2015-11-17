package in.iitb.fileutils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import org.springframework.web.multipart.MultipartFile;

public class Utils {
	
	private final static String WEBSITE_ADDRESS = "http://ruralict.cse.iitb.ac.in/";
	private final static String ROOT_DIR= "/home/sanket/";
	
	
	//private final static String ROOT_DIR= "/home/ruralivrs/Ruralict/apache-tomcat-7.0.42/webapps/";
	private final static String VOICE_DIR = "Downloads/lokavidya/voices";	
	private final static String VIDEO_DIR = "Downloads/lokavidya/videos";
	private final static String TEMP_VIDEO_DIR = "Downloads/lokavidya/videos/temp";
	private final static String IMAGE_DIR = "Downloads/lokavidya/images";	
	
	public static String getImageDir()
	{
		return ROOT_DIR+IMAGE_DIR;
	}
	public static String getVideoDir()
	{
		return ROOT_DIR+VIDEO_DIR;
	}
	public static String getVideoDirURL()
	{
		return WEBSITE_ADDRESS+VIDEO_DIR +"/";
	}
	public static String getAudioDir()
	{
		return ROOT_DIR+VOICE_DIR;
	}
	public static String getTempVideoDir()
	{
		return ROOT_DIR+TEMP_VIDEO_DIR;
	}
	
	public static File saveFile(String fileName, String directory, MultipartFile file) {
		if (!file.isEmpty()) {
			try {
				byte[] bytes = file.getBytes();
 
				// Creating the directory to store file
				File dir = new File(directory);
				if (!dir.exists())
					dir.mkdirs();

				// Create the file on server
				File temp = new File(dir.getAbsolutePath() + File.separator + fileName);
				System.out.println(temp);
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(temp));
				stream.write(bytes);
				stream.close();
				
				return temp;
			} catch(Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		else {
			System.out.println("File is Empty!");
			return null;
		}
	}
}
