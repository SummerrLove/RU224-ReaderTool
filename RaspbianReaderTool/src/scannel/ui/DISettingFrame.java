package scannel.ui;

import com.pi4j.io.gpio.PinState;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class DISettingFrame extends AnchorPane {

	private Label title;
	private DIRadioButtonSet[] setting;
	
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
		
		setting = new DIRadioButtonSet[4];
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
		}
	}
	
	public PinState[] getSetting() {
		PinState[] di_setting = new PinState[setting.length];
		for (int i=0; i<di_setting.length; i++) {
			di_setting[i] = setting[i].getSetting();
		}
		
		return di_setting;
	}
	
	public void setSetting(PinState[] states) {
		for (int i=0; i<setting.length; i++) {
			setting[i].setSetting(states[i]);
		}
	}
}
