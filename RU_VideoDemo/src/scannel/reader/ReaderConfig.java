package scannel.reader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.thingmagic.Gen2.Session;
import com.thingmagic.Gen2.Target;
import com.thingmagic.Reader.Region;

public class ReaderConfig {

	private static ReaderConfig config;
	private static Properties config_properties;
//	private int power;
//	private int[] antList;
//	private String session;
//	private String target;
//	private String region;
//	private int hoptable;
	private final static String KEY_RFPOWER = "RFPower";
	private final static String KEY_ANTENNA = "Antenna";
	private final static String KEY_SESSION = "Session";
	private final static String KEY_TARGET = "Target";
	private final static String KEY_REGION = "Region";
	private final static String KEY_SAVE_TO_DISK = "LocalSave";
	private final static String KEY_SAVE_TO_DB = "DBSave";
	private final static String KEY_DB_TYPE = "DB_Type";
	private final static String KEY_DB_IP = "DB_IP";
	private final static String KEY_DB_NAME = "DB_Name";
	private final static String KEY_TABLE_NAME = "Table_Name";
	private final static String KEY_USER_ID = "User_Id";
	private final static String KEY_PASSWORD = "Password";
	private final static String KEY_EPC_FIELDNAME = "EPC_FieldName";
	private final static String KEY_READERID_FIELDNAME = "ReaderID_FieldName";
	private final static String KEY_TIME_FIELDNAME = "Time_FieldName";
	private final static String KEY_READERID = "ReaderID";
	private final static String KEY_DI_TRIGGER = "DI_Trigger";
	private final static String KEY_DO_TRIGGER = "DO_Trigger";
	private final static String KEY_DI_1 = "DI_1";
	private final static String KEY_DI_2 = "DI_2";
	private final static String KEY_DI_3 = "DI_3";
	private final static String KEY_DI_4 = "DI_4";
	private final static String KEY_DO_1 = "DO_1";
	private final static String KEY_DO_2 = "DO_2";
	private final static String KEY_DO_3 = "DO_3";
	private final static String KEY_DO_4 = "DO_4";
	
	
	
	
	private final static String CONFIG_PATH = "./reader_setting.config";
	
	public static Map<String, Region> regionMap;
	
	public enum DBType{
		MYSQL,
		MS_SQL_SERVER;
	}
	
	private ReaderConfig() {
		// TODO Auto-generated constructor stub
		System.out.println("ReaderConfig constructor...");
		
		File f = new File(CONFIG_PATH);
		if (!f.exists()) {
			// create config file
			try {
				f.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("Cannot create configuration file!");
				return;
			}
		}
		
		loadReaderConfig();
		initializeRegionMap();
	}
	
	public static ReaderConfig getInstance() {
		if (config == null) {
			config = new ReaderConfig();
		}
		
		return config;
	}

	public void destroy() {
		if (config_properties != null) {
			config_properties.clear();
			config_properties = null;
		}
		
		config = null;
	}
	
	public void setRegion(Region region) {
		switch (region) {
		case NA:
			config_properties.setProperty(KEY_REGION, "NA");
			break;
		case IN:
			config_properties.setProperty(KEY_REGION, "IN");
			break;
		case PRC:
			config_properties.setProperty(KEY_REGION, "PRC");
			break;
		case EU3:
			config_properties.setProperty(KEY_REGION, "EU3");
			break;
		case KR2:
			config_properties.setProperty(KEY_REGION, "KR2");
			break;
		case AU:
			config_properties.setProperty(KEY_REGION, "AU");
			break;
		case NZ:
			config_properties.setProperty(KEY_REGION, "NZ");
			break;
		case MY:
			config_properties.setProperty(KEY_REGION, "MY");
			break;
		case ID:
			config_properties.setProperty(KEY_REGION, "ID");
			break;
		case PH:
			config_properties.setProperty(KEY_REGION, "PH");
			break;
		case TW:
			config_properties.setProperty(KEY_REGION, "TW");
			break;
		case MO:
			config_properties.setProperty(KEY_REGION, "MO");
			break;
		case RU:
			config_properties.setProperty(KEY_REGION, "RU");
			break;
		case SG:
			config_properties.setProperty(KEY_REGION, "SG");
			break;
		default:
			break;
		}
	}
	
	public Region getRegion() {
		String region = config_properties.getProperty(KEY_REGION);
		
		if (region == null) {
			region = "NA";
			config_properties.setProperty(KEY_REGION, region);
		}
		
		return regionMap.get(region);
	}
	
	public void setRFPower(int value) {
		config_properties.setProperty(KEY_RFPOWER, Integer.toString(value));
	}
	
