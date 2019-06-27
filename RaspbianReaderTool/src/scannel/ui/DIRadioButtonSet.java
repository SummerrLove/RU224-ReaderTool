package scannel.ui;

import com.pi4j.io.gpio.PinState;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.GridPane;

public class DIRadioButtonSet extends GridPane implements EventHandler<ActionEvent> {

//	public enum IO_EVENT{
//		LOW_TO_HIGH,
//		HIGH_TO_LOW,
//		NONE
//	}
	
	private RadioButton rb_high;
	private RadioButton rb_low;
	
	
	public DIRadioButtonSet() {
		initComponents();
	}

	private void initComponents() {
		rb_high = new RadioButton("Low-to-High");
		rb_high.setOnAction(this);
		rb_low = new RadioButton("High-to-Low");
		rb_low.setOnAction(this);
		
		this.setHgap(20.0);
		this.add(rb_high, 0, 0);
		this.add(rb_low, 1, 0);
	}

	@Override
	public void handle(ActionEvent event) {
		if (event.getSource() == rb_high && rb_high.isSelected()) {
			rb_low.setSelected(!rb_high.isSelected());
		} else if (event.getSource() == rb_low && rb_low.isSelected()) {
			rb_high.setSelected(!rb_low.isSelected());
		}
		
	}
	
	public PinState getSetting() {
		if (rb_high.isSelected()) {
			return PinState.HIGH;
		}
		
		if (rb_low.isSelected()) {
			return PinState.LOW;
		}
		
		// when none of the options is selected, return null
		return null;
	}
	
	public void setSetting(PinState state) {
		if (state == PinState.HIGH) {
			rb_high.setSelected(true);
			rb_low.setSelected(false);
		} else if (state == PinState.LOW) {
			rb_high.setSelected(false);
			rb_low.setSelected(true);
		} else {
			rb_high.setSelected(false);
			rb_low.setSelected(false);
		}
	}
}
