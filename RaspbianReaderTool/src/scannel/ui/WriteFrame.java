package scannel.ui;

import javax.xml.bind.DatatypeConverter;

import com.thingmagic.Gen2;
import com.thingmagic.ReaderException;
import com.thingmagic.TagFilter;
import com.thingmagic.TagReadData;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import scannel.reader.MyLogger;
import scannel.reader.ReaderUtility;
import scannel.reader.StringTool;
import scannel.reader.TagUnit;

public class WriteFrame extends AnchorPane implements EventHandler<ActionEvent> {

	private TextField epc;
	private TextField tid;
	private TextArea userBank;
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
		
		btn_read = new Button("Read");
		btn_read.setFont(Font.font("Arial", FontWeight.NORMAL, 20));
		btn_read.setOnAction(this);
		AnchorPane.setLeftAnchor(btn_read, 50.0);
		AnchorPane.setTopAnchor(btn_read, 40.0);
		this.getChildren().add(btn_read);
		
		Label title_epc = new Label("EPC");
		title_epc.setFont(Font.font("Arial", FontWeight.BOLD, 16));
		AnchorPane.setLeftAnchor(title_epc, 50.0);
		AnchorPane.setTopAnchor(title_epc, 120.0);
		this.getChildren().add(title_epc);
		
		epc = new TextField();
		epc.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
		epc.setPrefWidth(400);
		AnchorPane.setLeftAnchor(epc, 50.0);
		AnchorPane.setTopAnchor(epc, 150.0);
		this.getChildren().add(epc);
		
		Label title_tid = new Label("TID");
		title_tid.setFont(Font.font("Arial", FontWeight.BOLD, 16));
		AnchorPane.setLeftAnchor(title_tid, 50.0);
		AnchorPane.setTopAnchor(title_tid, 220.0);
		this.getChildren().add(title_tid);
		
		tid = new TextField();
		tid.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
		tid.setPrefWidth(400);
		AnchorPane.setLeftAnchor(tid, 50.0);
		AnchorPane.setTopAnchor(tid, 250.0);
		this.getChildren().add(tid);
		
		Label title_user = new Label("User Memory");
		title_user.setFont(Font.font("Arial", FontWeight.BOLD, 16));
		AnchorPane.setLeftAnchor(title_user, 50.0);
		AnchorPane.setTopAnchor(title_user, 320.0);
		this.getChildren().add(title_user);
		
		userBank = new TextArea();
		userBank.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
		userBank.setWrapText(true);
		userBank.setPrefSize(400, 150);
		AnchorPane.setLeftAnchor(userBank, 50.0);
		AnchorPane.setTopAnchor(userBank, 350.0);
		this.getChildren().add(userBank);
		
		
		Separator separator = new Separator();
		separator.setOrientation(Orientation.HORIZONTAL);
		AnchorPane.setLeftAnchor(separator, 20.0);
		AnchorPane.setRightAnchor(separator, 20.0);
		AnchorPane.setTopAnchor(separator, 550.0);
		this.getChildren().add(separator);
		
		
		Label title_new = new Label("New EPC");
		title_new.setFont(Font.font("Arial", FontWeight.BOLD, 16));
		AnchorPane.setLeftAnchor(title_new, 50.0);
		AnchorPane.setTopAnchor(title_new, 600.0);
		this.getChildren().add(title_new);

		new_epc = new TextField();
		new_epc.setPrefWidth(300);
		AnchorPane.setLeftAnchor(new_epc, 50.0);
		AnchorPane.setTopAnchor(new_epc, 630.0);
		this.getChildren().add(new_epc);
		
		btn_write = new Button("Write");
		btn_write.setOnAction(this);
		AnchorPane.setLeftAnchor(btn_write, 370.0);
		AnchorPane.setTopAnchor(btn_write, 630.0);
		this.getChildren().add(btn_write);
		
		Label notice = new Label("Because of the data unit requirement for writing RFID tags, '0' will be added to the end of the hex string in some cases.");
		notice.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
		AnchorPane.setLeftAnchor(notice, 50.0);
		AnchorPane.setTopAnchor(notice, 680.0);
		this.getChildren().add(notice);
	}

	@Override
	public void handle(ActionEvent event) {
		if (event.getSource() == btn_read) {
			// read tag and show epc
			try {
				TagUnit tu = ReaderUtility.getInstance().readTag();
				if (tu == null) {
					MyLogger.printLog("No tag detected....");
					epc.setText("No tag detected...");
					tid.setText("");
					userBank.setText("");
					return;
				} else {
					MyLogger.printLog("Tag read: "+tu.getEPC());
					epc.setText(tu.getEPC());
					tid.setText(tu.getTid());
					userBank.setText(tu.getUserBank());
					
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
			
			String epcStr = epc.getText();
			if (epcStr!=null && epcStr.length()>0) {
				Gen2.Select select = new Gen2.Select(false, Gen2.Bank.EPC, 32, epcStr.length()*4, DatatypeConverter.parseHexBinary(epcStr));
				writeHexEPC(new_epc.getText(), select);
			} else {
				writeHexEPC(new_epc.getText(), null);
			}
			
		}
		
	}

	
	private void writeHexEPC(String hexString, TagFilter target) {
		if (StringTool.isHexString(hexString)) {
			try {
				ReaderUtility.getInstance().writeEPC(hexString, target);
			} catch (ReaderException e) {
				e.printStackTrace();
				
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText(null);
				alert.setContentText(e.getMessage());
				alert.showAndWait();
			}
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText(null);
			alert.setResizable(true);
			alert.setContentText("Not Hex String!");
			alert.showAndWait();
		}
	}
	
	private void writeAsciiEPC(String str, TagFilter target) {
		if (str.length()%2 != 0) {
			str = str+"0";
		}
		
		try {
			ReaderUtility.getInstance().writeAsciiEPC(str, target);
		} catch (ReaderException e) {
			e.printStackTrace();
			
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText(null);
			alert.setContentText(e.getMessage());
			alert.showAndWait();
		}
	}
	
	private String formatString(String str) {
		int counter = 0;
		String output = "";
		
		while (counter < str.length()) {
			if ((counter+2) <= str.length()) {
				output += str.substring(counter, counter+2)+" ";
			} else {
				output += str.substring(counter, str.length());
			}
			counter += 2;
		}
		
		return output;
	}
}
