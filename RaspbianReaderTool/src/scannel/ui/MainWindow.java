package scannel.ui;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import scannel.reader.ReaderUtility;

public class MainWindow extends TabPane implements EventHandler<Event> {

	private Tab readTab;
	private ReadFrame read_window;
	private Tab writeTab;
	private WriteFrame write_window;
	private Tab saveTab;
	private SaveDataFrame save_window;
	private Tab ioTab;
	private DigitalIOSettingFrame io_window;
//	private Tab decodeTab;
//	private DecodeSettingFrame decode_window;
	
	
	public MainWindow() {
		this.initComponents();
	}

	public MainWindow(Tab... arg0) {
		super(arg0);
	}

	private void initComponents() {
		readTab = new Tab("Read");
		readTab.setClosable(false);
		read_window = new ReadFrame();
		readTab.setContent(read_window);
		readTab.setOnSelectionChanged(this);
		
		writeTab = new Tab("Write");
		writeTab.setClosable(false);
		write_window = new WriteFrame();
		writeTab.setContent(write_window);
		writeTab.setOnSelectionChanged(this);
		
		saveTab = new Tab("Save Data");
		saveTab.setClosable(false);
		save_window = new SaveDataFrame();
		saveTab.setContent(save_window);
		saveTab.setOnSelectionChanged(this);
		
		ioTab = new Tab("Digital I/O");
		ioTab.setClosable(false);
		io_window = new DigitalIOSettingFrame();
		ioTab.setContent(io_window);
		ioTab.setOnSelectionChanged(this);
		
//		decodeTab = new Tab("EPC Decode Setting");
//		decodeTab.setClosable(false);
//		decode_window = new DecodeSettingFrame();
//		decodeTab.setContent(decode_window);
//		decodeTab.setOnSelectionChanged(this);
		
		this.getTabs().addAll(readTab, writeTab, saveTab, ioTab);
//		this.getTabs().addAll(readTab, writeTab);
//		this.setStyle("-fx-background-color: #F0F0F0;");
	}

	@Override
	public void handle(Event event) {
		// when switch from ioTab to any other tab, save the io setting
		if ((event.getSource() == ioTab) && !ioTab.isSelected()) {
			io_window.applySetting();
		}
		
		if ((event.getSource() == writeTab) && writeTab.isSelected()) {
			write_window.setDisable(!ReaderUtility.getInstance().isConnected());
		}
		
		if (readTab.isSelected()) {
			read_window.checkDITrigger();
		} 
		
//		if ((event.getSource() == decodeTab) && decodeTab.isSelected()) {
//			decode_window.disableReadFunction(!ReaderUtility.getInstance().isConnected());
//		}
	}
	
}
