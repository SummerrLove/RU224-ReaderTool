package scannel.ui;

import java.util.ArrayList;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import scannel.reader.MyLogger;

public class AntennaFrame extends GridPane {
	
	private CheckBox[] antenna;
	private TextField[] ant_power;
	private boolean activate_power_list;
	

	public AntennaFrame() {
		this.initComponents();
	}

	private void initComponents() {
		Label ant_title = new Label("Antenna");
		ant_title.setFont(Font.font("Arial", FontWeight.BOLD, 16));
		this.add(ant_title, 0, 0, 2, 1);
		
		antenna = new CheckBox[4];
		ant_power = new TextField[4];
		
		for (int i=0; i<4; i++) {
			antenna[i] = new CheckBox("Antenna "+(i+1));
			ant_power[i] = new TextField();
			ant_power[i].setPromptText("5 - 31.5");
			ant_power[i].setPrefWidth(60);
			this.add(antenna[i], 0, i+1);
			this.add(ant_power[i], 1, i+1);
		}
//		antenna[0].setSelected(true);
		
		
	}
	
	public int[] getAntennaList() {
		ArrayList<String> list = new ArrayList<String>();
		
		for (int i=0; i<antenna.length; i++) {
			if (antenna[i].isSelected()) {
				list.add(new String(""+(i+1)));
			}
		}
		
		String myAntenna = "";
		int[] ant_list = new int[list.size()];
		for (int j=0;j<list.size();j++) {
			ant_list[j] = Integer.parseInt(list.get(j));
			myAntenna += (ant_list[j]+" ");
		}
		MyLogger.printLog("Antenna List: " + myAntenna);
		
		return ant_list;
	}
	
	public void setAntennaList(int[] ant_list) {
		for (int i=0; i<antenna.length; i++) {
			antenna[i].setSelected(false);
		}
		
		for (int j=0; j<ant_list.length; j++) {
			antenna[ant_list[j]-1].setSelected(true);
		}
	}
	
	public void enablePowerList(boolean bool) {
		activate_power_list = bool;
		for (int i=0; i<ant_power.length; i++) {
			ant_power[i].setDisable(!activate_power_list);
		}
	}
	
	public float[] getPowerList(){
		
		if (activate_power_list) {
			float[] powerList = new float[4];
			
			for (int i=0; i<powerList.length; i++) {
				if (antenna[i].isSelected()) {
					powerList[i] = Float.parseFloat(ant_power[i].getText());
				} else {
					powerList[i] = -1;
				}
			}
			
			return powerList;
			
		} else {
			return null;
		}
	}
	
	public void setPowerList(String[] list) {
		if (list != null) {
			for (int i=0; i<list.length; i++) {
				if (Float.parseFloat(list[i]) > 0) {
					ant_power[i].setText(list[i]);
				} 
			}
		}
	}
	
}
