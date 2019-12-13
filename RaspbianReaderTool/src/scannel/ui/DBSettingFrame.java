package scannel.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import scannel.reader.DatabaseUtility;
import scannel.reader.ReaderConfig;
import scannel.reader.ReaderConfig.DBType;

public class DBSettingFrame extends AnchorPane implements EventHandler<ActionEvent> {

	private ChoiceBox<DBType> db_type;
	private TextField ip;
	private TextField userid;
	private TextField password;
	private TextField db_name;
	
	private TextField readerid;
	private TextField table_name;
	private TextField fieldname_epc;
	private TextField fieldname_readerid;
	private TextField fieldname_time;
	private TextField fieldname_tid;
	private TextField fieldname_antenna;
	
	public DBSettingFrame() {
		this.initComponents();
		
		this.setStyle("-fx-padding: 10;" + 
                "-fx-border-style: solid inside;" + 
                "-fx-border-width: 2;" +
                "-fx-border-insets: 5;" + 
                "-fx-border-radius: 5;" + 
                "-fx-border-color: lightgrey;");
	}

	public DBSettingFrame(Node... arg0) {
		super(arg0);
	}

	private void initComponents() {
		
		Label db_title = new Label("DB Type: ");
		db_title.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
		AnchorPane.setLeftAnchor(db_title, 30.0);
		AnchorPane.setTopAnchor(db_title, 20.0);
		this.getChildren().add(db_title);
		
		db_type = new ChoiceBox<DBType>(loadDBOption());
		db_type.setOnAction(this);
		db_type.setValue(ReaderConfig.getInstance().getDBType());
		AnchorPane.setLeftAnchor(db_type, 150.0);
		AnchorPane.setTopAnchor(db_type, 20.0);
		this.getChildren().add(db_type);
		
		
		Label ip_title = new Label("IP & port:");
		ip_title.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
		AnchorPane.setLeftAnchor(ip_title, 30.0);
		AnchorPane.setTopAnchor(ip_title, 60.0);
		this.getChildren().add(ip_title);
		
		ip = new TextField();
		ip.setPrefWidth(200);
		ip.setPromptText("ex. \"192.168.21.135:4001\"");
		AnchorPane.setLeftAnchor(ip, 150.0);
		AnchorPane.setTopAnchor(ip, 60.0);
		this.getChildren().add(ip);
		
		
		Label dbname_title = new Label("DB Name:");
		dbname_title.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
		AnchorPane.setLeftAnchor(dbname_title, 30.0);
		AnchorPane.setTopAnchor(dbname_title, 100.0);
		this.getChildren().add(dbname_title);
		
		db_name = new TextField();
		db_name.setPrefWidth(200);
		AnchorPane.setLeftAnchor(db_name, 150.0);
		AnchorPane.setTopAnchor(db_name, 100.0);
		this.getChildren().add(db_name);
		
		
		Label userid_title = new Label("User ID:");
		userid_title.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
		AnchorPane.setLeftAnchor(userid_title, 30.0);
		AnchorPane.setTopAnchor(userid_title, 140.0);
		this.getChildren().add(userid_title);
		
		userid = new TextField();
		userid.setPrefWidth(200);
		AnchorPane.setLeftAnchor(userid, 150.0);
		AnchorPane.setTopAnchor(userid, 140.0);
		this.getChildren().add(userid);
		
		
		Label password_title = new Label("Password:");
		password_title.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
		AnchorPane.setLeftAnchor(password_title, 30.0);
		AnchorPane.setTopAnchor(password_title, 180.0);
		this.getChildren().add(password_title);
		
		password = new TextField();
		password.setPrefWidth(200);
		AnchorPane.setLeftAnchor(password, 150.0);
		AnchorPane.setTopAnchor(password, 180.0);
		this.getChildren().add(password);
		
		
		Separator separator = new Separator();
		separator.setOrientation(Orientation.VERTICAL);
		AnchorPane.setLeftAnchor(separator, 410.0);
		AnchorPane.setTopAnchor(separator, 10.0);
		AnchorPane.setBottomAnchor(separator, 10.0);
		this.getChildren().add(separator);
		
		
		Label readerid_title = new Label("Reader ID:");
		readerid_title.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
		AnchorPane.setLeftAnchor(readerid_title, 480.0);
		AnchorPane.setTopAnchor(readerid_title, 20.0);
		this.getChildren().add(readerid_title);
		
		readerid = new TextField();
		readerid.setPrefWidth(160);
		AnchorPane.setLeftAnchor(readerid, 680.0);
		AnchorPane.setTopAnchor(readerid, 20.0);
		this.getChildren().add(readerid);
		
		
		Label tablename_title = new Label("Table Name:");
		tablename_title.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
		AnchorPane.setLeftAnchor(tablename_title, 480.0);
		AnchorPane.setTopAnchor(tablename_title, 60.0);
		this.getChildren().add(tablename_title);
		
		table_name = new TextField();
		table_name.setPrefWidth(160);
		AnchorPane.setLeftAnchor(table_name, 680.0);
		AnchorPane.setTopAnchor(table_name, 60.0);
		this.getChildren().add(table_name);
		
		Label epcfield_title = new Label("Field Name (EPC):");
		epcfield_title.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
		AnchorPane.setLeftAnchor(epcfield_title, 480.0);
		AnchorPane.setTopAnchor(epcfield_title, 100.0);
		this.getChildren().add(epcfield_title);
		
		fieldname_epc = new TextField();
		fieldname_epc.setPrefWidth(160);
		AnchorPane.setLeftAnchor(fieldname_epc, 680.0);
		AnchorPane.setTopAnchor(fieldname_epc, 100.0);
		this.getChildren().add(fieldname_epc);
		
		
		Label fn_readerid_title = new Label("Field Name (Reader ID):");
		fn_readerid_title.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
		AnchorPane.setLeftAnchor(fn_readerid_title, 480.0);
		AnchorPane.setTopAnchor(fn_readerid_title, 140.0);
		this.getChildren().add(fn_readerid_title);
		
		fieldname_readerid = new TextField();
		fieldname_readerid.setPrefWidth(160);
		AnchorPane.setLeftAnchor(fieldname_readerid, 680.0);
		AnchorPane.setTopAnchor(fieldname_readerid, 140.0);
		this.getChildren().add(fieldname_readerid);
		
		
		Label time_title = new Label("Field Name (Time):");
		time_title.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
		AnchorPane.setLeftAnchor(time_title, 480.0);
		AnchorPane.setTopAnchor(time_title, 180.0);
		this.getChildren().add(time_title);
		
		fieldname_time = new TextField();
		fieldname_time.setPrefWidth(160);
		AnchorPane.setLeftAnchor(fieldname_time, 680.0);
		AnchorPane.setTopAnchor(fieldname_time, 180.0);
		this.getChildren().add(fieldname_time);
		
		
		Label tid_title = new Label("Field Name (TID):");
		tid_title.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
		AnchorPane.setLeftAnchor(tid_title, 480.0);
		AnchorPane.setTopAnchor(tid_title, 220.0);
		this.getChildren().add(tid_title);
		
		fieldname_tid = new TextField();
		fieldname_tid.setPrefWidth(160);
		AnchorPane.setLeftAnchor(fieldname_tid, 680.0);
		AnchorPane.setTopAnchor(fieldname_tid, 220.0);
		this.getChildren().add(fieldname_tid);
		
		
		Label antenna_title = new Label("Field Name (Antenna):");
		antenna_title.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
		AnchorPane.setLeftAnchor(antenna_title, 480.0);
		AnchorPane.setTopAnchor(antenna_title, 260.0);
		this.getChildren().add(antenna_title);
		
		fieldname_antenna = new TextField();
		fieldname_antenna.setPrefWidth(160);
		AnchorPane.setLeftAnchor(fieldname_antenna, 680.0);
		AnchorPane.setTopAnchor(fieldname_antenna, 260.0);
		this.getChildren().add(fieldname_antenna);
		
	}

