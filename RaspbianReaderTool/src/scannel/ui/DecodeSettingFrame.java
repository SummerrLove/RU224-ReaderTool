package scannel.ui;

import com.thingmagic.ReaderException;
import com.thingmagic.TagReadData;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import scannel.reader.EPCDecoder;
import scannel.reader.MyLogger;
import scannel.reader.ReaderUtility;

public class DecodeSettingFrame extends AnchorPane implements EventHandler<ActionEvent> {
	
	private static boolean[] setting = new boolean[4];
	private RadioButton[] rb_setting = new RadioButton[4]; 
	private static boolean[] setting_schema = new boolean[17];
	private RadioButton[] rb_schema = new RadioButton[17];
//	private Button btn_apply;
	private Button btn_read;
	private TextField tf_epc;
	
	private static String[] str_setting = {"UDC", "EAN / UPC", "EAN / UPC + EAS", "EPC Raw Data"};
	private static String[] str_schema = {"SGTIN", "SSCC", "SGLN", "GRAI", 
			"GIAI", "GSRN", "GSRNP", "GDTI", "CPI", "SGCN", "GINC", "GSIN", "ITIP", "GID", "USDOD", "ADI", "BIC"};
	
	
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
	
	public static enum EAN_TYPE{
		GTIN_14,
		GTIN_13,
		GTIN_12,
		GTIN_8;
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
		
		
		int x, y;
		for (int i=0; i<rb_schema.length; i++) {
			rb_schema[i] = new RadioButton(str_schema[i]);
			rb_schema[i].setFont(Font.font("Arial", FontWeight.BOLD, 14));
			
			x = i%2;
			y = i/2;
			AnchorPane.setLeftAnchor(rb_schema[i], 500+x*200.0);
			AnchorPane.setTopAnchor(rb_schema[i], 150+y*30.0);
			this.getChildren().add(rb_schema[i]);
			rb_schema[i].setDisable(true);
		}
		
//		cb_schema = new ChoiceBox<String>(FXCollections.observableArrayList("sgtin", "sscc", "sgln", "grai", 
//				"giai", "gsrn", "gsrnp", "gdti", "cpi", "sgcn", "ginc", "gsin", "itip", "gid", "usdod", "adi", "bic"));
//		AnchorPane.setLeftAnchor(cb_schema, 650.0);
//		AnchorPane.setTopAnchor(cb_schema, 120.0);
//		cb_schema.setDisable(true);
//		this.getChildren().add(cb_schema);
		
		Separator separator = new Separator();
		separator.setOrientation(Orientation.HORIZONTAL);
		AnchorPane.setLeftAnchor(separator, 20.0);
		AnchorPane.setRightAnchor(separator, 20.0);
		AnchorPane.setTopAnchor(separator, 520.0);
		this.getChildren().add(separator);
		
//		btn_apply = new Button("Apply Setting");
//		btn_apply.setOnAction(this);
//		AnchorPane.setLeftAnchor(btn_apply, 50.0);
//		AnchorPane.setTopAnchor(btn_apply, 600.0);
//		this.getChildren().add(btn_apply);
		
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
				rb_schema[i].setDisable(!rb_setting[EANUPC].isSelected());
			}
//			cb_schema.setDisable(!rb_setting[EANUPC].isSelected());
		}
		
		if (event.getSource() == btn_read) {
			for (int i=0; i<setting.length; i++) {
				setting[i] = rb_setting[i].isSelected();
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
		String result = null;
		
		if (setting[UDC]) {
			result = EPCDecoder.parseEPCString(hexString, ENCODE_TYPE.UDC, null);
		}
		
		if (setting[EANUPC] && (result == null)) {
			result = EPCDecoder.parseEPCString(hexString, ENCODE_TYPE.EAN_UPC, setting_schema);
		}
		
		if (setting[EANUPC_EAS]) {
			result = EPCDecoder.parseEPCString(hexString, ENCODE_TYPE.EAN_UPC_EAS, null);
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
