package scannel.ui;

import javax.xml.bind.DatatypeConverter;

import com.thingmagic.ReaderException;
import com.thingmagic.TagReadData;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import scannel.decoder.Spec2000Decoder;
import scannel.reader.MyLogger;
import scannel.reader.ReaderUtility;
import scannel.reader.StringTool;

public class LifevestDemo extends AnchorPane implements EventHandler<ActionEvent> {

	private TextField tf_epc;
	private Button btn_read;
	private TextArea ta_birthRecord;
	
	
	public LifevestDemo() {
		this.initComponents();
	}

	public LifevestDemo(Node... arg0) {
		super(arg0);
		
	}

	private void initComponents() {
		Label title_epc = new Label("EPC");
		title_epc.setFont(Font.font("Arial", FontWeight.BOLD, 16));
		AnchorPane.setLeftAnchor(title_epc, 50.0);
		AnchorPane.setTopAnchor(title_epc, 70.0);
		this.getChildren().add(title_epc);
		
		tf_epc = new TextField();
		tf_epc.setPrefWidth(300);
		AnchorPane.setLeftAnchor(tf_epc, 50.0);
		AnchorPane.setTopAnchor(tf_epc, 110.0);
		this.getChildren().add(tf_epc);
		
		btn_read = new Button("Read");
		btn_read.setOnAction(this);
		AnchorPane.setLeftAnchor(btn_read, 370.0);
		AnchorPane.setTopAnchor(btn_read, 110.0);
		this.getChildren().add(btn_read);
		
		Label title_birthRecord = new Label("Birth Record");
		title_birthRecord.setFont(Font.font("Arial", FontWeight.BOLD, 16));
		AnchorPane.setLeftAnchor(title_birthRecord, 50.0);
		AnchorPane.setTopAnchor(title_birthRecord, 180.0);
		this.getChildren().add(title_birthRecord);
		
		ta_birthRecord = new TextArea();
		ta_birthRecord.setWrapText(true);
		ta_birthRecord.setPrefSize(300, 200);
		AnchorPane.setLeftAnchor(ta_birthRecord, 50.0);
		AnchorPane.setTopAnchor(ta_birthRecord, 220.0);
		this.getChildren().add(ta_birthRecord);
		
		
	}

	@Override
	public void handle(ActionEvent event) {
		if (event.getSource() == btn_read) {
			try {
				TagReadData trd = ReaderUtility.getInstance().readTag();
				if (trd == null) {
					MyLogger.printLog("No tag detected....");
					return;
				} else {
					MyLogger.printLog("Tag read: "+trd.epcString());
					tf_epc.setText(trd.epcString());
					MyLogger.printLog("EPC memory: "+trd.getEPCMemData());
					MyLogger.printLog("User memory: "+trd.getUserMemData());
					MyLogger.printLog("length: "+trd.getUserMemData().length);
					
					MyLogger.printLog(DatatypeConverter.printHexBinary(trd.getUserMemData()));
//					String temp = DatatypeConverter.printHexBinary(trd.getUserMemData());
//					MyLogger.printLog(temp);
//					Spec2000Decoder.parseBirthRecord(temp);
				}
			} catch (ReaderException e) {
				e.printStackTrace();
			}
		}
	}
}
