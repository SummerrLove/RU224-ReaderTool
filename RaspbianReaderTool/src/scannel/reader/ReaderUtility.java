package scannel.reader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import com.thingmagic.Gen2;
import com.thingmagic.ReadListener;
import com.thingmagic.Reader;
import com.thingmagic.ReaderException;
import com.thingmagic.SerialTransportTCP;
import com.thingmagic.SimpleReadPlan;
import com.thingmagic.TMConstants;
import com.thingmagic.TagFilter;
import com.thingmagic.TagProtocol;
import com.thingmagic.TagReadData;

import javafx.application.Platform;
import scannel.gpio.DigitalIOController;

import com.thingmagic.Reader.Region;

public class ReaderUtility implements ReadListener {

	private static ReaderUtility ru;
	private static Reader myReader;
	private static TagList tagReadList;
	private static Timer readTimer;
	private static TimerTask task;
//	private String reader_id;
	
	private static boolean isConnected;
	private static boolean isReading;
	
	private static DataUpdateListener updateListener;
	private static String filter_str;
	
	private final static boolean PRINT_TRANSPORT_LOG = false;
//	private final static boolean USE_SERIAL_PORT = false;
//	private final static String URI_SERIAL = "tmr:///dev/ttyS0";
//	private final static String URI_IP = "192.168.100.160";
//	private final static String URI_PORT = "4001";
	
	
	private final static long INTERVAL = 500;
	private final static int TIMEOUT = 60000;
	
	public final static boolean USE_TIMER = true;
	public final static int READ_TIME = 300;
	public final static String FILE_DIRECTORY = "./saveData/";
	
	
	private ReaderUtility() {
		// TODO Auto-generated constructor stub
		
	}

	public static ReaderUtility getInstance() {
		if (ru == null) {
			ru = new ReaderUtility();
		}
		return ru;
	}
	
	public void destroy() {
		if (readTimer != null) {
			readTimer.cancel();
		}
		
		if (myReader != null) {
			myReader.destroy();
			myReader = null;
		}
		
		if (tagReadList != null) {
			tagReadList.reset();
			tagReadList = null;
		}
		
		if (ru != null) {
			ru = null;
		}
	}
	
	private void initiateTimer() {
		readTimer = new Timer();
		task = new TimerTask() {

			@Override
			public void run() {
				Platform.runLater(new Runnable() {

					@Override
					public void run() {
						readData();
						if (updateListener != null) {
							updateListener.dataUpdate();
						}
					}
					
				});
			}
			
		};
	}
	
	private void createTCPReader(String ip, String port) throws ReaderException {
		Reader.setSerialTransport("tcp", new SerialTransportTCP.Factory());
		myReader = Reader.create("tcp://"+ip+":"+port);
		System.out.println("TCP reader created!");
	}
	
//	public void createTCPReader(String readerURI) throws ReaderException{
//		Reader.setSerialTransport("tcp", new SerialTransportTCP.Factory());
//		myReader = Reader.create(readerURI);
//	}
	
	private void createSerialReader(String port) throws ReaderException{
		myReader = Reader.create(port);
		System.out.println("Serial Reader created!");
	}
	
	public void connectReader(String ip, String port) throws ReaderException{
		if (myReader != null){
			
			if (myReader != null) {
				myReader.destroy();
				myReader = null;
			}
		} 
		
		this.createTCPReader(ip, port);
		myReader.connect();
		tagReadList = new TagList();
		isConnected = true;
		System.out.println("connect reader!");
		
	}
	
	public void connectReader(String port) throws ReaderException {
		if (myReader != null){
			
			if (myReader != null) {
				myReader.destroy();
				myReader = null;
			}
		} 
		
		this.createSerialReader(port);
		myReader.connect();
		tagReadList = new TagList();
		isConnected = true;
		System.out.println("connect reader!");
	}
	
