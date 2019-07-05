package scannel.ui;

import java.util.ArrayList;

import org.llrp.ltk.generated.parameters.AntennaProperties;

import javafx.geometry.Insets;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import scannel.reader.MyLogger;

public class AntennaFrame extends GridPane {
	
	private CheckBox[] antenna;

	public AntennaFrame() {
		this.initComponents();
	}

	private void initComponents() {
		Label ant_title = new Label("Antenna");
		ant_title.setFont(Font.font("Arial", FontWeight.BOLD, 12));
		this.add(ant_title, 0, 0, 2, 1);
		
		antenna = new CheckBox[4];
		for (int i=0; i<4; i++) {
			antenna[i] = new CheckBox("Antenna "+(i+1));
			antenna[i].setFont(Font.font("Arial", FontWeight.NORMAL, 10));
			int index_x = i%2;
			int index_y = 1 + i/2;
			this.add(antenna[i], index_x, index_y);
		}
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
}
