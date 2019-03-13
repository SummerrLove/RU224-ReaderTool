package scannel.ui;

import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinState;
import com.thingmagic.Gen2.Session;
import com.thingmagic.Gen2.Target;
import com.thingmagic.Reader.Region;

import java.io.IOException;

import com.thingmagic.ReaderException;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import scannel.gpio.DigitalIOController;
import scannel.gpio.DigitalInputListener;
import scannel.reader.DatabaseUtility;
import scannel.reader.MyLogger;
import scannel.reader.ReaderConfig;
import scannel.reader.ReaderUtility;

public class ConfigurationFrame extends AnchorPane implements EventHandler<ActionEvent>, DigitalInputListener {

	private TextField power;
	private AntennaFrame antenna_list;
	private ChoiceBox<Session> cb_session;
	private ChoiceBox<Target> cb_target;
	private ChoiceBox<Region> cb_region;
	private Button btn_start;
	private RadioButton rb_hoptable;
	private TextArea hoptable;
	private static ObservableList<Region> SUPPORT_REGIONS = FXCollections.observableArrayList();
//			FXCollections.observableArrayList("NA", "IN", "PRC", "EU3", "KR2", "AU", "NZ", "MY", "ID", "PH", "TW", "MO", "RU", "SG");
	
	private final static boolean ACTIVATE_HOPTABLE = false;
	
	
	public ConfigurationFrame() {
		this.initComponents();
		DigitalIOController.getInstance().setDigitalInputListener(this);
	}

	public ConfigurationFrame(Node... children) {
		super(children);
	}

	private void initComponents() {
		
		Label power_title = new Label("RF Power (dbm):");
		power_title.setFont(Font.font("Arial", FontWeight.BOLD, 16));
		AnchorPane.setLeftAnchor(power_title, 30.0);
		AnchorPane.setTopAnchor(power_title, 10.0);
		this.getChildren().add(power_title);
		
		power = new TextField();
		power.setPrefWidth(60);
		AnchorPane.setLeftAnchor(power, 30.0);
		AnchorPane.setTopAnchor(power, 40.0);
		this.getChildren().add(power);
		
		antenna_list = new AntennaFrame();
		antenna_list.setVgap(15);
		antenna_list.setHgap(15);
		antenna_list.setPadding(new Insets(20, 10, 20, 10));
//		antenna_list.initComponents();
		AnchorPane.setLeftAnchor(antenna_list, 20.0);
		AnchorPane.setTopAnchor(antenna_list, 70.0);
		this.getChildren().add(antenna_list);
		
		
		Label session_title = new Label("Session: ");
		session_title.setFont(Font.font("Arial", FontWeight.BOLD, 16));
		AnchorPane.setLeftAnchor(session_title, 30.0);
		AnchorPane.setTopAnchor(session_title, 210.0);
		this.getChildren().add(session_title);
		
		cb_session = new ChoiceBox<Session>(FXCollections.observableArrayList(Session.S0, Session.S1, Session.S2, Session.S3));
		cb_session.setTooltip(new Tooltip("Select a session value"));
//		cb_session.setValue(Session.S0);
		AnchorPane.setLeftAnchor(cb_session, 110.0);
		AnchorPane.setTopAnchor(cb_session, 210.0);
		this.getChildren().add(cb_session);
		
		
		Label target_title = new Label("Target: ");
		target_title.setFont(Font.font("Arial", FontWeight.BOLD, 16));
		AnchorPane.setLeftAnchor(target_title, 30.0);
		AnchorPane.setTopAnchor(target_title, 260.0);
		this.getChildren().add(target_title);
		
		cb_target = new ChoiceBox<Target>(FXCollections.observableArrayList(Target.A, Target.B, Target.AB, Target.BA));
//		cb_target.setValue("A");
		AnchorPane.setLeftAnchor(cb_target, 95.0);
		AnchorPane.setTopAnchor(cb_target, 260.0);
		this.getChildren().add(cb_target);
		
		
		Label region_title = new Label("Region: ");
		region_title.setFont(Font.font("Arial", FontWeight.BOLD, 16));
		AnchorPane.setLeftAnchor(region_title, 30.0);
		AnchorPane.setTopAnchor(region_title, 310.0);
		this.getChildren().add(region_title);
		
//		cb_region = new ChoiceBox<String>(FXCollections.observableArrayList("NA", "EU", "TW"));
		cb_region = new ChoiceBox<Region>();
//		cb_region.setValue("NA");
		AnchorPane.setLeftAnchor(cb_region, 100.0);
		AnchorPane.setTopAnchor(cb_region, 310.0);
		this.getChildren().add(cb_region);
		
		
		btn_start = new Button("Start");
		btn_start.setOnAction(this);
		btn_start.setFont(new Font("Arial", 14));
		AnchorPane.setLeftAnchor(btn_start, 30.0);
		AnchorPane.setTopAnchor(btn_start, 350.0);
		this.getChildren().add(btn_start);
		
		
		Separator separator = new Separator();
		separator.setOrientation(Orientation.HORIZONTAL);
		AnchorPane.setLeftAnchor(separator, 20.0);
		AnchorPane.setRightAnchor(separator, 20.0);
		AnchorPane.setTopAnchor(separator, 390.0);
		this.getChildren().add(separator);
		
		
		if (ACTIVATE_HOPTABLE) {
			rb_hoptable = new RadioButton("Hop Table");
			rb_hoptable.setOnAction(this);
			AnchorPane.setLeftAnchor(rb_hoptable, 30.0);
			AnchorPane.setTopAnchor(rb_hoptable, 410.0);
			this.getChildren().add(rb_hoptable);
			
			hoptable = new TextArea();
			hoptable.setWrapText(true);
			hoptable.setPromptText("Frequency value in kHz");
			AnchorPane.setLeftAnchor(hoptable, 30.0);
			AnchorPane.setTopAnchor(hoptable, 440.0);
			AnchorPane.setRightAnchor(hoptable, 30.0);
			AnchorPane.setBottomAnchor(hoptable, 30.0);
			this.getChildren().add(hoptable);
			hoptable.setDisable(true);
		}
	}

