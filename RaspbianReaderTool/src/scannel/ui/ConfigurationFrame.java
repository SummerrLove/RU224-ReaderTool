package scannel.ui;

import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinState;
import com.thingmagic.Gen2.Session;
import com.thingmagic.Gen2.Target;
import com.thingmagic.Reader.Region;

import java.io.IOException;

import javax.xml.bind.DatatypeConverter;

import com.thingmagic.ReaderException;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Alert.AlertType;
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
	private Button btn_reset;
//	private TextField filter;
	private RadioButton rb_tid;
	private RadioButton rb_userbank;
	private static ObservableList<Region> SUPPORT_REGIONS = FXCollections.observableArrayList();
	
	
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
		AnchorPane.setLeftAnchor(antenna_list, 20.0);
		AnchorPane.setTopAnchor(antenna_list, 70.0);
		this.getChildren().add(antenna_list);
		
		
		Label session_title = new Label("Session: ");
		session_title.setFont(Font.font("Arial", FontWeight.BOLD, 16));
		AnchorPane.setLeftAnchor(session_title, 30.0);
		AnchorPane.setTopAnchor(session_title, 170.0);
		this.getChildren().add(session_title);
		
		cb_session = new ChoiceBox<Session>(FXCollections.observableArrayList(Session.S0, Session.S1, Session.S2, Session.S3));
		cb_session.setTooltip(new Tooltip("Select a session value"));
		AnchorPane.setLeftAnchor(cb_session, 110.0);
		AnchorPane.setTopAnchor(cb_session, 170.0);
		this.getChildren().add(cb_session);
		
		
		Label target_title = new Label("Target: ");
		target_title.setFont(Font.font("Arial", FontWeight.BOLD, 16));
		AnchorPane.setLeftAnchor(target_title, 30.0);
		AnchorPane.setTopAnchor(target_title, 220.0);
		this.getChildren().add(target_title);
		
		cb_target = new ChoiceBox<Target>(FXCollections.observableArrayList(Target.A, Target.B, Target.AB, Target.BA));
		AnchorPane.setLeftAnchor(cb_target, 95.0);
		AnchorPane.setTopAnchor(cb_target, 220.0);
		this.getChildren().add(cb_target);
		
		
		Label region_title = new Label("Region: ");
		region_title.setFont(Font.font("Arial", FontWeight.BOLD, 16));
		AnchorPane.setLeftAnchor(region_title, 30.0);
		AnchorPane.setTopAnchor(region_title, 270.0);
		this.getChildren().add(region_title);
		
		cb_region = new ChoiceBox<Region>();
		AnchorPane.setLeftAnchor(cb_region, 100.0);
		AnchorPane.setTopAnchor(cb_region, 270.0);
		this.getChildren().add(cb_region);
		
		
		rb_tid = new RadioButton("show TID");
		rb_tid.setOnAction(this);
		AnchorPane.setLeftAnchor(rb_tid, 30.0);
		AnchorPane.setTopAnchor(rb_tid, 350.0);
		this.getChildren().add(rb_tid);
		
		rb_userbank = new RadioButton("show User Memory");
		rb_userbank.setOnAction(this);
		AnchorPane.setLeftAnchor(rb_userbank, 30.0);
		AnchorPane.setTopAnchor(rb_userbank, 380.0);
		this.getChildren().add(rb_userbank);
		
		
		btn_start = new Button("Start");
		btn_start.setOnAction(this);
		btn_start.setFont(Font.font("Arial", FontWeight.BOLD, 24));
		btn_start.setPrefSize(150, 40);
		AnchorPane.setLeftAnchor(btn_start, 50.0);
		AnchorPane.setTopAnchor(btn_start, 500.0);
		this.getChildren().add(btn_start);
		
		btn_reset = new Button("Reset");
		btn_reset.setOnAction(this);
		btn_reset.setFont(Font.font("Arial", FontWeight.BOLD, 24));
		btn_reset.setPrefSize(150, 40);
		AnchorPane.setLeftAnchor(btn_reset, 50.0);
		AnchorPane.setTopAnchor(btn_reset, 560.0);
		this.getChildren().add(btn_reset);
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
		
		if (event.getSource() == btn_reset) {
			ReaderUtility.getInstance().resetData();
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
			
			
			ReaderUtility.getInstance().includeTID(rb_tid.isSelected());
			ReaderUtility.getInstance().includeUSERBANK(rb_userbank.isSelected());
			
			int[] ant_setting = antenna_list.getAntennaList();
			ReaderConfig.getInstance().setAntennaList(ant_setting);
			ReaderUtility.getInstance().resetData();
			ReaderUtility.getInstance().startReading(ant_setting);
			btn_start.setText("Stop");
		} catch (ReaderException e) {
			e.printStackTrace();
			
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText(null);
			alert.setContentText(e.getMessage());
			alert.showAndWait();
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
		
	}
	
	private void setTargetFlag(Target target) throws ReaderException {
		MyLogger.printLog("Set Target: "+target);
		ReaderUtility.getInstance().setTargetFlag(target);
		ReaderConfig.getInstance().setTargetFlag(target);
		
	}
	
	private void setRegion(Region region) throws ReaderException {
		MyLogger.printLog("Set Region: "+region);
		ReaderUtility.getInstance().setRegion(region);
		ReaderConfig.getInstance().setRegion(region);
		
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
	
	public void resetStartButton() {
		btn_start.setText("Start");
	}
	
	private void loadSupportRegion() {
		try {
			SUPPORT_REGIONS.addAll(ReaderUtility.getInstance().getSupportedRegion());
			cb_region.setItems(SUPPORT_REGIONS);
		} catch (ReaderException e) {
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
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				startReading();
			}
			
		});
		
	}

	@Override
	public void digitalInputOff() {
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				stopReading();
			}
			
		});
	}
}
