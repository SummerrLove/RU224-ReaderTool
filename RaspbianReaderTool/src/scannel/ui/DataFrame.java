package scannel.ui;

import java.text.SimpleDateFormat;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import scannel.reader.DataUpdateListener;
import scannel.reader.MyLogger;
import scannel.reader.ReaderConfig;
import scannel.reader.ReaderUtility;
import scannel.reader.StringTool;
import scannel.reader.TagList;
import scannel.reader.TagTableData;
import scannel.reader.TagUnit;

public class DataFrame extends AnchorPane implements DataUpdateListener, EventHandler<ActionEvent>{

//	private Label tag_num;
	private RFIDTable table;
//	private Button btn_reset;
//	private Button btn_save;
	private Label icon;
	private ObservableList<TagTableData> dataList = FXCollections.observableArrayList();
	private SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//	private Label total_inventory_time;
	
	private ChoiceBox<String> cb_antenna;
	
	
	public DataFrame() {
		this.initComponents();
		ReaderUtility.getInstance().setDataUpdateListener(this);
	}

	public DataFrame(Node... children) {
		super(children);
	}

	private void initComponents() {
		
//		btn_reset = new Button("Reset");
//		btn_reset.setOnAction(this);
//		btn_reset.setFont(new Font("Arial", 14));
//		AnchorPane.setLeftAnchor(btn_reset, 20.0);
//		AnchorPane.setTopAnchor(btn_reset, 30.0);
//		this.getChildren().add(btn_reset);
		
//		btn_save = new Button("Save");
//		btn_save.setOnAction(this);
//		btn_save.setFont(new Font("Arial", 14));
//		AnchorPane.setLeftAnchor(btn_save, 70.0);
//		AnchorPane.setTopAnchor(btn_save, 30.0);
//		this.getChildren().add(btn_save);
		
		Label antID = new Label("Antenna ID : ");
		antID.setFont(new Font("Arial", 24));
		AnchorPane.setLeftAnchor(antID, 20.0);
		AnchorPane.setTopAnchor(antID, 20.0);
		this.getChildren().add(antID);
		
		cb_antenna = new ChoiceBox<String>(FXCollections.observableArrayList("Antenna 1", "Antenna 2", "Antenna 3", "Antenna 4"));
		cb_antenna.setValue("Antenna 1");
		cb_antenna.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue ov, Number oldValue, Number newValue) {
				System.out.println("old="+oldValue+", new="+newValue);
				System.out.println(ov.toString());
				if (!ReaderUtility.getInstance().isReading()) {
					dataUpdate();
				} else {
					MyLogger.printLog("No data update while inventorying items...");
				}
			}
			
		});
		AnchorPane.setLeftAnchor(cb_antenna, 170.0);
		AnchorPane.setTopAnchor(cb_antenna, 22.0);
		this.getChildren().add(cb_antenna);
		
//		tag_num = new Label(String.format("%03d", 0));
//		tag_num.setFont(new Font("Arial", 300));
//		tag_num.setStyle("-fx-border-color: black; -fx-background-color: lightgray;");
//		tag_num.setAlignment(Pos.CENTER);
//		tag_num.setPrefSize(480, 400);
//		AnchorPane.setLeftAnchor(tag_num, 20.0);
//		AnchorPane.setRightAnchor(tag_num, 10.0);
//		AnchorPane.setTopAnchor(tag_num, 70.0);
//		this.getChildren().add(tag_num);
		
		table = new RFIDTable();
		AnchorPane.setLeftAnchor(table, 10.0);
		AnchorPane.setTopAnchor(table, 100.0);
		AnchorPane.setBottomAnchor(table, 10.0);
		AnchorPane.setRightAnchor(table, 0.0);
		this.getChildren().add(table);
		
		icon = new Label();
		Image img = new Image(getClass().getResourceAsStream("/resource/scannel.jpg"));
		icon.setGraphic(new ImageView(img));
		AnchorPane.setRightAnchor(icon, 20.0);
		AnchorPane.setTopAnchor(icon, 10.0);
		this.getChildren().add(icon);
		
//		total_inventory_time = new Label();
//		total_inventory_time.setFont(Font.font("Arial", FontWeight.BOLD, 20));
//		total_inventory_time.setAlignment(Pos.CENTER);
//		AnchorPane.setLeftAnchor(total_inventory_time, 50.0);
//		AnchorPane.setTopAnchor(total_inventory_time, 440.0);
//		this.getChildren().add(total_inventory_time);
	}
	
//	private void setDisplayNumber(int num) {
//		if (Integer.parseInt(tag_num.getText()) != num){
//			tag_num.setText(String.format("%03d", num));
//		}
//	}
	
	private void clearTableData() {
		table.setTableData(FXCollections.observableArrayList());
	}

	@Override
	public void dataUpdate() {
		
		int antenna_id = cb_antenna.getSelectionModel().getSelectedIndex() + 1;
		
		dataList.clear();
		TagList tagList = ReaderUtility.getInstance().getTagData();
		MyLogger.printLog("Total number of tag read: "+tagList.size());
		
		if (tagList != null) {
//			setDisplayNumber(tagList.size());
			
			
			// TODO 
			//=============================================
			// The following code is just for temporary use. It's better to combine the tagList of ReaderUtility with
			// the observablelist used for table 
			for (int i=0; i<tagList.size(); i++) {
				TagUnit tu = tagList.get(i);
				if (tu.getAntennaId() == antenna_id) {
					TagTableData ttd = new TagTableData();
					ttd.setEpc(tu.getEPC());
					ttd.setAscii(StringTool.hexToAsciiString(tu.getEPC()));
					ttd.setReadCount(tu.getReadCount());
					ttd.setTime(formatter.format(tu.getTime()));
					ttd.setReadFrequency(tu.getReadFrequency());
					ttd.setAntennaId(Integer.toString(tu.getAntennaId()));
					dataList.add(ttd);
				}
			}
			//==============================================
		} else {
//			setDisplayNumber(0);
		}
		
//		if (!ReaderConfig.getInstance().getDOTrigger()) {
//			total_inventory_time.setText(Float.toString(ReaderUtility.getInstance().getTotalInventoryTime()) + " sec");
//		} else {
//			total_inventory_time.setText("");
//		}
		MyLogger.printLog("Tag read by antenna #"+antenna_id+": "+dataList.size());
		table.setTableData(dataList);
		
	}

	@Override
	public void handle(ActionEvent event) {
//		if (event.getSource() == btn_reset) {
//			this.resetDisplayData();
//			ReaderUtility.getInstance().resetData();
//		} else if (event.getSource() == btn_save){
//			
//		}
	}
	
	public void resetDisplayData() {
//		setDisplayNumber(0);
		clearTableData();
//		total_inventory_time.setText("");
	}
	
//	public void hideTotalInventoryTime(boolean hide) {
//		if (hide) {
//			this.getChildren().remove(total_inventory_time);
//		} else {
//			this.getChildren().add(total_inventory_time);
//		}
//	}
}