	@Override
	public void handle(ActionEvent event) {
		if (!ReaderUtility.getInstance().isConnected()) {
			System.out.println("[ConfigurationFrame] reader is not connected, so ignore event...");
			return;
		}
		
		if (event.getSource() == btn_start) {
			if (ReaderUtility.getInstance().isReading()) {
				// stop reading tag
				System.out.println("[ConfigurationFrame] press stop button");
				this.stopReading();
			} else {
				// start reading tag
				System.out.println("[ConfigurationFrame] press start button");
				this.startReading();
			}
		}
		
		
		if (ACTIVATE_HOPTABLE && (event.getSource() == rb_hoptable)) {
			if (rb_hoptable.isSelected()) {
				System.out.println("RadioButon selected.");
				try {
					this.setHopTableText(ReaderUtility.getInstance().getHopTable());
				} catch (ReaderException e) {
					e.printStackTrace();
				}
				hoptable.setDisable(false);
			} else {
				System.out.println("RadioButon un-selected.");
//				this.parseHopTable(hoptable.getText());
				hoptable.setDisable(true);
			}
		}
	}
	
	private void startReading() {
		if (!ReaderUtility.getInstance().isConnected()) {
			return;
		}
		
		if (ReaderUtility.getInstance().isReading()) {
			System.out.println("Reader is reading tags, so ignore function call...");
			return;
		}
		
		try {
			this.setRFPower(Integer.parseInt(power.getText()));
			this.setSession(cb_session.getSelectionModel().getSelectedItem());
			this.setTargetFlag(cb_target.getSelectionModel().getSelectedItem());
			this.setRegion(cb_region.getSelectionModel().getSelectedItem());
			
			if (ACTIVATE_HOPTABLE && rb_hoptable.isSelected()) {
				// Use user-defined frequency list
				ReaderUtility.getInstance().setHopTable(this.parseHopTable(hoptable.getText()));
			} else {
				// Use default hop table for selected region. 
				// However, if the reader hop table has been modified, 
				// the region parameter of the reader must be set again to reset the hop table to default value.   
			}
			
			int[] ant_setting = antenna_list.getAntennaList();
			ReaderConfig.getInstance().setAntennaList(ant_setting);
			ReaderUtility.getInstance().resetData();
			ReaderUtility.getInstance().startReading(ant_setting);
			btn_start.setText("Stop");
		} catch (ReaderException e) {
			e.printStackTrace();
		}
	}
	
