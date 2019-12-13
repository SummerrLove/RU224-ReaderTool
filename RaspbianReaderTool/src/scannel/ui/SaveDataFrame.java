package scannel.ui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import scannel.reader.ReaderConfig;

public class SaveDataFrame extends AnchorPane implements EventHandler<ActionEvent> {

	private CheckBox cb_saveDisk;
	private CheckBox cb_saveDB;
	private DBSettingFrame db_setting;
	private Button btn_apply;
	
	
	private static boolean SAVE_DATA_IN_DISK = false;
	private static boolean SAVE_DATA_IN_DB = false;
	
	public SaveDataFrame() {
		this.initComponents();
		this.loadSetting();
	}

	public SaveDataFrame(Node... arg0) {
		super(arg0);
	}

	private void initComponents() {
		cb_saveDisk = new CheckBox("Save data in disk");
		cb_saveDisk.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		cb_saveDisk.setOnAction(this);
		AnchorPane.setLeftAnchor(cb_saveDisk, 60.0);
		AnchorPane.setTopAnchor(cb_saveDisk, 30.0);
		this.getChildren().add(cb_saveDisk);
		
		cb_saveDB = new CheckBox("Save data into DB");
		cb_saveDB.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		cb_saveDB.setOnAction(this);
		AnchorPane.setLeftAnchor(cb_saveDB, 60.0);
		AnchorPane.setTopAnchor(cb_saveDB, 100.0);
		this.getChildren().add(cb_saveDB);
		
		
		db_setting = new DBSettingFrame();
		AnchorPane.setLeftAnchor(db_setting, 50.0);
		AnchorPane.setTopAnchor(db_setting, 150.0);
		AnchorPane.setRightAnchor(db_setting, 50.0);
		AnchorPane.setBottomAnchor(db_setting, 270.0);
		this.getChildren().add(db_setting);
		db_setting.setDisable(true);
		
		
		btn_apply = new Button("Apply");
		btn_apply.setPrefWidth(70);
		btn_apply.setOnAction(this);
		AnchorPane.setLeftAnchor(btn_apply, 60.0);
		AnchorPane.setTopAnchor(btn_apply, 530.0);
		this.getChildren().add(btn_apply);
	}
	
	private void loadSetting() {
		db_setting.loadDBSetting();
		
		SAVE_DATA_IN_DISK = ReaderConfig.getInstance().isDataLocalSaveEnabled();
		cb_saveDisk.setSelected(SAVE_DATA_IN_DISK);
		
		SAVE_DATA_IN_DB = ReaderConfig.getInstance().isDataDBSaveEnabled();
		cb_saveDB.setSelected(SAVE_DATA_IN_DB);
		db_setting.setDisable(!SAVE_DATA_IN_DB);
	}
	
	@Override
	public void handle(ActionEvent event) {
		if (event.getSource() == cb_saveDisk) {
			SAVE_DATA_IN_DISK = cb_saveDisk.isSelected();
			ReaderConfig.getInstance().setDataLocalSave(SAVE_DATA_IN_DISK);
		} else if (event.getSource() == cb_saveDB) {
			SAVE_DATA_IN_DB = cb_saveDB.isSelected();
			db_setting.setDisable(!SAVE_DATA_IN_DB);
			ReaderConfig.getInstance().setDataDBSave(SAVE_DATA_IN_DB);
		} else if (event.getSource() == btn_apply) {
			db_setting.saveDBSetting();
		}
		
	}
}
