package scannel.ui;

import java.text.SimpleDateFormat;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import scannel.reader.DataUpdateListener;
import scannel.reader.ReaderConfig;
import scannel.reader.ReaderUtility;
import scannel.reader.StringTool;
import scannel.reader.TagList;
import scannel.reader.TagTableData;
import scannel.reader.TagUnit;

public class DataFrame extends AnchorPane implements DataUpdateListener, EventHandler<ActionEvent>{

	private Label tag_num;
	private RFIDTable table;
	private Label icon;
	private ObservableList<TagTableData> dataList = FXCollections.observableArrayList();
	private SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	private Label total_inventory_time;
	
	
	public DataFrame() {
		this.initComponents();
		ReaderUtility.getInstance().setDataUpdateListener(this);
	}

	public DataFrame(Node... children) {
		super(children);
	}

	private void initComponents() {
		
		tag_num = new Label(String.format("%03d", 0));
		tag_num.setFont(new Font("Arial", 200));
		tag_num.setStyle("-fx-border-color: black; -fx-background-color: lightgray;");
		tag_num.setAlignment(Pos.CENTER);
		tag_num.setPrefSize(480, 240);
		AnchorPane.setLeftAnchor(tag_num, 20.0);
		AnchorPane.setRightAnchor(tag_num, 10.0);
		AnchorPane.setTopAnchor(tag_num, 70.0);
		this.getChildren().add(tag_num);
		
		table = new RFIDTable();
		AnchorPane.setLeftAnchor(table, 10.0);
		AnchorPane.setTopAnchor(table, 320.0);
		AnchorPane.setBottomAnchor(table, 10.0);
		AnchorPane.setRightAnchor(table, 0.0);
		this.getChildren().add(table);
		
		icon = new Label();
		Image img = new Image(getClass().getResourceAsStream("/resource/scannel.jpg"));
		icon.setGraphic(new ImageView(img));
		AnchorPane.setRightAnchor(icon, 20.0);
		AnchorPane.setTopAnchor(icon, 10.0);
		this.getChildren().add(icon);
		
		total_inventory_time = new Label();
		total_inventory_time.setFont(Font.font("Arial", FontWeight.BOLD, 16));
		total_inventory_time.setAlignment(Pos.CENTER);
		AnchorPane.setLeftAnchor(total_inventory_time, 40.0);
		AnchorPane.setTopAnchor(total_inventory_time, 260.0);
		this.getChildren().add(total_inventory_time);
	}
	
	private void setDisplayNumber(int num) {
		if (Integer.parseInt(tag_num.getText()) != num){
			tag_num.setText(String.format("%03d", num));
		}
	}
	
	private void clearTableData() {
		table.setTableData(FXCollections.observableArrayList());
	}

	@Override
	public void dataUpdate() {
		
		dataList.clear();
		TagList tagList = ReaderUtility.getInstance().getTagData();
		
		if (tagList != null) {
			setDisplayNumber(tagList.size());
			
			
			// TODO 
			//=============================================
			// The following code is just for temporary use. It's better to combine the tagList of ReaderUtility with
			// the observablelist used for table 
			for (int i=0; i<tagList.size(); i++) {
				TagUnit tu = tagList.get(i);
				TagTableData ttd = new TagTableData();
				ttd.setEpc(tu.getEPC());
				ttd.setAscii(StringTool.hexToAsciiString(tu.getEPC()));
				ttd.setReadCount(tu.getReadCount());
				ttd.setTime(formatter.format(tu.getTime()));
				ttd.setReadFrequency(tu.getReadFrequency());
				ttd.setAntennaId(Integer.toString(tu.getAntennaId()));
				dataList.add(ttd);
			}
			//==============================================
		} else {
			setDisplayNumber(0);
		}
		
		if (!ReaderConfig.getInstance().getDOTrigger()) {
			total_inventory_time.setText(Float.toString(ReaderUtility.getInstance().getTotalInventoryTime()) + " sec");
		} else {
			total_inventory_time.setText("");
		}
		
		table.setTableData(dataList);
		
	}

	@Override
	public void handle(ActionEvent event) {
		
	}
	
	public void resetDisplayData() {
		setDisplayNumber(0);
		clearTableData();
		total_inventory_time.setText("");
	}
	
}