	public void disconnectReader() {
		if (myReader != null) {
			 if (!USE_TIMER) {
				 myReader.stopReading();
			 } else {
				 if (readTimer != null) {
					 readTimer.cancel();
					 readTimer = null;
				 }
			 }
			myReader.destroy();
			myReader = null;
			isConnected = false;
			isReading = false;
		} else {
			System.out.println("[ReaderUtility] failed to disconnect from reader cause reader is null..");
		}
	}
	
	public void setSession(Gen2.Session session) throws ReaderException {
		if (myReader == null) {
			System.out.println("Reader is not initialized...");
			return;
		}
		
//		System.out.println("set Session:"+session.toString());
		myReader.paramSet(TMConstants.TMR_PARAM_GEN2_SESSION, session);
	}
	
	public void setTargetFlag(Gen2.Target target) throws ReaderException {
		if (myReader == null) {
			System.out.println("Reader is not initialized...");
			return;
		}
		
//		System.out.println("set target:"+target.toString());
		myReader.paramSet(TMConstants.TMR_PARAM_GEN2_TARGET, target);
	}
	
	public void setRFPower(int power) throws ReaderException {
		if (myReader == null) {
			System.out.println("Reader is not initialized...");
			return;
		}
		
//		System.out.println("set RF power:"+power);
		myReader.paramSet(TMConstants.TMR_PARAM_RADIO_READPOWER, new Integer(power*100));
	}
	
//	private void getRFPower() throws ReaderException {
//		if (myReader == null) {
//			System.out.println("Reader is not initialized...");
//			return;
//		}
//		
//		Integer power = (Integer) myReader.paramGet(TMConstants.TMR_PARAM_RADIO_READPOWER);
//		System.out.println(power);
//	}
	
	public void setRegion(Region region) throws ReaderException {
		if (myReader == null) {
			System.out.println("Reader is not initialized...");
			return;
		}
		
//		System.out.println("set Region: "+region.toString());
		myReader.paramSet(TMConstants.TMR_PARAM_REGION_ID, region);
	}
	
//	private void getRegion() throws ReaderException {
//		if (myReader == null) {
//			System.out.println("Reader is not initialized...");
//			return;
//		}
//		
//		Region region = (Region) myReader.paramGet(TMConstants.TMR_PARAM_REGION_ID);
//		System.out.println(region);
//	}
	
	public Region[] getSupportedRegion() throws ReaderException {
		if (myReader == null) {
			System.out.println("Reader is not initialized...");
			return null;
		}
		
		Region[] regionArray = (Region[]) myReader.paramGet(TMConstants.TMR_PARAM_REGION_SUPPORTEDREGIONS);
		return regionArray;
	}
	
//	public void setReaderId(String id) {
//		reader_id = id;
//	}
//	
//	public String getReaderId() {
//		return reader_id;
//	}
	
	public void startReading(int[] antenna) throws ReaderException {
		if (myReader == null) {
			System.out.println("Reader is not initialized...");
			return;
		}
		
		//============================================================
//		myReader.paramSet(TMConstants.TMR_PARAM_GEN2_TAGENCODING, Gen2.TagEncoding.FM0);
//		myReader.paramSet(TMConstants.TMR_PARAM_GEN2_BLF, Gen2.LinkFrequency.LINK640KHZ);
//		myReader.paramSet(TMConstants.TMR_PARAM_GEN2_TARI, Gen2.Tari.TARI_6_25US);
//		myReader.paramSet(TMConstants.TMR_PARAM_GEN2_Q, new Gen2.StaticQ(0));
		System.out.println("======= Reader setting =======");
		System.out.println(myReader.paramGet(TMConstants.TMR_PARAM_GEN2_TAGENCODING));
		System.out.println(myReader.paramGet(TMConstants.TMR_PARAM_GEN2_BLF));
		System.out.println(myReader.paramGet(TMConstants.TMR_PARAM_GEN2_TARI));
		System.out.println(myReader.paramGet(TMConstants.TMR_PARAM_GEN2_Q));
//		System.out.println(myReader.paramGet(TMConstants.TMR_PARAM_VERSION_SOFTWARE));
		System.out.println("==============================");
		//============================================================
		
		if (PRINT_TRANSPORT_LOG) {
			myReader.addTransportListener(myReader.simpleTransportListener);
		}
		
		System.out.println("start reading");
		SimpleReadPlan plan = new SimpleReadPlan(antenna, TagProtocol.GEN2, null, null, 1000);
        myReader.paramSet(TMConstants.TMR_PARAM_READ_PLAN, plan);
        
        // set up the timeout value
        myReader.paramSet(TMConstants.TMR_PARAM_TRANSPORTTIMEOUT, TIMEOUT);
		
        if (USE_TIMER) {
        	initiateTimer();
        	readTimer.schedule(task, 0, INTERVAL);
        } else {
        	myReader.addReadListener(this);
        	myReader.startReading();
        }
        
		isReading = true;
//		System.out.println("start reading tags...");
		
//		UserConfigOp op = new UserConfigOp(SetUserProfileOption.SAVE);
	}
	
