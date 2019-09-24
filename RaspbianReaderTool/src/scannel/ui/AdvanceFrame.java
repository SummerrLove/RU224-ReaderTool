package scannel.ui;

import com.thingmagic.ReaderException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import scannel.reader.ReaderUtility;

public class AdvanceFrame extends AnchorPane implements EventHandler<ActionEvent> {

	private TextField rf_on;
	private TextField rf_off;
	private Button btn_apply;
	
	
	public AdvanceFrame() {
		initComponents();
	}

	public AdvanceFrame(Node... arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	private void initComponents() {
		Label label_rf = new Label("RF Time Setting:");
		label_rf.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		AnchorPane.setLeftAnchor(label_rf, 50.0);
		AnchorPane.setTopAnchor(label_rf, 50.0);
		this.getChildren().add(label_rf);
		
		Label label_rf_on = new Label("On");
		label_rf_on.setFont(Font.font("Arial", FontWeight.BOLD, 16));
		AnchorPane.setLeftAnchor(label_rf_on, 50.0);
		AnchorPane.setTopAnchor(label_rf_on, 105.0);
		this.getChildren().add(label_rf_on);
		
		rf_on = new TextField();
		rf_on.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
		rf_on.setPrefWidth(100);
		AnchorPane.setLeftAnchor(rf_on, 90.0);
		AnchorPane.setTopAnchor(rf_on, 100.0);
		this.getChildren().add(rf_on);
		
		Label label_rf_off = new Label("Off");
		label_rf_off.setFont(Font.font("Arial", FontWeight.BOLD, 16));
		AnchorPane.setLeftAnchor(label_rf_off, 250.0);
		AnchorPane.setTopAnchor(label_rf_off, 105.0);
		this.getChildren().add(label_rf_off);
		
		rf_off = new TextField();
		rf_off.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
		rf_off.setPrefWidth(100);
		AnchorPane.setLeftAnchor(rf_off, 290.0);
		AnchorPane.setTopAnchor(rf_off, 100.0);
		this.getChildren().add(rf_off);
		
		btn_apply = new Button("Apply");
		btn_apply.setFont(Font.font("Arial", FontWeight.BOLD, 24));
		AnchorPane.setLeftAnchor(btn_apply, 50.0);
		AnchorPane.setTopAnchor(btn_apply, 400.0);
		this.getChildren().add(btn_apply);
		btn_apply.setOnAction(this);
	}

	@Override
	public void handle(ActionEvent event) {
		if (event.getSource() == btn_apply) {
			try {
				int onTime = Integer.parseInt(rf_on.getText());
				int offTime = Integer.parseInt(rf_off.getText());
				
				ReaderUtility.getInstance().setRFOnTime(onTime);
				ReaderUtility.getInstance().setRFOffTime(offTime);
			} catch (NumberFormatException e) {
				e.printStackTrace();
				
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText(null);
				alert.setContentText(e.getMessage());
				alert.showAndWait();
			} catch (ReaderException e) {
				e.printStackTrace();
				
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText(null);
				alert.setContentText(e.getMessage());
				alert.showAndWait();
			} catch (Exception e) {
				e.printStackTrace();
				
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText(null);
				alert.setContentText(e.getMessage());
				alert.showAndWait();
			}
		}
	}
	
	public void getCurretSetting() {
		try {
			rf_on.setText(Integer.toString(ReaderUtility.getInstance().getRFOnTime()));
			rf_off.setText(Integer.toString(ReaderUtility.getInstance().getRFOffTime()));
		} catch (ReaderException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
