package scannel.ui;

import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Separator;
import javafx.scene.layout.AnchorPane;

public class ReadFrame extends AnchorPane {

	private ControlFrame control;
	private DataFrame data;
	
	
	public ReadFrame() {
		this.initComponent();
		
	}

	public ReadFrame(Node... arg0) {
		super(arg0);
	}

	// Create sub-components
	private void initComponent() {
		
		control = new ControlFrame();
		AnchorPane.setLeftAnchor(control, 0.0);
		AnchorPane.setTopAnchor(control, 0.0);
		AnchorPane.setBottomAnchor(control, 0.0);
		control.setPrefWidth(200);

		
		Separator separator = new Separator();
		separator.setOrientation(Orientation.VERTICAL);
		AnchorPane.setLeftAnchor(separator, 200.0);
		AnchorPane.setTopAnchor(separator, 0.0);
		AnchorPane.setBottomAnchor(separator, 0.0);
		
		
		data = new DataFrame();
		AnchorPane.setLeftAnchor(data, 210.0);
		AnchorPane.setRightAnchor(data, 20.0);
		AnchorPane.setTopAnchor(data, 0.0);
		AnchorPane.setBottomAnchor(data, 0.0);
		
		this.getChildren().addAll(control, separator, data);
	}

	public void checkDITrigger() {
		control.checkDITrigger();
	}
}
