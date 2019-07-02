package scannel.ui;

import com.pi4j.io.gpio.PinState;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import scannel.reader.MyLogger;

public class DISettingFrame extends AnchorPane {

	private Label title;
	private DIRadioButtonSet[] setting;
	private TextField[] delay;
	private final int SIZE = 4; 
	
	
	public DISettingFrame(String title_str) {
		initComponents(title_str);
	}

	public DISettingFrame(Node... children) {
		super(children);
	}

	
	private void initComponents(String title_str) {
		title = new Label(title_str);
		title.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		AnchorPane.setLeftAnchor(title, 20.0);
		AnchorPane.setTopAnchor(title, 20.0);
		this.getChildren().add(title);
		
		setting = new DIRadioButtonSet[SIZE];
		delay = new TextField[SIZE];
		for (int i=0; i<setting.length; i++) {
			Label label = new Label("DI #"+(i+1));
			label.setFont(Font.font("Arial", FontWeight.BOLD, 16));
			AnchorPane.setLeftAnchor(label, 20.0);
			AnchorPane.setTopAnchor(label, 60.0 + i*30);
			this.getChildren().add(label);
			
			setting[i] = new DIRadioButtonSet();
			AnchorPane.setLeftAnchor(setting[i], 80.0);
			AnchorPane.setTopAnchor(setting[i], 60.0 + i*30);
			this.getChildren().add(setting[i]);
			
			delay[i] = new TextField();
			delay[i].setPromptText("in ms");
			delay[i].setPrefWidth(60);
			AnchorPane.setLeftAnchor(delay[i], 320.0);
			AnchorPane.setTopAnchor(delay[i], 60.0 + i*30);
			this.getChildren().add(delay[i]);
			
			Label label2 = new Label("ms");
			label2.setFont(Font.font("Arial", FontWeight.BOLD, 16));
			AnchorPane.setLeftAnchor(label2, 390.0);
			AnchorPane.setTopAnchor(label2, 60.0 + i*30);
			this.getChildren().add(label2);
		}
	}
	
	public PinState[] getSetting() {
		PinState[] di_setting = new PinState[SIZE];
		for (int i=0; i<di_setting.length; i++) {
			di_setting[i] = setting[i].getSetting();
		}
		
		return di_setting;
	}
	
	public void setSetting(PinState[] states) {
		for (int i=0; i<SIZE; i++) {
			setting[i].setSetting(states[i]);
		}
	}
	
	public int[] getDelaySetting() {
		int[] intArray = new int[SIZE];
		
		for (int i=0; i<delay.length; i++) {
			String temp = delay[i].getText();
			if (temp!=null && temp.length()>0) {
				try {
					intArray[i] = Integer.parseInt(temp);
				} catch (NumberFormatException e) {
					e.printStackTrace();
					intArray[i] = 0;
				}
			}
		}
		
		return intArray;
	}
	
	public void setDelaySetting(int[] intArray) {
		if (intArray.length != SIZE) {
			MyLogger.printLog("Setting DI delay error...");
			return;
		}
		
		for (int i=0; i<SIZE; i++) {
			if (intArray[i] > 0) {
				delay[i].setText(Integer.toString(intArray[i]));
			} else {
				delay[i].setText("0");
			}
		}
	}
}
