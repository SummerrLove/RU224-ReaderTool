package scannel.ui;

import javafx.scene.control.CheckBox;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import scannel.reader.MyLogger;

public class IOFrame extends GridPane {

	private CheckBox[] digital_IO;
	private String item_name;
	
	public IOFrame(String name) {
		item_name = name;
		this.initComponents();
		
		this.setStyle("-fx-padding: 10;" + 
                "-fx-border-style: solid inside;" + 
                "-fx-border-width: 2;" +
                "-fx-border-insets: 5;" + 
                "-fx-border-radius: 5;" + 
                "-fx-border-color: lightgrey;");
	}

	private void initComponents() {
		digital_IO = new CheckBox[4];
		
		for (int i=0; i<4; i++) {
			digital_IO[i] = new CheckBox(item_name+" "+(i+1));
			digital_IO[i].setFont(Font.font("Arial", FontWeight.NORMAL, 18));
			int index_x = i%2;
			int index_y = 1 + i/2;
			this.add(digital_IO[i], index_x, index_y);
		}
	}
	
	public boolean[] getIOSettingList() {
		MyLogger.printLog(item_name+" List: ");

		boolean[] io_setting = new boolean[digital_IO.length];
		for (int i=0; i<digital_IO.length; i++) {
			io_setting[i] = digital_IO[i].isSelected();
			MyLogger.printLog(i+": "+io_setting[i]);
		}
		
		return io_setting;
	}
	
	public void setIOSettingList(boolean[] setting) {
		if (setting.length != digital_IO.length) {
			MyLogger.printLog("The length of IO setting doesn't match!");
			return;
		}
		
		for (int i=0; i<digital_IO.length; i++) {
			digital_IO[i].setSelected(setting[i]);
		}
		
	}
}
