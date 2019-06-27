package scannel.ui;

import com.pi4j.io.gpio.PinState;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Separator;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import scannel.gpio.DigitalIOController;
import scannel.reader.ReaderConfig;

public class DigitalIOSettingFrame extends AnchorPane implements EventHandler<ActionEvent> {
	
	private RadioButton rb_activate_DI;
	private RadioButton rb_activate_DO;
	
	private DISettingFrame start_di;
	private DISettingFrame stop_di;
	
//	private IOFrame di_frame;
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
		AnchorPane.setTopAnchor(rb_activate_DI, 40.0);
		this.getChildren().add(rb_activate_DI);

		createDISetting();
		
//		di_frame = new IOFrame("Digital Input");
//		di_frame.setPrefSize(900, 200);
//		di_frame.setVgap(40);
//		di_frame.setHgap(250);
////		di_frame.setPadding(new Insets(20, 30, 20, 30));
//		di_frame.setAlignment(Pos.TOP_CENTER);
//		AnchorPane.setLeftAnchor(di_frame, 50.0);
//		AnchorPane.setTopAnchor(di_frame, 100.0);
//		this.getChildren().add(di_frame);
		
		
		
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
		
		
//		btn_apply = new Button("Apply");
//		btn_apply.setPrefWidth(70);
//		btn_apply.setOnAction(this);
//		AnchorPane.setLeftAnchor(btn_apply, 60.0);
//		AnchorPane.setTopAnchor(btn_apply, 700.0);
//		this.getChildren().add(btn_apply);
	}

	private void createDISetting() {
		Rectangle rect1 = new Rectangle(40, 90, 410, 200);
		rect1.setArcHeight(15);
		rect1.setArcWidth(15);
		rect1.setFill(Color.WHITE);
		rect1.setStroke(Color.BLACK);
		this.getChildren().add(rect1);
		
		start_di = new DISettingFrame("Start inventory:");
		start_di.setPrefSize(410, 200);
		AnchorPane.setLeftAnchor(start_di, 40.0);
		AnchorPane.setTopAnchor(start_di, 90.0);
		this.getChildren().add(start_di);
//		Label start_label = new Label("Start inventory:");
//		start_label.setFont(Font.font("Arial", FontWeight.BOLD, 20));
//		AnchorPane.setLeftAnchor(start_label, 60.0);
//		AnchorPane.setTopAnchor(start_label, 110.0);
//		this.getChildren().add(start_label);
//		
//		start_di = new DIRadioButtonSet[4];
//		for (int i=0; i<start_di.length; i++) {
//			Label label = new Label("DI #"+(i+1));
//			label.setFont(Font.font("Arial", FontWeight.BOLD, 16));
//			AnchorPane.setLeftAnchor(label, 60.0);
//			AnchorPane.setTopAnchor(label, 150.0 + i*30);
//			this.getChildren().add(label);
//			
//			start_di[i] = new DIRadioButtonSet();
//			AnchorPane.setLeftAnchor(start_di[i], 120.0);
//			AnchorPane.setTopAnchor(start_di[i], 150.0 + i*30);
//			this.getChildren().add(start_di[i]);
//		}
		
		Rectangle rect2 = new Rectangle(500, 90, 410, 200);
		rect2.setArcHeight(15);
		rect2.setArcWidth(15);
		rect2.setFill(Color.WHITE);
		rect2.setStroke(Color.BLACK);
		this.getChildren().add(rect2);
		
		stop_di = new DISettingFrame("Stop inventory:");
		stop_di.setPrefSize(410, 200);
		AnchorPane.setLeftAnchor(stop_di, 500.0);
		AnchorPane.setTopAnchor(stop_di, 90.0);
		this.getChildren().add(stop_di);
//		Label stop_label = new Label("Stop inventory:");
//		stop_label.setFont(Font.font("Arial", FontWeight.BOLD, 20));
//		AnchorPane.setLeftAnchor(stop_label, 520.0);
//		AnchorPane.setTopAnchor(stop_label, 110.0);
//		this.getChildren().add(stop_label);
//		
//		stop_di = new DIRadioButtonSet[4];
//		for (int i=0; i<stop_di.length; i++) {
//			Label label = new Label("DI #"+(i+1));
//			label.setFont(Font.font("Arial", FontWeight.BOLD, 16));
//			AnchorPane.setLeftAnchor(label, 520.0);
//			AnchorPane.setTopAnchor(label, 150.0 + i*30);
//			this.getChildren().add(label);
//			
//			stop_di[i] = new DIRadioButtonSet();
//			AnchorPane.setLeftAnchor(stop_di[i], 580.0);
//			AnchorPane.setTopAnchor(stop_di[i], 150.0 + i*30);
//			this.getChildren().add(stop_di[i]);
//		}
	}
	
	@Override
	public void handle(ActionEvent event) {
		if (event.getSource() == rb_activate_DI) {
//			di_frame.setDisable(!rb_activate_DI.isSelected());
			start_di.setDisable(!rb_activate_DI.isSelected());
			stop_di.setDisable(!rb_activate_DI.isSelected());
		} else if (event.getSource() == rb_activate_DO) {
			do_frame.setDisable(!rb_activate_DO.isSelected());
		} 
	}
	
	private void loadSetting() {
		rb_activate_DI.setSelected(ReaderConfig.getInstance().getDITrigger());
		if (rb_activate_DI.isSelected()) {
			PinState[] input_start = new PinState[4];
			PinState[] input_stop = new PinState[4];

			for (int i=0; i<input_start.length; i++) {
				input_start[i] = ReaderConfig.getInstance().getDIStart(i+1);
				input_stop[i] = ReaderConfig.getInstance().getDIStop(i+1);
			}
			
			start_di.setSetting(input_start);
			stop_di.setSetting(input_stop);
		}
		start_di.setDisable(!rb_activate_DI.isSelected());
		stop_di.setDisable(!rb_activate_DI.isSelected());
		
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
			PinState[] di_start_setting = start_di.getSetting();
			for (int i=0; i<di_start_setting.length; i++) {
				ReaderConfig.getInstance().setDIStart((i+1), di_start_setting[i]);
			}
			
			PinState[] di_stop_setting = stop_di.getSetting();
			for (int i=0; i<di_stop_setting.length; i++) {
				ReaderConfig.getInstance().setDIStop((i+1), di_stop_setting[i]);
			}
			DigitalIOController.getInstance().enableDI(di_start_setting, di_stop_setting);
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
