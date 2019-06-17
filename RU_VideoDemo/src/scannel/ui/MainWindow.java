package scannel.ui;

import com.thingmagic.ReaderException;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.paint.Color;
import scannel.reader.ReaderUtility;

public class MainWindow extends TabPane implements EventHandler<Event> {

	private Tab videoTab;
	private VideoFrame videoFrame;
	private Tab settingTab;
	private VideoLinkSettingFrame settingFrame;
	
	private final static boolean USE_SERIAL_PORT = true;
	private final static String URI_SERIAL = "tmr:///dev/ttyS0";
	private final static String URI_IP = "192.168.100.160";
	private final static String URI_PORT = "4001";
	
	
	public MainWindow() {
		this.initComponents();
		
		new Thread() {
			public void run() {
				connectReader();
			}
		}.start();
	}

	public MainWindow(Tab... arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	private void initComponents() {
		videoTab = new Tab("Video");
		videoTab.setClosable(false);
		videoFrame = new VideoFrame();
		videoTab.setContent(videoFrame);
		videoTab.setOnSelectionChanged(this);

//		this.setOnKeyPressed(new EventHandler() {
//
//			@Override
//			public void handle(Event event) {
//				// TODO Auto-generated method stub
//				System.out.println("key pressed");
//				System.out.println("source="+event.getSource()+", target="+event.getTarget());
//			}
//			
//		});
		
		settingTab = new Tab("Setting");
		settingTab.setClosable(false);
		settingFrame = new VideoLinkSettingFrame();
		settingTab.setContent(settingFrame);
		settingTab.setOnSelectionChanged(this);
		
		this.getTabs().addAll(videoTab, settingTab);
		
	}
	
	@Override
	public void handle(Event event) {
		System.out.println("MainWindow");
		System.out.println("source="+event.getSource()+", target="+event.getTarget());
	}

	private void connectReader() {
		System.out.println("Connecting Reader...");
		try {
			if (USE_SERIAL_PORT) {
				// Since the reader is connect to raspberry pi by UART, 
				// use "tmr:///dev/ttyS0" as default connection path
				ReaderUtility.getInstance().connectReader(URI_SERIAL);
			} else {
				// Connect reader through ethernet for testing, reader ip and port may change 
				ReaderUtility.getInstance().connectReader(URI_IP, URI_PORT);
			}
		} catch (ReaderException e) {
			e.printStackTrace();
			System.out.println("Connection Failed");
		}
	}
	
	private void disconnectReader() {
		ReaderUtility.getInstance().disconnectReader();
	}
}
