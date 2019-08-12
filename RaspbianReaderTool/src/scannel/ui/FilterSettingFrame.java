package scannel.ui;

import com.thingmagic.Gen2;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

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
		AnchorPane.setLeftAnchor(title, 30.0);
		AnchorPane.setTopAnchor(title, 50.0);
		this.getChildren().add(title);
		
		
		rb_epc = new RadioButton("EPC");
		rb_epc.setOnAction(this);
		AnchorPane.setLeftAnchor(rb_epc, 30.0);
		AnchorPane.setTopAnchor(rb_epc, 100.0);
		this.getChildren().add(rb_epc);
		
		
		rb_userMem = new RadioButton("User Memory");
		rb_userMem.setOnAction(this);
		AnchorPane.setLeftAnchor(rb_userMem, 130.0);
		AnchorPane.setTopAnchor(rb_userMem, 100.0);
		this.getChildren().add(rb_userMem);
		
		
		Label filter_title = new Label("Filter String:");
		filter_title.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		AnchorPane.setLeftAnchor(filter_title, 30.0);
		AnchorPane.setTopAnchor(filter_title, 350.0);
		this.getChildren().add(filter_title);
		
		filter = new TextField();
		filter.setPromptText("Input even-length hex string.");
		AnchorPane.setLeftAnchor(filter, 30.0);
		AnchorPane.setTopAnchor(filter, 390.0);
		this.getChildren().add(filter);
		
		
		Label butPointer_title = new Label("Starting Bit Position:");
		butPointer_title.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		AnchorPane.setLeftAnchor(butPointer_title, 30.0);
		AnchorPane.setTopAnchor(butPointer_title, 450.0);
		this.getChildren().add(butPointer_title);
		
		bitPointer = new TextField();
//		bitPointer.setPromptText("");
		AnchorPane.setLeftAnchor(bitPointer, 30.0);
		AnchorPane.setTopAnchor(bitPointer, 380.0);
		this.getChildren().add(bitPointer);
		
		
		btn_apply = new Button("Apply");
		btn_apply.setOnAction(this);
		btn_apply.setFont(Font.font("Arial", FontWeight.NORMAL, 20));
		AnchorPane.setLeftAnchor(btn_apply, 30.0);
		AnchorPane.setTopAnchor(btn_apply, 500.0);
		this.getChildren().add(btn_apply);
	}

	@Override
	public void handle(ActionEvent event) {
		if (event.getSource() == rb_epc) {
			if (rb_epc.isSelected() && rb_userMem.isSelected()) {
				rb_userMem.setSelected(false);
			}
		} else if (event.getSource() == rb_userMem) {
			if (rb_epc.isSelected() && rb_userMem.isSelected()) {
				rb_epc.setSelected(false);
			}
		} else if (event.getSource() == btn_apply) {
			Gen2.Bank memBank;
			if (rb_epc.isSelected()) {
				
				return;
			} 
			
			if (rb_userMem.isSelected()) {
				
				return;
			}
			
			// Neither epc filter nor user memory filter is selected,
			// so do nothing.
			
		}
		
	}
}
