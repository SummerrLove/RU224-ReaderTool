package scannel.ui;

import com.thingmagic.ReaderException;

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
	private Tab hopTableTab;
	private HopTableFrame hopTable_window;
	private Tab filterTab;
	private FilterSettingFrame filter_window;
	private Tab regulatoryTab;
	private RegulatoryTestFrame regulatory_window;
	
	
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
		
		hopTableTab = new Tab("Hop Table");
		hopTableTab.setClosable(false);
		hopTable_window = new HopTableFrame();
		hopTableTab.setContent(hopTable_window);
		hopTableTab.setOnSelectionChanged(this);
		
		filterTab = new Tab("Filter");
		filterTab.setClosable(false);
		filter_window = new FilterSettingFrame();
		filterTab.setContent(filter_window);
		filterTab.setOnSelectionChanged(this);
		
		regulatoryTab = new Tab("Regulatory Test");
		regulatoryTab.setClosable(false);
		regulatory_window = new RegulatoryTestFrame();
		regulatoryTab.setContent(regulatory_window);
		regulatoryTab.setOnSelectionChanged(this);
		
		this.getTabs().addAll(readTab, hopTableTab, regulatoryTab, filterTab, writeTab, saveTab, ioTab);
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
		
		if ((event.getSource() == hopTableTab) && hopTableTab.isSelected()) {
			hopTable_window.enableInput(ReaderUtility.getInstance().isConnected());
			
			if (ReaderUtility.getInstance().isConnected()) {
				try {
					hopTable_window.setHopTableText(ReaderUtility.getInstance().getHopTable());
				} catch (ReaderException e) {
					e.printStackTrace();
				}
			}
		}
		
//		if ((event.getSource() == decodeTab) && decodeTab.isSelected()) {
//			decode_window.disableReadFunction(!ReaderUtility.getInstance().isConnected());
//		}
	}
	
}
