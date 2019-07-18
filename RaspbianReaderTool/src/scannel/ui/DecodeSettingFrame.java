package scannel.ui;

import com.thingmagic.ReaderException;
import com.thingmagic.TagReadData;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import scannel.decoder.GS1Decoder;
import scannel.decoder.GS1Decoder.EPC_SCHEMA;
import scannel.reader.MyLogger;
import scannel.reader.ReaderUtility;

public class DecodeSettingFrame extends AnchorPane implements EventHandler<ActionEvent> {
	
	private static boolean[] setting = new boolean[4];
	private RadioButton[] rb_setting = new RadioButton[4]; 
	private static boolean[] setting_schema = new boolean[17];
	private RadioButton[] rb_schema = new RadioButton[17];
	private Button btn_read;
	private TextField tf_epc;
	private EPC_SCHEMA[] schemas;
	
	private static String[] str_setting = {"UDC", "EAN / UPC", "EAN / UPC + EAS", "EPC Raw Data"};
	
	
	public static int UDC 			= 0;
	public static int EANUPC 		= 1;
	public static int EANUPC_EAS 	= 2;
	public static int RAWDATA		= 3;
	
	public static enum ENCODE_TYPE{
		UDC,
		EAN_UPC,
		EAN_UPC_EAS,
		RAWDATA;
	}
	
	public DecodeSettingFrame() {
		this.initComponents();
	}

