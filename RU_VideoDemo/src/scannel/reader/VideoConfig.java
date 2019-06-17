package scannel.reader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

public class VideoConfig {

	private static VideoConfig config;
	private static Properties config_properties;
	private static HashMap<String, String> linkMap;
	
	private final static String VIDEOCONFIG_PATH = "./videoSetting.config";
	private final static int MONITOR_SIZE = 5;
	
	private final static String KEY_INTRO = "Intro";
	private final static String KEY_EPC1 = "epc1";
	private final static String KEY_EPC2 = "epc2";
	private final static String KEY_EPC3 = "epc3";
	private final static String KEY_EPC4 = "epc4";
	private final static String KEY_EPC5 = "epc5";
	private final static String KEY_VIDEO1 = "video1";
	private final static String KEY_VIDEO2 = "video2";
	private final static String KEY_VIDEO3 = "video3";
	private final static String KEY_VIDEO4 = "video4";
	private final static String KEY_VIDEO5 = "video5";
	
	
	private VideoConfig() {
		System.out.println("VideoConfig constructor...");
		
		File f = new File(VIDEOCONFIG_PATH);
		if (!f.exists()) {
			// create config file
			try {
				f.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("Cannot create video configuration file!");
				return;
			}
		}
		
		linkMap = new HashMap();
		loadVideoConfig();
	}

	public static VideoConfig getInstance() {
		if (config == null) {
			config = new VideoConfig();
		}
		
		return config;
	}
	
	public void destroy() {
		if (config_properties != null) {
			config_properties.clear();
			config_properties = null;
		}
		
		if (linkMap != null) {
			linkMap.clear();
			linkMap = null;
		}
		config = null;
	}
	
	public void setIntroLink(String uri) {
		config_properties.setProperty(KEY_INTRO, uri);
	}
	
	public String getIntroLink() {
		return config_properties.getProperty(KEY_INTRO);
	}
	
	private void loadVideoConfig() {
		config_properties = new Properties();
		try {
			InputStream is = new FileInputStream(VIDEOCONFIG_PATH);
			config_properties.load(is);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		for (int i=0; i<MONITOR_SIZE; i++) {
			String epc = getEPC(i+1);
			String uri = getVideoLink(i+1);
			
			if (epc!=null && uri!=null) {
				linkMap.put(epc, uri);
			}
		}
	}
	
	public void saveConfig() {
		System.out.println("Save video configuration...");
		
		File config_file = new File(VIDEOCONFIG_PATH);
		if (!config_file.exists()) {
			try {
				config_file.createNewFile();
			} catch (IOException e) {
				System.out.println("Cannot create configuration file, so abort saving configuration!");
				e.printStackTrace();
			}
		}
		
		if (config_properties != null) {
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(config_file, false);
				config_properties.store(fos, null);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (fos != null) {
					try {
						fos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		} else {
			System.out.println("Video configuration properties is null!");
		}
		
		linkMap.clear();
		for (int i=0; i<MONITOR_SIZE; i++) {
			String epc = getEPC(i+1);
			String uri = getVideoLink(i+1);
			
			if (epc!=null && uri!=null) {
				linkMap.put(epc, uri);
			}
		}
	}
	
	public void setEPC(int index, String epc) {
		if (epc == null || epc.length() == 0) {
			return;
		}
		
		switch(index) {
		case 1:
			config_properties.setProperty(KEY_EPC1, epc);
			break;
		case 2:
			config_properties.setProperty(KEY_EPC2, epc);
			break;
		case 3:
			config_properties.setProperty(KEY_EPC3, epc);
			break;
		case 4:
			config_properties.setProperty(KEY_EPC4, epc);
			break;
		case 5:
			config_properties.setProperty(KEY_EPC5, epc);
			break;
		default:
			System.out.println("Unknow index for setEPC(): "+index);
			break;
		}
	}
	
	public String getEPC(int index) {
		String epc = null;
		switch (index) {
		case 1:
			epc = config_properties.getProperty(KEY_EPC1);
			break;
		case 2:
			epc = config_properties.getProperty(KEY_EPC2);
			break;
		case 3:
			epc = config_properties.getProperty(KEY_EPC3);
			break;
		case 4:
			epc = config_properties.getProperty(KEY_EPC4);
			break;
		case 5:
			epc = config_properties.getProperty(KEY_EPC5);
			break;
		default:
			System.out.println("Unknow index for getEPC(): "+index);
			break;
			
		}
		
		return epc;
	}
	
	public void setVideoLink(int index, String uri) {
		if (uri == null || uri.length() == 0) {
			return;
		}
		
		switch(index) {
		case 1:
			config_properties.setProperty(KEY_VIDEO1, uri);
			break;
		case 2:
			config_properties.setProperty(KEY_VIDEO2, uri);
			break;
		case 3:
			config_properties.setProperty(KEY_VIDEO3, uri);
			break;
		case 4:
			config_properties.setProperty(KEY_VIDEO4, uri);
			break;
		case 5:
			config_properties.setProperty(KEY_VIDEO5, uri);
			break;
		default:
			System.out.println("Unknow index for setVideoLink(): "+index);
			break;
		}
	}
	
	public String getVideoLink(int index) {
		String link = null;
		switch (index) {
		case 1:
			link = config_properties.getProperty(KEY_VIDEO1);
			break;
		case 2:
			link = config_properties.getProperty(KEY_VIDEO2);
			break;
		case 3:
			link = config_properties.getProperty(KEY_VIDEO3);
			break;
		case 4:
			link = config_properties.getProperty(KEY_VIDEO4);
			break;
		case 5:
			link = config_properties.getProperty(KEY_VIDEO5);
			break;
		default:
			System.out.println("Unknow index for getVideoLink(): "+index);
			break;
			
		}
		
		return link;
	}
	
	public void setEPCLink(String epc, String uri) {
		if (epc!=null && uri!=null) {
			linkMap.put(epc, uri);
		}
	}
	
	public String getEPCLink(String epc) {
		return linkMap.get(epc);
	}
}