	public int getRFPower() {
		String power = config_properties.getProperty(KEY_RFPOWER);
		
		// if no power value in configuration file, return default value
		if (power == null) {
			power = "24";
			config_properties.setProperty(KEY_RFPOWER, power);
		}
		
		return Integer.parseInt(power);
	}
	
	public void setAntennaList(int[] list) {
		String ant_list = "";
		for (int i=0; i<list.length; i++) {
			if (i == 0) {
				ant_list = Integer.toString(list[i]);
			} else {
				ant_list += ","+Integer.toString(list[i]);
			}
		}
		
		config_properties.setProperty(KEY_ANTENNA, ant_list);
	}
	
	public int[] getAntennaList() {
		String list = config_properties.getProperty(KEY_ANTENNA);
		
		// if no antenna setting in configuration file, return default value
		if (list == null) {
			list = "1";
			config_properties.setProperty(KEY_ANTENNA, list);
		}
		return parseAntennaList(list);
	}
	
	public void setSession(Session s) {
		switch (s) {
		case S0:
			config_properties.setProperty(KEY_SESSION, "S0");
			break;
		case S1:
			config_properties.setProperty(KEY_SESSION, "S1");
			break;
		case S2:
			config_properties.setProperty(KEY_SESSION, "S2");
			break;
		case S3:
			config_properties.setProperty(KEY_SESSION, "S3");
			break;
		}
	}
	
	public Session getSession() {
		String mySession = config_properties.getProperty(KEY_SESSION);
		if (mySession == null) {
			this.setSession(Session.S0);
			return Session.S0;
		} else {
			switch (mySession) {
			case ("S0"):
				return Session.S0;
			case ("S1"):
				return Session.S1;
			case ("S2"):
				return Session.S2;
			case ("S3"):
				return Session.S3;
			default:
				config_properties.setProperty(KEY_SESSION, "S0");
				return Session.S0;
			}
		}
	}
	
	public void setTargetFlag(Target t) {
		switch (t) {
		case A:
			config_properties.setProperty(KEY_TARGET, "A");
			break;
		case B:
			config_properties.setProperty(KEY_TARGET, "B");
			break;
		case AB:
			config_properties.setProperty(KEY_TARGET, "AB");
			break;
		case BA:
			config_properties.setProperty(KEY_TARGET, "BA");
			break;
		}
	}
	
	public Target getTargetFlag() {
		String myTargetFlag = config_properties.getProperty(KEY_TARGET);
		
		if (myTargetFlag == null) {
			this.setTargetFlag(Target.A);
			return Target.A;
		} else {
			switch (myTargetFlag) {
			case ("A"):
				return Target.A;
			case ("B"):
				return Target.B;
			case ("AB"):
				return Target.AB;
			case ("BA"):
				return Target.BA;
			default:
				config_properties.setProperty(KEY_TARGET, "A");
				return Target.A;
			}
		}
	}
	
	private int[] parseAntennaList(String str) {
		MyLogger.printLog("parse antenna list string: "+str);
		
		String[] strArray = str.split(",");
		int[] antenna = new int[strArray.length];
		for (int i=0; i<antenna.length; i++) {
			
			antenna[i] = Integer.parseInt(strArray[i]);
		}
		
		return antenna;
	}
	
