package scannel.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import scannel.reader.TagTableData;

public class RFIDTable extends AnchorPane {

	private TableView<TagTableData> table;
	private TableColumn<TagTableData, String> epcCol;
	private TableColumn<TagTableData, String> asciiCol;
	private TableColumn<TagTableData, Integer> countCol;
	private TableColumn<TagTableData, String> antIdCol;
	private TableColumn<TagTableData, String> timeCol;
	private TableColumn<TagTableData, Integer> frequencyCol;
	
	
	public RFIDTable() {
		
		initComponents();
		
	}
	
	private void initComponents() {
		
		epcCol = new TableColumn<TagTableData, String>("EPC");
		epcCol.setPrefWidth(150.0);
		epcCol.setCellValueFactory(new PropertyValueFactory("epc"));
		
		asciiCol = new TableColumn<TagTableData, String>("ASCII Code");
		asciiCol.setPrefWidth(100.0);
		asciiCol.setCellValueFactory(new PropertyValueFactory("ascii"));
		
		countCol = new TableColumn<TagTableData, Integer>("Read Count");
		countCol.setPrefWidth(50.0);
		countCol.setCellValueFactory(new PropertyValueFactory("readCount"));
		
		antIdCol = new TableColumn<TagTableData, String>("Antenna ID");
		antIdCol.setPrefWidth(50.0);
		antIdCol.setCellValueFactory(new PropertyValueFactory("antennaId"));
		
		timeCol = new TableColumn<TagTableData, String>("Time");
		timeCol.setPrefWidth(150.0);
		timeCol.setCellValueFactory(new PropertyValueFactory("time"));
		
		frequencyCol = new TableColumn<TagTableData, Integer>("Frequency");
		frequencyCol.setPrefWidth(100.0);
		frequencyCol.setCellValueFactory(new PropertyValueFactory("readFrequency"));

		table = new TableView<TagTableData>();
		table.getColumns().setAll(epcCol, asciiCol, countCol, antIdCol, timeCol, frequencyCol);
		AnchorPane.setLeftAnchor(table, 0.0);
		AnchorPane.setTopAnchor(table, 0.0);
		AnchorPane.setBottomAnchor(table, 0.0);
		AnchorPane.setRightAnchor(table, 0.0);
		this.getChildren().add(table);
	}
	
	private void createSampleData() {
		ObservableList<TagTableData> dataList = FXCollections.observableArrayList();
		
		for (int i=0; i<5; i++) {
			TagTableData data = new TagTableData();
			data.setEpc("aaa00"+i);
			data.setReadCount(i*13 + 2);
			data.setTime("2018/01/01");
			dataList.add(data);
		}
		
		table.setItems(dataList);
	}
	
	public void setTableData(ObservableList<TagTableData> dataList) {
		table.setItems(dataList);
	}
}
