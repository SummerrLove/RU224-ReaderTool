package scannel.ui;

import javax.xml.bind.DatatypeConverter;

import com.thingmagic.Gen2;
import com.thingmagic.Gen2.Bank;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import scannel.reader.ReaderUtility;
import scannel.reader.StringTool;

public class FilterSettingFrame extends AnchorPane implements EventHandler<ActionEvent> {

	private RadioButton rb_epc;
	private RadioButton rb_userMem;
	private TextField filter;
	private TextField bitPointer;
	private Button btn_apply;
	
	
	public FilterSettingFrame() {
		this.initComponents();
	}

	public FilterSettingFrame(Node... children) {
		super(children);
		// TODO Auto-generated constructor stub
	}

	private void initComponents() {
		
		Label title = new Label("Tag Filter");
		title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
		AnchorPane.setLeftAnchor(title, 50.0);
		AnchorPane.setTopAnchor(title, 50.0);
		this.getChildren().add(title);
		
		
		rb_epc = new RadioButton("EPC");
		rb_epc.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
		rb_epc.setOnAction(this);
		AnchorPane.setLeftAnchor(rb_epc, 50.0);
		AnchorPane.setTopAnchor(rb_epc, 100.0);
		this.getChildren().add(rb_epc);
		
		
		rb_userMem = new RadioButton("User Memory");
		rb_userMem.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
		rb_userMem.setOnAction(this);
		AnchorPane.setLeftAnchor(rb_userMem, 150.0);
		AnchorPane.setTopAnchor(rb_userMem, 100.0);
		this.getChildren().add(rb_userMem);
		
		
		Label filter_title = new Label("Mask:");
		filter_title.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		AnchorPane.setLeftAnchor(filter_title, 50.0);
		AnchorPane.setTopAnchor(filter_title, 170.0);
		this.getChildren().add(filter_title);
		
		filter = new TextField();
		filter.setFont(Font.font("Arial", FontWeight.NORMAL, 30));
		filter.setPromptText("Input binary string");
		filter.setPrefWidth(500);
		filter.setDisable(true);
		AnchorPane.setLeftAnchor(filter, 50.0);
		AnchorPane.setTopAnchor(filter, 210.0);
		this.getChildren().add(filter);
		
		
		Label butPointer_title = new Label("Starting Bit Position:");
		butPointer_title.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		AnchorPane.setLeftAnchor(butPointer_title, 50.0);
		AnchorPane.setTopAnchor(butPointer_title, 320.0);
		this.getChildren().add(butPointer_title);
		
		bitPointer = new TextField();
//		bitPointer.setPromptText("");
		bitPointer.setFont(Font.font("Arial", FontWeight.NORMAL, 20));
		bitPointer.setPrefWidth(100);
		bitPointer.setDisable(true);
		AnchorPane.setLeftAnchor(bitPointer, 50.0);
		AnchorPane.setTopAnchor(bitPointer, 360.0);
		this.getChildren().add(bitPointer);
		
		
		btn_apply = new Button("Apply");
		btn_apply.setOnAction(this);
		btn_apply.setFont(Font.font("Arial", FontWeight.NORMAL, 20));
		AnchorPane.setLeftAnchor(btn_apply, 50.0);
		AnchorPane.setTopAnchor(btn_apply, 500.0);
		this.getChildren().add(btn_apply);
	}

	@Override
	public void handle(ActionEvent event) {
		if (event.getSource() == rb_epc) {
			if (rb_epc.isSelected() && rb_userMem.isSelected()) {
				rb_userMem.setSelected(false);
			}
			
			// disable input if neither of the memory bank is selected
			if (!rb_epc.isSelected() && !rb_userMem.isSelected()) {
				filter.setDisable(true);
				filter.setText("");
				bitPointer.setDisable(true);
				bitPointer.setText("");
			} else {
				filter.setDisable(false);
				bitPointer.setDisable(false);
			}
		} else if (event.getSource() == rb_userMem) {
			if (rb_epc.isSelected() && rb_userMem.isSelected()) {
				rb_epc.setSelected(false);
			}
			
			// disable input if neither of the memory banks is selected
			if (!rb_epc.isSelected() && !rb_userMem.isSelected()) {
				filter.setDisable(true);
				filter.setText("");
				bitPointer.setDisable(true);
				bitPointer.setText("");
			} else {
				filter.setDisable(false);
				bitPointer.setDisable(false);
			}
		} else if (event.getSource() == btn_apply) {
			if (!rb_epc.isSelected() && !rb_userMem.isSelected()) {
				// if neither of the memory banks is selected, remove the filter
				ReaderUtility.getInstance().resetFilter();
			} else {
				Gen2.Bank bank = null;
				try {
					byte[] data = StringTool.toByteArray(filter.getText());
					if (data == null) {
						Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("Error");
						alert.setHeaderText(null);
						alert.setContentText("Please input valid binary string...");
						alert.showAndWait();
					} else {
						int pointer = Integer.parseInt(bitPointer.getText());
						if (rb_epc.isSelected()) {
							bank = Bank.EPC;
							// the data in EPC bank before bit 32 is meta-data
							pointer += 32;
						} else if (rb_userMem.isSelected()) {
							bank = Bank.USER;
						}
						
						ReaderUtility.getInstance().setFilter(bank, pointer, filter.getText().length(), data);
					}
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
					
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Error");
					alert.setHeaderText(null);
					alert.setContentText(e.getMessage());
					alert.showAndWait();
				}
				
			}
		}
		
	}
	
	
}