	private void loadReaderConfig() {
		config_properties = new Properties();
		try {
			InputStream is = new FileInputStream(CONFIG_PATH);
			config_properties.load(is);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void saveConfig() {
		System.out.println("Save reader configuration...");
		
		File config_file = new File(CONFIG_PATH);
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if (fos != null) {
					try {
						fos.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		} else {
			System.out.println("Configuration properties is null!");
		}
	}
	
	private void initializeRegionMap() {
		if (regionMap != null) {
			regionMap.clear();
		} else {
			regionMap = new HashMap<String, Region>();
		}
		
		regionMap.put("NA", Region.NA);
		regionMap.put("IN", Region.IN);
		regionMap.put("PRC", Region.PRC);
		regionMap.put("EU3", Region.EU3);
		regionMap.put("KR2", Region.KR2);
		regionMap.put("AU", Region.AU);
		regionMap.put("NZ", Region.NZ);
		regionMap.put("MY", Region.MY);
		regionMap.put("ID", Region.ID);
		regionMap.put("PH", Region.PH);
		regionMap.put("TW", Region.TW);
		regionMap.put("MO", Region.MO);
		regionMap.put("RU", Region.RU);
		regionMap.put("SG", Region.SG);
		
	}
	
	public void setDataLocalSave(boolean local_save) {
		if (local_save) {
			config_properties.setProperty(KEY_SAVE_TO_DISK, "Y");
		} else {
			config_properties.setProperty(KEY_SAVE_TO_DISK, "N");
		}
	}
	
	public boolean isDataLocalSaveEnabled() {
		String local_save = config_properties.getProperty(KEY_SAVE_TO_DISK);
		
		if (local_save == null) {
			this.setDataLocalSave(true);
			return true;
		} else {
			switch (local_save) {
			case ("Y"):
				return true;
			case ("N"):
				return false;
			default:
				this.setDataLocalSave(true);
				return true;
			}
		}
	}
	
	public void setDataDBSave(boolean db_save) {
		if (db_save) {
			config_properties.setProperty(KEY_SAVE_TO_DB, "Y");
		} else {
			config_properties.setProperty(KEY_SAVE_TO_DB, "N");
		}
	}
	
	public boolean isDataDBSaveEnabled() {
		String db_save = config_properties.getProperty(KEY_SAVE_TO_DB);
		
		if (db_save == null) {
			this.setDataDBSave(true);
			return true;
		} else {
			switch (db_save) {
			case ("Y"):
				return true;
			case ("N"):
				return false;
			default:
				this.setDataDBSave(true);
				return true;
			}
		}
	}
	
	public void setDBType(DBType db) {
		config_properties.setProperty(KEY_DB_TYPE, db.toString());
	}
	
	public DBType getDBType() {
		String type = config_properties.getProperty(KEY_DB_TYPE);
		
		if (type == null) {
			return null;
		} else {
			if (type.equals(DBType.MYSQL.name())) {
				return DBType.MYSQL;
			} else if (type.equals(DBType.MS_SQL_SERVER.name())) {
				return DBType.MS_SQL_SERVER;
			} else {
				return null;
			}
		}
	}
	
	public void setDBIP(String ip) {
		config_properties.setProperty(KEY_DB_IP, ip);
	}
	
	public String getDBIP() {
		return config_properties.getProperty(KEY_DB_IP);
	}
	
	public void setDBName(String name) {
		config_properties.setProperty(KEY_DB_NAME, name);
	}
	
	public String getDBName() {
		return config_properties.getProperty(KEY_DB_NAME);
	}
	
	public void setUserId(String id) {
		config_properties.setProperty(KEY_USER_ID, id);
	}
	
	public String getUserId() {
		return config_properties.getProperty(KEY_USER_ID);
	}
	
	public void setPassword(String password) {
		config_properties.setProperty(KEY_PASSWORD, password);
	}
	
	public String getPassword() {
		return config_properties.getProperty(KEY_PASSWORD);
	}
	
	public void setTableName(String name) {
		config_properties.setProperty(KEY_TABLE_NAME, name);
	}
	
	public String getTableName() {
		return config_properties.getProperty(KEY_TABLE_NAME);
	}
	
	public void setEPCFieldName(String name) {
		config_properties.setProperty(KEY_EPC_FIELDNAME, name);
	}
	
	public String getEPCFieldName() {
		return config_properties.getProperty(KEY_EPC_FIELDNAME);
	}
	
	public void setReaderIdFieldName(String name) {
		config_properties.setProperty(KEY_READERID_FIELDNAME, name);
	}
	
	public String getReaderIdFieldName() {
		return config_properties.getProperty(KEY_READERID_FIELDNAME);
	}
	
	public void setTimeFieldName(String name) {
		config_properties.setProperty(KEY_TIME_FIELDNAME, name);
	}
	
	public String getTimeFieldName() {
		return config_properties.getProperty(KEY_TIME_FIELDNAME);
	}
	
	public void setReaderId(String id) {
		config_properties.setProperty(KEY_READERID, id);
	}
	
	public String getReaderId() {
		return config_properties.getProperty(KEY_READERID);
	}
	
	public void setDITrigger(boolean trigger) {
		if (trigger) {
			config_properties.setProperty(KEY_DI_TRIGGER, "true");
		} else {
			config_properties.setProperty(KEY_DI_TRIGGER, "false");
		}
	}
	
	public boolean getDITrigger() {
		String trigger = config_properties.getProperty(KEY_DI_TRIGGER);
		
		if (trigger == null) {
			this.setDITrigger(false);
			return false;
		} else {
			if (trigger.equals("true")) {
				return true;
			} else if (trigger.equals("false")) {
				return false;
			} else {
				this.setDITrigger(false);
				return false;
			}
		}
	}
	
	public void setDI1(boolean trigger) {
		if (trigger) {
			config_properties.setProperty(KEY_DI_1, "true");
		} else {
			config_properties.setProperty(KEY_DI_1, "false");
		}
	}
	
	public boolean getDI1() {
		String trigger = config_properties.getProperty(KEY_DI_1);
		
		if (trigger == null) {
			this.setDI1(false);
			return false;
		} else {
			if (trigger.equals("true")) {
				return true;
			} else if (trigger.equals("false")) {
				return false;
			} else {
				this.setDI1(false);
				return false;
			}
		}
	}
	
	public void setDI2(boolean trigger) {
		if (trigger) {
			config_properties.setProperty(KEY_DI_2, "true");
		} else {
			config_properties.setProperty(KEY_DI_2, "false");
		}
	}
	
	public boolean getDI2() {
		String trigger = config_properties.getProperty(KEY_DI_2);
		
		if (trigger == null) {
			this.setDI2(false);
			return false;
		} else {
			if (trigger.equals("true")) {
				return true;
			} else if (trigger.equals("false")) {
				return false;
			} else {
				this.setDI2(false);
				return false;
			}
		}
	}
	
	public void setDI3(boolean trigger) {
		if (trigger) {
			config_properties.setProperty(KEY_DI_3, "true");
		} else {
			config_properties.setProperty(KEY_DI_3, "false");
		}
	}
	
	public boolean getDI3() {
		String trigger = config_properties.getProperty(KEY_DI_3);
		
		if (trigger == null) {
			this.setDI3(false);
			return false;
		} else {
			if (trigger.equals("true")) {
				return true;
			} else if (trigger.equals("false")) {
				return false;
			} else {
				this.setDI3(false);
				return false;
			}
		}
	}
	
	public void setDI4(boolean trigger) {
		if (trigger) {
			config_properties.setProperty(KEY_DI_4, "true");
		} else {
			config_properties.setProperty(KEY_DI_4, "false");
		}
	}
	
	public boolean getDI4() {
		String trigger = config_properties.getProperty(KEY_DI_4);
		
		if (trigger == null) {
			this.setDI4(false);
			return false;
		} else {
			if (trigger.equals("true")) {
				return true;
			} else if (trigger.equals("false")) {
				return false;
			} else {
				this.setDI4(false);
				return false;
			}
		}
	}
	
	public void setDOTrigger(boolean trigger) {
		if (trigger) {
			config_properties.setProperty(KEY_DO_TRIGGER, "true");
		} else {
			config_properties.setProperty(KEY_DO_TRIGGER, "false");
		}
	}
	
	public boolean getDOTrigger() {
		String trigger = config_properties.getProperty(KEY_DO_TRIGGER);
		
		if (trigger == null) {
			this.setDOTrigger(false);
			return false;
		} else {
			if (trigger.equals("true")) {
				return true;
			} else if (trigger.equals("false")) {
				return false;
			} else {
				this.setDOTrigger(false);
				return false;
			}
		}
	}
	
	public void setDO1(boolean trigger) {
		if (trigger) {
			config_properties.setProperty(KEY_DO_1, "true");
		} else {
			config_properties.setProperty(KEY_DO_1, "false");
		}
	}
	
	public boolean getDO1() {
		String trigger = config_properties.getProperty(KEY_DO_1);
		
		if (trigger == null) {
			this.setDO1(false);
			return false;
		} else {
			if (trigger.equals("true")) {
				return true;
			} else if (trigger.equals("false")) {
				return false;
			} else {
				this.setDO1(false);
				return false;
			}
		}
	}
	
	public void setDO2(boolean trigger) {
		if (trigger) {
			config_properties.setProperty(KEY_DO_2, "true");
		} else {
			config_properties.setProperty(KEY_DO_2, "false");
		}
	}
	
	public boolean getDO2() {
		String trigger = config_properties.getProperty(KEY_DO_2);
		
		if (trigger == null) {
			this.setDO2(false);
			return false;
		} else {
			if (trigger.equals("true")) {
				return true;
			} else if (trigger.equals("false")) {
				return false;
			} else {
				this.setDO2(false);
				return false;
			}
		}
	}
	
	public void setDO3(boolean trigger) {
		if (trigger) {
			config_properties.setProperty(KEY_DO_3, "true");
		} else {
			config_properties.setProperty(KEY_DO_3, "false");
		}
	}
	
	public boolean getDO3() {
		String trigger = config_properties.getProperty(KEY_DO_3);
		
		if (trigger == null) {
			this.setDO3(false);
			return false;
		} else {
			if (trigger.equals("true")) {
				return true;
			} else if (trigger.equals("false")) {
				return false;
			} else {
				this.setDO3(false);
				return false;
			}
		}
	}
	
	public void setDO4(boolean trigger) {
		if (trigger) {
			config_properties.setProperty(KEY_DO_4, "true");
		} else {
			config_properties.setProperty(KEY_DO_4, "false");
		}
	}
	
	public boolean getDO4() {
		String trigger = config_properties.getProperty(KEY_DO_4);
		
		if (trigger == null) {
			this.setDO4(false);
			return false;
		} else {
			if (trigger.equals("true")) {
				return true;
			} else if (trigger.equals("false")) {
				return false;
			} else {
				this.setDO4(false);
				return false;
			}
		}
	}
}
