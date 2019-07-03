package scannel.ui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Separator;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import scannel.gpio.DigitalIOController;
import scannel.reader.ReaderConfig;

public class DigitalIOSettingFrame extends AnchorPane implements EventHandler<ActionEvent> {
	
	private RadioButton rb_activate_DI;
	private RadioButton rb_activate_DO;
	
	private IOFrame di_frame;
	private IOFrame do_frame;
	

	public DigitalIOSettingFrame() {
		this.initComponents();
		this.loadSetting();
	}

	public DigitalIOSettingFrame(Node... arg0) {
		super(arg0);
		
	}

	private void initComponents() {
		rb_activate_DI = new RadioButton("Activate DI trigger reading");
		rb_activate_DI.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		rb_activate_DI.setOnAction(this);
		AnchorPane.setLeftAnchor(rb_activate_DI, 50.0);
		AnchorPane.setTopAnchor(rb_activate_DI, 50.0);
		this.getChildren().add(rb_activate_DI);

		di_frame = new IOFrame("Digital Input");
		di_frame.setPrefSize(900, 200);
		di_frame.setVgap(40);
		di_frame.setHgap(250);
//		di_frame.setPadding(new Insets(20, 30, 20, 30));
		di_frame.setAlignment(Pos.TOP_CENTER);
		AnchorPane.setLeftAnchor(di_frame, 50.0);
		AnchorPane.setTopAnchor(di_frame, 100.0);
		this.getChildren().add(di_frame);
		
		
		
		Separator separator = new Separator();
		separator.setOrientation(Orientation.HORIZONTAL);
		AnchorPane.setLeftAnchor(separator, 20.0);
		AnchorPane.setRightAnchor(separator, 20.0);
		AnchorPane.setTopAnchor(separator, 350.0);
		this.getChildren().add(separator);
		
		
		rb_activate_DO = new RadioButton("Activate DO output when tag is read");
		rb_activate_DO.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		rb_activate_DO.setOnAction(this);
		AnchorPane.setLeftAnchor(rb_activate_DO, 50.0);
		AnchorPane.setTopAnchor(rb_activate_DO, 410.0);
		this.getChildren().add(rb_activate_DO);
		
		do_frame = new IOFrame("Digital Output");
		do_frame.setPrefSize(900, 200);
		do_frame.setVgap(40);
		do_frame.setHgap(250);
		do_frame.setAlignment(Pos.TOP_CENTER);
		AnchorPane.setLeftAnchor(do_frame, 50.0);
		AnchorPane.setTopAnchor(do_frame, 460.0);
		this.getChildren().add(do_frame);
		
	}

	@Override
	public void handle(ActionEvent event) {
		if (event.getSource() == rb_activate_DI) {
			di_frame.setDisable(!rb_activate_DI.isSelected());
		} else if (event.getSource() == rb_activate_DO) {
			do_frame.setDisable(!rb_activate_DO.isSelected());
		} 
	}
	
	private void loadSetting() {
		rb_activate_DI.setSelected(ReaderConfig.getInstance().getDITrigger());
		if (rb_activate_DI.isSelected()) {
			boolean[] setting = new boolean[4];
			setting[0] = ReaderConfig.getInstance().getDI1();
			setting[1] = ReaderConfig.getInstance().getDI2();
			setting[2] = ReaderConfig.getInstance().getDI3();
			setting[3] = ReaderConfig.getInstance().getDI4();
			
			di_frame.setIOSettingList(setting);
		}
		di_frame.setDisable(!rb_activate_DI.isSelected());
		
		rb_activate_DO.setSelected(ReaderConfig.getInstance().getDOTrigger());
		if (rb_activate_DO.isSelected()) {
			boolean[] setting = new boolean[4];
			setting[0] = ReaderConfig.getInstance().getDO1();
			setting[1] = ReaderConfig.getInstance().getDO2();
			setting[2] = ReaderConfig.getInstance().getDO3();
			setting[3] = ReaderConfig.getInstance().getDO4();
			
			do_frame.setIOSettingList(setting);
		}
		do_frame.setDisable(!rb_activate_DO.isSelected());
	}
	
	public void applySetting() {
		ReaderConfig.getInstance().setDITrigger(rb_activate_DI.isSelected());
		DigitalIOController.getInstance().setDIActivation(rb_activate_DI.isSelected());
		DigitalIOController.getInstance().removeDigitalInputListener();
		ReaderConfig.getInstance().setDOTrigger(rb_activate_DO.isSelected());
		DigitalIOController.getInstance().setDOActivation(rb_activate_DO.isSelected());
		DigitalIOController.getInstance().removeDigitalOutputListener();
		
		if (rb_activate_DI.isSelected()) {
			boolean[] di_setting = di_frame.getIOSettingList();
			ReaderConfig.getInstance().setDI1(di_setting[0]);
			ReaderConfig.getInstance().setDI2(di_setting[1]);
			ReaderConfig.getInstance().setDI3(di_setting[2]);
			ReaderConfig.getInstance().setDI4(di_setting[3]);
			DigitalIOController.getInstance().enableDI(di_setting);
		}
		
		ReaderConfig.getInstance().setDOTrigger(rb_activate_DO.isSelected());
		if (rb_activate_DO.isSelected()) {
			boolean[] do_setting = do_frame.getIOSettingList();
			ReaderConfig.getInstance().setDO1(do_setting[0]);
			ReaderConfig.getInstance().setDO2(do_setting[1]);
			ReaderConfig.getInstance().setDO3(do_setting[2]);
			ReaderConfig.getInstance().setDO4(do_setting[3]);
			DigitalIOController.getInstance().enableDO(do_setting);
		}
	}
}