	private void stopReading() {
		if (!ReaderUtility.getInstance().isConnected()) {
			return;
		}
		
		if (!ReaderUtility.getInstance().isReading()) {
			System.out.println("Reader has stopped reading already, so ignore function call...");
			return;
		}
		
		ReaderUtility.getInstance().stopReading();
		btn_start.setText("Start");
		
		if (ReaderConfig.getInstance().isDataLocalSaveEnabled()) {
			try {
				ReaderUtility.getInstance().saveTagDataToFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if (ReaderConfig.getInstance().isDataDBSaveEnabled()) {
			DatabaseUtility.getInstance().insertTagData();
		}
	}
	
	private void setRFPower(int rfpower) throws ReaderException {
		MyLogger.printLog("Set RFPower: "+rfpower);
		ReaderUtility.getInstance().setRFPower(rfpower);
		ReaderConfig.getInstance().setRFPower(rfpower);
	}
	
	private void setSession(Session session) throws ReaderException {
		MyLogger.printLog("Set Session: "+session);
		ReaderUtility.getInstance().setSession(session);
		ReaderConfig.getInstance().setSession(session);
		
//		switch (cb_session.getSelectionModel().getSelectedIndex()) {
//		case 0:
//			System.out.println("Set Session: Session 0");
//			ReaderUtility.getInstance().setSession(Gen2.Session.S0);
//			break;
//		case 1:
//			System.out.println("Set Session: Session 1");
//			ReaderUtility.getInstance().setSession(Gen2.Session.S1);
//			break;
//		case 2:
//			System.out.println("Set Session: Session 2");
//			ReaderUtility.getInstance().setSession(Gen2.Session.S2);
//			break;
//		case 3:
//			System.out.println("Set Session: Session 3");
//			ReaderUtility.getInstance().setSession(Gen2.Session.S3);
//			break;
//		}
	}
	
	private void setTargetFlag(Target target) throws ReaderException {
		MyLogger.printLog("Set Target: "+target);
		ReaderUtility.getInstance().setTargetFlag(target);
		ReaderConfig.getInstance().setTargetFlag(target);
		
//		switch (cb_target.getSelectionModel().getSelectedIndex()) {
//		case 0:
//			MyLogger.printLog("Set Target: A then B");
//			ReaderUtility.getInstance().setTargetFlag(Gen2.Target.AB);
//			break;
//		case 1:
//			MyLogger.printLog("Set Target: B then A");
//			ReaderUtility.getInstance().setTargetFlag(Gen2.Target.BA);
//			break;
//		case 2:
//			MyLogger.printLog("Set Target: Only A");
//			ReaderUtility.getInstance().setTargetFlag(Gen2.Target.A);
//			break;
//		case 3:
//			MyLogger.printLog("Set Target: Only B");
//			ReaderUtility.getInstance().setTargetFlag(Gen2.Target.B);
//			break;
//		}
	}
	
	private void setRegion(Region region) throws ReaderException {
		MyLogger.printLog("Set Region: "+region);
		ReaderUtility.getInstance().setRegion(region);
		ReaderConfig.getInstance().setRegion(region);
		
//		switch (cb_target.getSelectionModel().getSelectedIndex()) {
//		case 0:
//			ReaderUtility.getInstance().setRegion(Region.NA);
//			break;
//		case 1:
//			ReaderUtility.getInstance().setRegion(Region.EU);
//			break;
//		case 2:
//			ReaderUtility.getInstance().setRegion(Region.TW);
//			break;
//		}
	}
	
	private int[] parseHopTable(String text) {
		MyLogger.printLog("Parse Hop Table Text");
		String[] strList = text.split(",");
		int[] frequencyList = new int[strList.length];
		
		for (int i=0; i<strList.length; i++) {
			try {
				frequencyList[i] = Integer.parseInt(strList[i]);
				MyLogger.printLog("["+i+"]: "+strList[i]);
			} catch (NumberFormatException e) {
				System.out.println("Cannot parse string: "+strList[i]);
				frequencyList[i] = 0;
			}
		}
		
		return frequencyList;
	}
	
	private void setHopTableText(int[] frequencyList) {
		String text = "";
		
		for (int i=0; i<frequencyList.length; i++) {
			if (i == 0) {
				text = Integer.toString(frequencyList[i]);
			} else {
				text += ","+Integer.toString(frequencyList[i]);
			}
		}
		
		hoptable.setText(text);
	}
	
	public void resetStartButton() {
		btn_start.setText("Start");
	}
	
//	private ObservableList getRegionList(){
//		ObservableList list = FXCollections.observableArrayList();
//		Region[] regions;
//		try {
//			regions = ReaderUtility.getInstance().getSupportedRegion();
//		} catch (ReaderException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return null;
//		}
//		
//		for (int i=0; i<regions.length; i++) {
//			list.add(regions[i]);
//		}
//		return list;
//	}
	
	private void loadSupportRegion() {
		try {
			SUPPORT_REGIONS.addAll(ReaderUtility.getInstance().getSupportedRegion());
			cb_region.setItems(SUPPORT_REGIONS);
		} catch (ReaderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void loadConfig() {
		if (cb_region.getItems().size() == 0) {
			loadSupportRegion();
		}
		
		power.setText(Integer.toString(ReaderConfig.getInstance().getRFPower()));
		antenna_list.setAntennaList(ReaderConfig.getInstance().getAntennaList());
		cb_session.setValue(ReaderConfig.getInstance().getSession());
		cb_target.setValue(ReaderConfig.getInstance().getTargetFlag());
		cb_region.setValue(ReaderConfig.getInstance().getRegion());
	}
	
	public void disableStartButton(boolean disable) {
		btn_start.setDisable(disable);
	}

	@Override
	public void handleInput(GpioPinDigitalInput pin, PinState state) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void digitalInputOn() {
		// TODO Auto-generated method stub
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				startReading();
			}
			
		});
		
	}

	@Override
	public void digitalInputOff() {
		// TODO Auto-generated method stub
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				stopReading();
			}
			
		});
	}
}
