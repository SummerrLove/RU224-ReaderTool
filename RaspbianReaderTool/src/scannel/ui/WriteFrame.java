package scannel.ui;

import com.thingmagic.Gen2;
import com.thingmagic.ReaderException;
import com.thingmagic.TagReadData;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import scannel.reader.MyLogger;
import scannel.reader.ReaderUtility;
import scannel.reader.StringTool;

public class WriteFrame extends AnchorPane implements EventHandler<ActionEvent> {

	private TextField current_epc;
	private Button btn_read;
	
	private TextField new_epc;
	private Button btn_write;
	
	public WriteFrame() {
		this.initComponents();
	}

	public WriteFrame(Node... arg0) {
		super(arg0);
	}
	
	private void initComponents() {
		
		Label title_current = new Label("Current EPC in ASCII code");
		title_current.setFont(Font.font("Arial", FontWeight.BOLD, 16));
		AnchorPane.setLeftAnchor(title_current, 50.0);
		AnchorPane.setTopAnchor(title_current, 70.0);
		this.getChildren().add(title_current);
		
		current_epc = new TextField();
		current_epc.setPrefWidth(300);
		AnchorPane.setLeftAnchor(current_epc, 50.0);
		AnchorPane.setTopAnchor(current_epc, 110.0);
		this.getChildren().add(current_epc);
		
		btn_read = new Button("Read");
		btn_read.setOnAction(this);
		AnchorPane.setLeftAnchor(btn_read, 370.0);
		AnchorPane.setTopAnchor(btn_read, 110.0);
		this.getChildren().add(btn_read);
		
		Label title_new = new Label("New EPC in ASCII code");
		title_new.setFont(Font.font("Arial", FontWeight.BOLD, 16));
		AnchorPane.setLeftAnchor(title_new, 50.0);
		AnchorPane.setTopAnchor(title_new, 180.0);
		this.getChildren().add(title_new);

		new_epc = new TextField();
		new_epc.setPrefWidth(300);
		AnchorPane.setLeftAnchor(new_epc, 50.0);
		AnchorPane.setTopAnchor(new_epc, 220.0);
		this.getChildren().add(new_epc);
		
		btn_write = new Button("Write");
		btn_write.setOnAction(this);
		AnchorPane.setLeftAnchor(btn_write, 370.0);
		AnchorPane.setTopAnchor(btn_write, 220.0);
		this.getChildren().add(btn_write);
		
		Label notice = new Label("A character '0' will be added in front of the chracters to be written into EPC if the number of characters is not even.");
		notice.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
		AnchorPane.setLeftAnchor(notice, 50.0);
		AnchorPane.setTopAnchor(notice, 260.0);
		this.getChildren().add(notice);
	}

	@Override
	public void handle(ActionEvent event) {
		if (event.getSource() == btn_read) {
			// read tag and show epc
			try {
				TagReadData trd = ReaderUtility.getInstance().readTag();
				if (trd == null) {
					MyLogger.printLog("No tag detected....");
					return;
				} else {
					MyLogger.printLog("Tag read: "+trd.epcString());
					current_epc.setText(StringTool.hexToAsciiString(trd.epcString()));
				}
			} catch (ReaderException e) {
				e.printStackTrace();
			}
		} else if (event.getSource() == btn_write) {
			// write epc
//			TagFilter target = null;
//			if (current_epc.getText().length() > 0) {
//				String target_epc = current_epc.getText();
//				int bit_length = (target_epc.getBytes().length)*8;
//				target = new Gen2.Select(false, Gen2.Bank.EPC, 32, bit_length, target_epc.getBytes());
//			}
			
			String epc = new_epc.getText();
			if (epc.length()%2 != 0) {
				epc = "0"+epc;
			}
			
			Gen2.TagData write_data = new Gen2.TagData(epc.getBytes());
//			MyLogger.printLog("TagData string: "+new String(write_data.epcBytes()));
//			MyLogger.printLog("Length: "+write_data.epcBytes().length);
//			MyLogger.printLog("hex string: "+StringTool.asciiToHexString(epc));
			try {
				ReaderUtility.getInstance().writeTag(write_data, null);
			} catch (ReaderException e) {
				e.printStackTrace();
				
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText(null);
				alert.setContentText(e.getMessage());
				alert.showAndWait();
			}
		}
		
	}

	private boolean isHexString(String str) {
		return str.matches("[0-9a-fA-F]+");
	}
	
	private short[] toShortArray(String str) {
		if (str==null || !isHexString(str)) {
			MyLogger.printLog("Not hex string: "+str);
			return null;
		}
		
		if (str.length()%2 != 0) {
			str += "0";
		}
		
		short[] data = new short[str.length()/2];
		for (int i=0; (i+1)*2<str.length(); i++) {
			String temp = str.substring(i*2, (i+1)*2);
			data[i] = Short.parseShort(temp, 16);
		}
		
		return data;
	}
}