	public void stopReading() {
		if (USE_TIMER) {
			readTimer.cancel();
			readTimer = null;
		} else {
			myReader.stopReading();
		}
		isReading = false;
		
		DigitalIOController.getInstance().turnOffOutput();
	}
	
	public void resetData() {
		if (tagReadList != null) {
			tagReadList.reset();
		}
	}
	
	public boolean isConnected() {
		return isConnected;
	}
	
	public boolean isReading() {
		return isReading;
	}
	
	public int totalTagRead() {
		return tagReadList.size();
	}
	
	public TagList getTagData() {
//		if (tagReadList != null) {
//			TagUnit[] tags = new TagUnit[tagReadList.size()];
//			for (int i=0; i<tagReadList.size(); i++) {
//				TagUnit tmp = tagReadList.get(i);
//				tags[i] = new TagUnit(tmp.getEPC(), tmp.getReadCount());
//			}
//			
//			return tags;
//		} else {
//			return null;
//		}
		return tagReadList;
	}
	
	public void setDataUpdateListener(DataUpdateListener listener) {
		ReaderUtility.updateListener = listener;
	}
	
	public void removeDataUpdateListener() {
		ReaderUtility.updateListener = null;
	}
	
	private void parseTagData(TagReadData[] trd){
		for (int i=0; i<trd.length; i++){
			parseTagData(trd[i]);
		}
	}

	private void parseTagData(TagReadData trd) {
		// if the epc of the tag doesn't match the filter format, then ignore the tag data
		if ((filter_str != null) && !trd.epcString().startsWith(filter_str)) {
			return;
		}
		tagReadList.addTag(trd);
	}
	
	private void readData() {
		TagReadData[] trd;
		try {
			trd = myReader.read(READ_TIME);
			
			if (trd.length > 0) {
				DigitalIOController.getInstance().turnOnOutput();
			} else {
				DigitalIOController.getInstance().turnOffOutput();
			}
			
			System.out.println("tag number = "+trd.length);
			this.parseTagData(trd);
			
		} catch (ReaderException e1) {
			System.out.println("Stop reading due to the following exception:");
			e1.printStackTrace();
			readTimer.cancel();
		}
	}

	@Override
	public void tagRead(Reader r, TagReadData t) {
		this.parseTagData(t);
	}
	
//	public void setFilter(String str) {
//		filter_str = str;
//	}
//	
//	private void removeFilter() {
//		filter_str = null;
//	}
	
	public void printSupportRegeion() throws ReaderException {
		if (myReader != null) {
			Region[] regions = (Region[]) myReader.paramGet(TMConstants.TMR_PARAM_REGION_SUPPORTEDREGIONS);
			for (int i=0; i<regions.length; i++) {
				System.out.println("supported regeion ["+i+"]: "+regions[i]);
			}
		}
	}
	