	public DecodeSettingFrame(Node... arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	private void initComponents() {
		Rectangle rect1 = new Rectangle(40, 80, 360, 400);
		rect1.setArcHeight(15);
		rect1.setArcWidth(15);
//		rect.setFill(Color.TRANSPARENT);
		rect1.setFill(Color.WHITE);
		rect1.setStroke(Color.BLACK);
		this.getChildren().add(rect1);
		
		Label decode_title = new Label("EPC Decode Format");
		decode_title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
		decode_title.setStyle("-fx-background-color: #F0F0F0;");
		AnchorPane.setLeftAnchor(decode_title, 50.0);
		AnchorPane.setTopAnchor(decode_title, 30.0);
		this.getChildren().add(decode_title);
		
		for (int i=0; i<rb_setting.length; i++) {
			rb_setting[i] = new RadioButton(str_setting[i]);
			rb_setting[i].setFont(Font.font("Arial", FontWeight.NORMAL, 14));
			rb_setting[i].setOnAction(this);
			AnchorPane.setLeftAnchor(rb_setting[i], 100.0);
			AnchorPane.setTopAnchor(rb_setting[i], 120.0 + i*50);
			this.getChildren().add(rb_setting[i]);
		}
		
		// Currently not implemented, so disable the RadioButtons
		rb_setting[UDC].setDisable(true);
		rb_setting[EANUPC_EAS].setDisable(true);

		
		Rectangle rect2 = new Rectangle(450, 80, 450, 400);
		rect2.setArcHeight(15);
		rect2.setArcWidth(15);
		rect2.setFill(Color.WHITE);
		rect2.setStroke(Color.BLACK);
		this.getChildren().add(rect2);
		
		Label schema_title = new Label("EPC Schema");
		schema_title.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		AnchorPane.setLeftAnchor(schema_title, 500.0);
		AnchorPane.setTopAnchor(schema_title, 100.0);
		this.getChildren().add(schema_title);
		
		schemas = EPC_SCHEMA.values();
		setting_schema = new boolean[schemas.length];
		rb_schema = new RadioButton[schemas.length];
		
		int x, y;
		for (int i=0; i<rb_schema.length; i++) {
			rb_schema[i] = new RadioButton(schemas[i].name());
			rb_schema[i].setMnemonicParsing(false);
			rb_schema[i].setFont(Font.font("Arial", FontWeight.BOLD, 14));
			
			x = i%2;
			y = i/2;
			AnchorPane.setLeftAnchor(rb_schema[i], 500+x*200.0);
			AnchorPane.setTopAnchor(rb_schema[i], 140+y*30.0);
			this.getChildren().add(rb_schema[i]);
			rb_schema[i].setDisable(true);
		}
		
		Separator separator = new Separator();
		separator.setOrientation(Orientation.HORIZONTAL);
		AnchorPane.setLeftAnchor(separator, 20.0);
		AnchorPane.setRightAnchor(separator, 20.0);
		AnchorPane.setTopAnchor(separator, 520.0);
		this.getChildren().add(separator);
		
		tf_epc = new TextField();
		tf_epc.setPrefWidth(500);
		tf_epc.setFont(Font.font("Arial", FontWeight.NORMAL, 24));
		AnchorPane.setLeftAnchor(tf_epc, 50.0);
		AnchorPane.setTopAnchor(tf_epc, 550.0);
		this.getChildren().add(tf_epc);
		
		btn_read = new Button("Read");
		btn_read.setOnAction(this);
		btn_read.setFont(Font.font("Arial", FontWeight.NORMAL, 24));
		AnchorPane.setLeftAnchor(btn_read, 570.0);
		AnchorPane.setTopAnchor(btn_read, 550.0);
		this.getChildren().add(btn_read);
	}

	@Override
	public void handle(ActionEvent event) {
		if (event.getSource() == rb_setting[EANUPC]) {
			for (int i=0; i<rb_schema.length; i++) {
				
				// Currently these schemas are not supported
				if (i==EPC_SCHEMA.CPI_var.getValue() || 
						i==EPC_SCHEMA.GIAI_202.getValue() || 
						i==EPC_SCHEMA.USDoD_96.getValue() || 
						i==EPC_SCHEMA.ADI_var.getValue()) {
					continue;
				}
				
				rb_schema[i].setDisable(!rb_setting[EANUPC].isSelected());
			}
		}
		
		if (event.getSource() == btn_read) {
			// Use a variable to check if any epc decode format is selected.
			boolean check = false;
			for (int i=0; i<setting.length; i++) {
				setting[i] = rb_setting[i].isSelected();
				check = check || rb_setting[i].isSelected();
			}
			
			// If non of the format is selected, show pop-up with warning message.
			if (check == false) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText(null);
				alert.setResizable(true);
				alert.setContentText("Please select a deocde format!");
				alert.showAndWait();
				return;
			}
			
			for (int i=0; i<rb_schema.length; i++) {
				setting_schema[i] = rb_schema[i].isSelected();
			}
			
			try {
				TagReadData trd = ReaderUtility.getInstance().readTag();
				if (trd == null) {
					MyLogger.printLog("No tag detected....");
					return;
				} else {
					MyLogger.printLog("Tag read: "+trd.epcString());
					String epc = trd.epcString();
					tf_epc.setText(this.parseEPC(epc));
				}
			} catch (ReaderException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static boolean isDecodeFormatSelected(int format) {
		if (format >= setting.length || format < 0) {
			MyLogger.printLog("Unknown decode format value.");
			return false;
		}
		
		return setting[format];
	}
	
	private String parseEPC(String hexString) {
		if (hexString == null) {
			return null;
		}
		
		String result = null;
		
		if (setting[UDC]) {
//			result = EPCDecoder.parseEPCString(hexString, ENCODE_TYPE.UDC, null);
		}
		
		if (setting[EANUPC] && (result == null)) {
			GS1Decoder decoder = new GS1Decoder(hexString);
			result = decoder.parseEPCString(setting_schema);
		}
		
		if (setting[EANUPC_EAS]) {
//			result = EPCDecoder.parseEPCString(hexString, ENCODE_TYPE.EAN_UPC_EAS, null);
		}
		
		if (setting[RAWDATA] && (result == null)) {
			result = hexString;
		}
		
		return result;
	}
	
	public void disableReadFunction(boolean disable) {
		btn_read.setDisable(disable);
		tf_epc.setDisable(disable);
	}
}
