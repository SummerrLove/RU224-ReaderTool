package scannel.ui;

import com.thingmagic.ReaderException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import scannel.reader.MyLogger;
import scannel.reader.ReaderUtility;

public class HopTableFrame extends AnchorPane implements EventHandler<ActionEvent> {

	private Button btn_apply;
	private TextArea hoptable;
	
	public static int[] FrequencyList;
	
	
	public HopTableFrame() {
		initComponents();
	}

	public HopTableFrame(Node... children) {
		super(children);
		// TODO Auto-generated constructor stub
	}

	private void initComponents() {
		
		hoptable = new TextArea();
		hoptable.setWrapText(true);
		hoptable.setPromptText("Frequency value in kHz");
		AnchorPane.setLeftAnchor(hoptable, 50.0);
		AnchorPane.setTopAnchor(hoptable, 100.0);
		AnchorPane.setRightAnchor(hoptable, 50.0);
		AnchorPane.setBottomAnchor(hoptable, 400.0);
		this.getChildren().add(hoptable);
		
		btn_apply = new Button("Apply");
		btn_apply.setOnAction(this);
		btn_apply.setFont(Font.font("Arial", FontWeight.NORMAL, 20));
		AnchorPane.setLeftAnchor(btn_apply, 30.0);
		AnchorPane.setTopAnchor(btn_apply, 500.0);
		this.getChildren().add(btn_apply);
	}

	@Override
	public void handle(ActionEvent event) {
		if (event.getSource() == btn_apply) {
			FrequencyList = this.parseHopTable(hoptable.getText());
			try {
				ReaderUtility.getInstance().setHopTable(FrequencyList);
			} catch (ReaderException e) {
				e.printStackTrace();
			}
		}
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
	
	public void setHopTableText(int[] frequencyList) {
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
		
	public void enableInput(boolean enable) {
		hoptable.setDisable(!enable);
		btn_apply.setDisable(!enable);
	}
}
