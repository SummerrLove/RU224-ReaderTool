package scannel.ui;

import java.io.File;

import com.thingmagic.ReaderException;
import com.thingmagic.TagReadData;

import application.Main;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import scannel.reader.ReaderUtility;
import scannel.reader.VideoConfig;

public class VideoLinkSettingFrame extends AnchorPane implements EventHandler<ActionEvent> {

	private TextField[] tags;
	private Button[] readButton;
	private TextField[] links;
	private Button[] openLinkButton;
	private FileChooser fileChooser;
	
	private TextField introLink;
	private Button introLinkButton;
	private Button save;
	private TextField tf_power;
	
	private final int SIZE = 5;
	
	public VideoLinkSettingFrame() {
		this.initComponenets();
		this.loadVideoLinks();
	}

	public VideoLinkSettingFrame(Node... children) {
		super(children);
		// TODO Auto-generated constructor stub
	}

	private void initComponenets() {
		fileChooser = new FileChooser();
		fileChooser.setTitle("Select a video file");
		
		Label intro = new Label("Introduction Video: ");
		intro.setFont(Font.font("Arial", FontWeight.BOLD, 24));
		AnchorPane.setLeftAnchor(intro, 40.0);
		AnchorPane.setTopAnchor(intro, 55.0);
		this.getChildren().add(intro);
		
		introLink = new TextField();
		introLink.setPrefWidth(870.0);
		introLink.setFont(Font.font("Arial", FontWeight.NORMAL, 24));
		AnchorPane.setLeftAnchor(introLink, 280.0);
		AnchorPane.setTopAnchor(introLink, 50.0);
		this.getChildren().add(introLink);
		
		introLinkButton = new Button("select file");
		introLinkButton.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
		AnchorPane.setLeftAnchor(introLinkButton, 1180.0);
		AnchorPane.setTopAnchor(introLinkButton, 55.0);
		this.getChildren().add(introLinkButton);
		introLinkButton.setOnAction(this);
		
		save = new Button("SAVE");
		save.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		AnchorPane.setLeftAnchor(save, 1400.0);
		AnchorPane.setTopAnchor(save, 50.0);
		this.getChildren().add(save);
		save.setOnAction(this);
		
		
		tags = new TextField[SIZE];
		readButton = new Button[SIZE];
		links = new TextField[SIZE];
		openLinkButton = new Button[SIZE];
		
		for (int i=0; i<SIZE; i++) {
			Rectangle rect1 = new Rectangle(40, 140+150*i, 1520, 100);
			rect1.setArcHeight(15);
			rect1.setArcWidth(15);
			rect1.setFill(Color.TRANSPARENT);
			rect1.setStroke(Color.BLACK);
			this.getChildren().add(rect1);
			
			tags[i] = new TextField();
			tags[i].setPrefWidth(250.0);
			tags[i].setFont(Font.font("Arial", FontWeight.NORMAL, 24));
			AnchorPane.setLeftAnchor(tags[i], 80.0);
			AnchorPane.setTopAnchor(tags[i], 170.0+i*150);
			this.getChildren().add(tags[i]);
			
			readButton[i] = new Button("read tag");
			readButton[i].setFont(Font.font("Arial", FontWeight.NORMAL, 16));
			AnchorPane.setLeftAnchor(readButton[i], 340.0);
			AnchorPane.setTopAnchor(readButton[i], 175.0+i*150);
			readButton[i].setOnAction(this);
			this.getChildren().add(readButton[i]);
			
			links[i] = new TextField();
			links[i].setPrefWidth(870.0);
			links[i].setFont(Font.font("Arial", FontWeight.NORMAL, 24));
			AnchorPane.setLeftAnchor(links[i], 530.0);
			AnchorPane.setTopAnchor(links[i], 170.0+i*150);
			this.getChildren().add(links[i]);
			
			openLinkButton[i] = new Button("select file");
			openLinkButton[i].setFont(Font.font("Arial", FontWeight.NORMAL, 16));
			AnchorPane.setLeftAnchor(openLinkButton[i], 1430.0);
			AnchorPane.setTopAnchor(openLinkButton[i], 175.0+i*150);
			openLinkButton[i].setOnAction(this);
			this.getChildren().add(openLinkButton[i]);
		}
		
		Label label = new Label("RF Power: ");
		label.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		AnchorPane.setLeftAnchor(label, 50.0);
		AnchorPane.setTopAnchor(label, 875.0);
		this.getChildren().add(label);
		
		tf_power = new TextField();
		tf_power.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		tf_power.setPrefWidth(100);
		AnchorPane.setLeftAnchor(tf_power, 160.0);
		AnchorPane.setTopAnchor(tf_power, 870.0);
		this.getChildren().add(tf_power);
	}

	@Override
	public void handle(ActionEvent event) {
		
		if (event.getSource() == introLinkButton) {
			File file = fileChooser.showOpenDialog(Main.getMainStage());
			if (file != null) {
				introLink.setText(file.toURI().toString());
			}
			return;
		}
		
		for (int i=0; i<openLinkButton.length; i++) {
			if (event.getSource() == openLinkButton[i]) {
				File file = fileChooser.showOpenDialog(Main.getMainStage());
				if (file != null) {
					links[i].setText(file.toURI().toString());
				}
				return;
			} else {
				System.out.println(event);
			}
		}
		
		if (event.getSource() == save) {
			if (!ReaderUtility.getInstance().isReading()) {
				saveRFPower();
			}
			saveVideoLink();
			return;
		}
		
		for (int i=0; i<readButton.length; i++) {
			if (event.getSource() == readButton[i]) {
				try {
					TagReadData trd = ReaderUtility.getInstance().readSingleTag();
					tags[i].setText(trd.epcString());
				} catch (ReaderException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	private void loadVideoLinks() {
		introLink.setText(VideoConfig.getInstance().getIntroLink());
		
		for (int i=0; i<SIZE; i++) {
			tags[i].setText(VideoConfig.getInstance().getEPC(i+1));
			links[i].setText(VideoConfig.getInstance().getVideoLink(i+1));
		}
	}
	
	private void saveVideoLink() {
		if (introLink.getText().length() > 0) {
			VideoConfig.getInstance().setIntroLink(introLink.getText());
		}
		
		for (int i=0; i<SIZE; i++) {
//			System.out.println(i+". "+tags[i].getText()+", "+links[i].getText());
//			if (isEmpty(tags[i].getText()) || isEmpty(links[i].getText())) {
//				VideoConfig.getInstance().setEPC((i+1), "");
//				VideoConfig.getInstance().setVideoLink((i+1), "");
//			} else {
				VideoConfig.getInstance().setEPC((i+1), tags[i].getText());
				VideoConfig.getInstance().setVideoLink((i+1), links[i].getText());
//			}
		}
		
		VideoConfig.getInstance().saveConfig();
	}
	
	private boolean isEmpty(String text) {
		if ((text != null) && (text.length() > 0)) {
			return false;
		} else {
			return true;
		}
	}
	
	private void saveRFPower() {
		int power;
		try {
			power = Integer.parseInt(tf_power.getText());
			if (power < 5 || power > 31) {
				power = 24;
			}
		} catch (NumberFormatException e) {
			power = 24;
		}
		
		try {
			ReaderUtility.getInstance().setRFPower(power);
		} catch (ReaderException e) {
			e.printStackTrace();
		}
	}
}
