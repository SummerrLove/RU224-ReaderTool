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
import scannel.reader.StringTool;

public class DecodeSettingFrame extends AnchorPane implements EventHandler<ActionEvent> {
	
	private static boolean[] setting = new boolean[4];
	private RadioButton[] rb_setting = new RadioButton[4]; 
	private ChoiceBox cb_eanType;
	private Button btn_apply;
	private Button btn_read;
	private TextField tf_epc;
	private static EAN_TYPE eanType;
	
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
		Rectangle rect = new Rectangle(40, 80, 920, 400);
		rect.setArcHeight(15);
		rect.setArcWidth(15);
//		rect.setFill(Color.TRANSPARENT);
		rect.setFill(Color.WHITE);
		rect.setStroke(Color.BLACK);
		this.getChildren().add(rect);
		
		Label title = new Label("EPC Decode Format");
		title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
		title.setStyle("-fx-background-color: #F0F0F0;");
		AnchorPane.setLeftAnchor(title, 50.0);
		AnchorPane.setTopAnchor(title, 30.0);
		this.getChildren().add(title);
		
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
//		if (event.getSource() == rb_sgtin) {
//			setting[SGTIN] = rb_sgtin.isSelected();
//		}
		
		if (event.getSource() == btn_apply) {
			for (int i=0; i<setting.length; i++) {
				setting[i] = rb_setting[i].isSelected();
			}
		}
		
		if (event.getSource() == btn_read) {
			try {
				TagReadData trd = ReaderUtility.getInstance().readTag();
				if (trd == null) {
					MyLogger.printLog("No tag detected....");
					return;
				} else {
					MyLogger.printLog("Tag read: "+trd.epcString());
					String epc = trd.epcString();
					if (setting[EANUPC]) {
						String decode_epc = EPCDecoder.parseEPCString(epc, ENCODE_TYPE.EAN_UPC);
					}
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
	
	public static EAN_TYPE getEANType() {
		return eanType;
	}
	
	private String parseEPC(String hexString) {
		String result = null;
		return result;
	}
}