	public Region[] getSupportRegion() throws ReaderException {
		if (myReader != null) {
			return (Region[]) myReader.paramGet(TMConstants.TMR_PARAM_REGION_SUPPORTEDREGIONS);
		} else {
			return null;
		}
	}
	
	public int[] getHopTable() throws ReaderException {
		MyLogger.printLog("Get Hop Table");
		if (myReader != null) {
			int[] list = (int[]) myReader.paramGet(TMConstants.TMR_PARAM_REGION_HOPTABLE);
			for (int i=0; i<list.length; i++) {
				System.out.println("Frequency Hop List [" + i + "]: " + list[i]);
			}
			
			return list;
		}
		
		return null;
	}
	
	public void setHopTable(int[] hoptable) throws ReaderException {
		MyLogger.printLog("Set Hop Table");
		if (myReader != null) {
			myReader.paramSet(TMConstants.TMR_PARAM_REGION_HOPTABLE, hoptable);
		}
	}
	
//	public void printReaderSetting() throws ReaderException {
//		this.printSupportRegeion();
//		this.getRFPower();
//		this.getRegion();
//	}
	
	/**
	 * Save the read tag data into file. The file name is based on the date that the functions is called.
	 * If a file with the same name exists, append the data to the file; otherwise create a new file and save the data.
	 * @throws IOException 
	 * 
	 */
	public void saveTagDataToFile() throws IOException {
		File dir = new File(FILE_DIRECTORY);
		if (!dir.isDirectory()) {
			MyLogger.printLog("create directory: "+FILE_DIRECTORY);
			dir.mkdir();
		}
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		String file_path = FILE_DIRECTORY+formatter.format(new Date())+".txt";
		System.out.println(file_path);
		
		File save_file = new File(file_path);
		if (!save_file.exists()) {
			save_file.createNewFile();
		}
		
		FileOutputStream fos = new FileOutputStream(save_file, true);
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
		
		SimpleDateFormat time_formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		String readerId = ReaderConfig.getInstance().getReaderId();
		if (tagReadList != null) {
			for (int i=0; i<tagReadList.size(); i++) {
				TagUnit tu = tagReadList.get(i);
				String output_str = tu.getEPC()+","+readerId+","+time_formatter.format(tu.getTime());
				bw.write(output_str);
				bw.newLine();
			}
		}
		
		bw.close();
		fos.close();
	}
	
//	public void createTestTagData() {
//		tagReadList = new TagList();
//		
//		for (int i=0; i<5; i++) {
//			TagUnit tu = new TagUnit("EPC-"+(i+1));
//			tu.setTime(new Date());
//			tagReadList.addTag(tu);
//		}
//		
//		
//	}
	
	public TagReadData readTag() throws ReaderException {
		if (myReader == null) {
			MyLogger.printLog("Reader is not initialized...");
			return null;
		}
		
		myReader.paramSet(TMConstants.TMR_PARAM_REGION_ID, ReaderConfig.getInstance().getRegion());
		SimpleReadPlan plan = new SimpleReadPlan(ReaderConfig.getInstance().getAntennaList(), TagProtocol.GEN2, null, null, 1000);
        myReader.paramSet(TMConstants.TMR_PARAM_READ_PLAN, plan);
        
        TagReadData[] data = myReader.read(300);
        MyLogger.printLog("Number of tags read: "+data.length);
        
        if (data.length > 0) {
        	return data[0];
        } else {
        	return null;
        }
		
	}
	
	public void writeTag(Gen2.TagData data, TagFilter target) throws ReaderException {
		if (myReader == null) {
			MyLogger.printLog("Reader is not initialized...");
			return;
		}
		
		myReader.paramSet(TMConstants.TMR_PARAM_REGION_ID, ReaderConfig.getInstance().getRegion());
		myReader.paramSet(TMConstants.TMR_PARAM_TAGOP_ANTENNA, ReaderConfig.getInstance().getAntennaList()[0]);
		myReader.executeTagOp(new Gen2.WriteTag(data), target);
	}
}