	private ObservableList<DBType> loadDBOption() {
		ObservableList<DBType> dblist = FXCollections.observableArrayList();
		dblist.addAll(DBType.values());
		
		return dblist;
	}
	
	@Override
	public void handle(ActionEvent event) {
		if (event.getSource() == db_type) {
			switch (db_type.getSelectionModel().getSelectedIndex()) {
			case 0:
				break;
			case 1:
				break;
			}
		}
	}
	
	public void saveDBSetting() {
		ReaderConfig.getInstance().setDBType(db_type.getValue());
		ReaderConfig.getInstance().setDBIP(ip.getText());
		ReaderConfig.getInstance().setDBName(db_name.getText());
		ReaderConfig.getInstance().setUserId(userid.getText());
		ReaderConfig.getInstance().setPassword(password.getText());
		
		ReaderConfig.getInstance().setReaderId(readerid.getText());
		ReaderConfig.getInstance().setTableName(table_name.getText());
		ReaderConfig.getInstance().setEPCFieldName(fieldname_epc.getText());
		ReaderConfig.getInstance().setReaderIdFieldName(fieldname_readerid.getText());
		ReaderConfig.getInstance().setTimeFieldName(fieldname_time.getText());
		ReaderConfig.getInstance().setTIDFieldName(fieldname_tid.getText());
		ReaderConfig.getInstance().setAntennaFieldName(fieldname_antenna.getText());
		
		
		DatabaseUtility.getInstance().setDBType(db_type.getValue());
		DatabaseUtility.getInstance().setDBParameters(ip.getText(), db_name.getText(), userid.getText(), password.getText());
		DatabaseUtility.getInstance().setTableParameters(table_name.getText(), fieldname_epc.getText(), fieldname_readerid.getText(), fieldname_time.getText(), fieldname_tid.getText(), fieldname_antenna.getText());
		DatabaseUtility.getInstance().setReaderId(readerid.getText());
	}
	
	public void loadDBSetting() {
		db_type.setValue(ReaderConfig.getInstance().getDBType());
		ip.setText(ReaderConfig.getInstance().getDBIP());
		db_name.setText(ReaderConfig.getInstance().getDBName());
		userid.setText(ReaderConfig.getInstance().getUserId());
		password.setText(ReaderConfig.getInstance().getPassword());
		
		readerid.setText(ReaderConfig.getInstance().getReaderId());
		table_name.setText(ReaderConfig.getInstance().getTableName());
		fieldname_epc.setText(ReaderConfig.getInstance().getEPCFieldName());
		fieldname_readerid.setText(ReaderConfig.getInstance().getReaderIdFieldName());
		fieldname_time.setText(ReaderConfig.getInstance().getTimeFieldName());
		fieldname_tid.setText(ReaderConfig.getInstance().getTIDFieldName());
		fieldname_antenna.setText(ReaderConfig.getInstance().getAntennaFieldName());
		
		DatabaseUtility.getInstance().setDBType(db_type.getValue());
		DatabaseUtility.getInstance().setDBParameters(ip.getText(), db_name.getText(), userid.getText(), password.getText());
		DatabaseUtility.getInstance().setTableParameters(table_name.getText(), fieldname_epc.getText(), fieldname_readerid.getText(), fieldname_time.getText(), fieldname_tid.getText(), fieldname_antenna.getText());
		DatabaseUtility.getInstance().setReaderId(readerid.getText());
	}
}
