package scannel.ui;

import com.thingmagic.ReaderException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import scannel.reader.DatabaseUtility;
import scannel.reader.ReaderConfig;
import scannel.reader.ReaderUtility;

public class ControlFrame extends AnchorPane implements EventHandler<ActionEvent> {

	private boolean isConnected;
	private Button btn_connect;
	private ConfigurationFrame config;
	private Circle status_light;
	private Label status;
	
	private final static boolean USE_SERIAL_PORT = false;
	private final static String URI_SERIAL = "tmr:///dev/ttyS0";
	private final static String URI_IP = "192.168.100.160";
	private final static String URI_PORT = "4001";
	
	
	public ControlFrame() {
		
		this.initComponents();
		isConnected = false;
//		config.setDisable(true);
	}

	public ControlFrame(Node... children) {
		super(children);
	}

	private void initComponents() {
		btn_connect = new Button("Connect");
		btn_connect.setOnAction(this);
		btn_connect.setFont(new Font("Arial", 20));
		AnchorPane.setLeftAnchor(btn_connect, 50.0);
		AnchorPane.setTopAnchor(btn_connect, 30.0);
		btn_connect.setPrefWidth(150);
		this.getChildren().add(btn_connect);
		
		status_light = new Circle(60, 100, 10);
		status_light.setFill(Color.RED);
		status_light.setStroke(Color.BLACK);
//		status_light.setStrokeWidth(5);
		this.getChildren().add(status_light);
		
		status = new Label("Disconnected");
		status.setFont(Font.font("Arial", 16));
		AnchorPane.setLeftAnchor(status, 80.0);
		AnchorPane.setTopAnchor(status, 90.0);
		this.getChildren().add(status);
		
		Separator separator = new Separator();
		separator.setOrientation(Orientation.HORIZONTAL);
		AnchorPane.setLeftAnchor(separator, 20.0);
		AnchorPane.setRightAnchor(separator, 20.0);
		AnchorPane.setTopAnchor(separator, 120.0);
		this.getChildren().add(separator);
		
		config = new ConfigurationFrame();
		AnchorPane.setLeftAnchor(config, 0.0);
		AnchorPane.setTopAnchor(config, 130.0);
		AnchorPane.setBottomAnchor(config, 0.0);
		config.setPrefWidth(250);
		this.getChildren().add(config);
	}

	@Override
	public void handle(ActionEvent event) {
		if (event.getSource() == btn_connect) {
			
			if (isConnected) {
				System.out.println("[ControlFrame] pressed disconnect button...");
				disconnectReader();
			} else {
				System.out.println("[ControlFrame] pressed connect button...");
//				ReaderUtility.getInstance().createTestTagData();
//				DatabaseUtility.getInstance().insertTagData();
				connectReader();
//				try {
//					ReaderUtility.getInstance().printSupportRegeion();
//				} catch (ReaderException e) {
//					e.printStackTrace();
//				}
			}
			
		} else {
			System.out.println("[ControlFrame] unknown event source...");
		}
	}
	
	private void disconnectReader() {
		ReaderUtility.getInstance().disconnectReader();
		btn_connect.setText("Connect");
		isConnected = false;
		config.resetStartButton();;
		config.setDisable(true);
		status_light.setFill(Color.RED);
		status.setText("Disconnected");
	}
	
	private void connectReader() {
		try {
			
			if (USE_SERIAL_PORT) {
				// Since the reader is connect to raspberry pi by UART, 
				// use "tmr:///dev/ttyS0" as default connection path
				ReaderUtility.getInstance().connectReader(URI_SERIAL);
			} else {
				// Connect reader through ethernet for testing, reader ip and port may change 
				ReaderUtility.getInstance().connectReader(URI_IP, URI_PORT);
			}
			
			btn_connect.setText("Disconnect");
			isConnected = true;
			status_light.setFill(Color.LIGHTGREEN);
			status.setText("Connected");
			if (!ReaderConfig.getInstance().getDITrigger()) {
				config.setDisable(false);
			}
			config.loadConfig();
			
		} catch (ReaderException e) {
			e.printStackTrace();
			System.out.println("Connection Failed");
		}
	}
	
	// Disable start button when reader is set to start reading by digital input
	public void checkDITrigger() {
		if (ReaderUtility.getInstance().isConnected()) {
			config.setDisable(ReaderConfig.getInstance().getDITrigger());
		} else {
			config.setDisable(true);
		}
	}
}
