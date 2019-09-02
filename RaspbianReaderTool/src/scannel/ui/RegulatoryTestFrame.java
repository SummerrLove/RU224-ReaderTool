package scannel.ui;

import com.thingmagic.Reader;
import com.thingmagic.ReaderException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import scannel.reader.ReaderUtility;

public class RegulatoryTestFrame extends AnchorPane implements EventHandler<ActionEvent> {

	private RadioButton rb_mode_continuous;
//	private RadioButton rb_mode_timed;
	private RadioButton rb_modulation_cw;
	private RadioButton rb_modulation_prbs;
	private AntennaFrame antenna_list;
	private TextField tf_onTime;
	private TextField tf_offTime;
	private Button btn_start;
	private Button btn_stop;
	private static boolean statusOn = false;
	
	
	public RegulatoryTestFrame() {
		this.initComponents();
		
		rb_mode_continuous.setSelected(true);
		rb_modulation_cw.setSelected(true);
	}

	public RegulatoryTestFrame(Node... arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	private void initComponents() {
		Label mode = new Label("Mode:");
		mode.setFont(Font.font("Arial", FontWeight.BOLD, 24));
		AnchorPane.setLeftAnchor(mode, 50.0);
		AnchorPane.setTopAnchor(mode, 50.0);
		this.getChildren().add(mode);
		
		rb_mode_continuous = new RadioButton("Continuous");
		rb_mode_continuous.setFont(Font.font("Arial", FontWeight.BOLD, 16));
		rb_mode_continuous.setOnAction(this);
		AnchorPane.setLeftAnchor(rb_mode_continuous, 50.0);
		AnchorPane.setTopAnchor(rb_mode_continuous, 90.0);
		this.getChildren().add(rb_mode_continuous);
		
//		rb_mode_timed = new RadioButton("Timed");
//		rb_mode_timed.setFont(Font.font("Arial", FontWeight.BOLD, 16));
//		rb_mode_timed.setOnAction(this);
//		AnchorPane.setLeftAnchor(rb_mode_timed, 200.0);
//		AnchorPane.setTopAnchor(rb_mode_timed, 90.0);
//		this.getChildren().add(rb_mode_timed);
		
		
		Label modulation = new Label("Modulation:");
		modulation.setFont(Font.font("Arial", FontWeight.BOLD, 24));
		AnchorPane.setLeftAnchor(modulation, 50.0);
		AnchorPane.setTopAnchor(modulation, 150.0);
		this.getChildren().add(modulation);
		
		rb_modulation_cw = new RadioButton("CW");
		rb_modulation_cw.setFont(Font.font("Arial", FontWeight.BOLD, 16));
		rb_modulation_cw.setOnAction(this);
		AnchorPane.setLeftAnchor(rb_modulation_cw, 50.0);
		AnchorPane.setTopAnchor(rb_modulation_cw, 190.0);
		this.getChildren().add(rb_modulation_cw);
		
		rb_modulation_prbs = new RadioButton("PRBS");
		rb_modulation_prbs.setFont(Font.font("Arial", FontWeight.BOLD, 16));
		rb_modulation_prbs.setOnAction(this);
		AnchorPane.setLeftAnchor(rb_modulation_prbs, 200.0);
		AnchorPane.setTopAnchor(rb_modulation_prbs, 190.0);
		this.getChildren().add(rb_modulation_prbs);
		
		
		antenna_list = new AntennaFrame();
		antenna_list.setVgap(15);
		antenna_list.setHgap(15);
		antenna_list.setPadding(new Insets(0, 10, 20, 10));
		AnchorPane.setLeftAnchor(antenna_list, 40.0);
		AnchorPane.setTopAnchor(antenna_list, 250.0);
		this.getChildren().add(antenna_list);
		
		
		Label title_ontime = new Label("On Time(ms):");
		title_ontime.setFont(Font.font("Arial", FontWeight.BOLD, 24));
		AnchorPane.setLeftAnchor(title_ontime, 50.0);
		AnchorPane.setTopAnchor(title_ontime, 350.0);
		this.getChildren().add(title_ontime);
		
		tf_onTime = new TextField();
		tf_onTime.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
		tf_onTime.setPromptText("2 ~ 65535");
		tf_onTime.setPrefWidth(100);
		AnchorPane.setLeftAnchor(tf_onTime, 50.0);
		AnchorPane.setTopAnchor(tf_onTime, 390.0);
		this.getChildren().add(tf_onTime);
		
		
		Label title_offtime = new Label("Off Time(ms):");
		title_offtime.setFont(Font.font("Arial", FontWeight.BOLD, 24));
		AnchorPane.setLeftAnchor(title_offtime, 50.0);
		AnchorPane.setTopAnchor(title_offtime, 450.0);
		this.getChildren().add(title_offtime);
		
		tf_offTime = new TextField();
		tf_offTime.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
		tf_offTime.setPromptText("2 ~ 65535");
		tf_offTime.setPrefWidth(100);
		AnchorPane.setLeftAnchor(tf_offTime, 50.0);
		AnchorPane.setTopAnchor(tf_offTime, 490.0);
		this.getChildren().add(tf_offTime);
		
		
		btn_start = new Button("Start");
		btn_start.setFont(Font.font("Arial", FontWeight.NORMAL, 20));
		btn_start.setOnAction(this);
		AnchorPane.setLeftAnchor(btn_start, 50.0);
		AnchorPane.setTopAnchor(btn_start, 600.0);
		this.getChildren().add(btn_start);
	}

	@Override
	public void handle(ActionEvent event) {
		if (event.getSource() == rb_mode_continuous) {
			rb_mode_continuous.setSelected(true);
//			rb_mode_timed.setSelected(!rb_mode_continuous.isSelected());
//		} else if (event.getSource() == rb_mode_timed) {
//			rb_mode_continuous.setSelected(!rb_mode_timed.isSelected());
		} else if (event.getSource() == rb_modulation_cw) {
			rb_modulation_prbs.setSelected(!rb_modulation_cw.isSelected());
		} else if (event.getSource() == rb_modulation_prbs) {
			rb_modulation_cw.setSelected(!rb_modulation_prbs.isSelected());
		} else if (event.getSource() == btn_start) {
			if (statusOn) {
				stopRegulatoryTest();
				statusOn = false;
				btn_start.setText("Start");
			} else {
				try {
					startRegulatoryTest();
					statusOn = true;
					btn_start.setText("Stop");
				} catch (ReaderException e) {
					e.printStackTrace();
				}
				
			}
		}
	}
	
	private void startRegulatoryTest() throws ReaderException {
		if (!ReaderUtility.getInstance().isConnected()) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText(null);
			alert.setContentText("Reader is not connected...");
			alert.showAndWait();
			return;
		}
		
		
		int[] antList = antenna_list.getAntennaList();
		if (antList.length == 0) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText(null);
			alert.setContentText("Invalid antenna setting...");
			alert.showAndWait();
			return;
		}
		
		Reader.RegulatoryMode regMode = null;
		if (rb_mode_continuous.isSelected()) {
			regMode = Reader.RegulatoryMode.CONTINUOUS;
//		} else if (rb_mode_timed.isSelected()) {
//			regMode = Reader.RegulatoryMode.TIMED;
		}
		
		Reader.RegulatoryModulation regModulation = null;
		if (rb_modulation_cw.isSelected()) {
			regModulation = Reader.RegulatoryModulation.CW;
		} else if (rb_modulation_prbs.isSelected()) {
			regModulation = Reader.RegulatoryModulation.PRBS;
		}
		
		int regOnTime = Integer.parseInt(tf_onTime.getText());
		int regOffTime = Integer.parseInt(tf_offTime.getText());
		
//		ReaderUtility.getInstance().startRegulatoryTest(antList, regMode, regModulation, regOnTime, regOffTime);
		ReaderUtility.getInstance().startRegulatoryTest(regModulation, regOnTime, regOffTime);
		
	}
	
	private void stopRegulatoryTest() {
		ReaderUtility.getInstance().stopRegulatoryTest();
	}
}
