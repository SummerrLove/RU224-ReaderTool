package scannel.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.thingmagic.Reader.Region;
import com.thingmagic.ReaderException;

import application.Main;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import scannel.reader.DataUpdateListener;
import scannel.reader.ReaderUtility;
import scannel.reader.VideoConfig;

public class VideoFrame extends AnchorPane implements EventHandler<ActionEvent>, DataUpdateListener {

	private final String intro_path = "./Cinematic.mp4";
	private final String video1 = "./SPIDERMAN.mp4";
	private final String video2 = "./Terminator.mp4";
	private Media intro_video;
	private MediaPlayer intro_player;
	private MediaView introView;
	
	private final List<String> playList = new ArrayList<String>();
	private final KeyCombination FullScreenKey = new KeyCodeCombination(KeyCode.ENTER, KeyCombination.ALT_DOWN);
	
	
//	private TextField tf_power;
	private Button btn_start;
	private MediaView currentView;
	
	public VideoFrame() {
		initComponents();
		ReaderUtility.getInstance().setDataUpdateListener(this);
		
//		playList.add(new File(video1).toURI().toString());
//		playList.add(new File(video2).toURI().toString());
//		playList.add(VideoConfig.getInstance().getVideoLink(1));
//		playList.add(VideoConfig.getInstance().getVideoLink(2));
	}

	public VideoFrame(Node... arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	private void initComponents() {
//		intro_player.setOnReady(new Runnable() {    
//			public void run() {
//				System.out.println(intro_video.getWidth()+", "+intro_video.getHeight());
//			}
//		});
		
		String link = VideoConfig.getInstance().getIntroLink();
		if (link == null || link.length()==0) {
			intro_video = new Media(new File(intro_path).toURI().toString());
		} else {
			intro_video = new Media(link);
		}
		intro_player = new MediaPlayer(intro_video);
		introView = new MediaView(intro_player);
		introView.setFitWidth(1600);
//		AnchorPane.setLeftAnchor(mediaView, 0.0);
//		AnchorPane.setTopAnchor(mediaView, 0.0);
		this.getChildren().add(introView);
		currentView = introView;
//		intro_player.play();

//		this.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
//			System.out.println(event.getText());
//			if (FullScreenKey.match(event)) {
//				Main.getMainStage().setFullScreen(!Main.getMainStage().isFullScreen());
//			}
//		});
//		this.setOnKeyPressed(new EventHandler() {
//
//			@Override
//			public void handle(Event event) {
//				// TODO Auto-generated method stub
//				System.out.println("source="+event.getSource()+", target="+event.getTarget());
//			}
//			
//		});
		
//		Label label = new Label("RF Power: ");
//		label.setFont(Font.font("Arial", FontWeight.BOLD, 20));
//		AnchorPane.setLeftAnchor(label, 50.0);
//		AnchorPane.setTopAnchor(label, 925.0);
//		this.getChildren().add(label);
//		
//		tf_power = new TextField();
//		tf_power.setFont(Font.font("Arial", FontWeight.BOLD, 20));
//		tf_power.setPrefWidth(100);
//		AnchorPane.setLeftAnchor(tf_power, 160.0);
//		AnchorPane.setTopAnchor(tf_power, 920.0);
//		this.getChildren().add(tf_power);
		
		btn_start = new Button("Start");
		btn_start.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		btn_start.setPrefWidth(100);
		AnchorPane.setLeftAnchor(btn_start, 750.0);
		AnchorPane.setTopAnchor(btn_start, 920.0);
		this.getChildren().add(btn_start);
		btn_start.setOnAction(this);
	}
	
	public void insertPlayList(String uri) {
		// check if currently playing intro. 
		// yes -> add uri into list, stop intro, play video from list
		// no -> add uri into list
		
		synchronized(playList) {
			if (playList.size() == 1) {
				playList.add(0, uri);
			} else {
				playList.add(uri);
			}
		}
	}
	
	public void removePlayList(String uri) {
		// check if currently playing the video
		// yes -> remove uri from list, stop and play next video from play list, if list is empty, play intro
		// no -> remove uri from list
		
		synchronized(playList) {
			playList.remove(uri);
		}
	}

	@Override
	public void handle(ActionEvent event) {
		// TODO Auto-generated method stub
		if (event.getSource() == btn_start) {
			if (currentView.getMediaPlayer().getStatus().equals(Status.PLAYING)) {
				currentView.getMediaPlayer().stop();
				btn_start.setText("Start");
				
				ReaderUtility.getInstance().stopReading();
			} else if (currentView.getMediaPlayer().getStatus().equals(Status.STOPPED)) {
				currentView.getMediaPlayer().play();
				currentView.getMediaPlayer().setOnEndOfMedia(new Runnable() {

					@Override
					public void run() {
						checkPlayList();
					}
					
				});
				btn_start.setText("Stop");
				
				this.startReadingRFIDTags();
			} else if (currentView.getMediaPlayer().getStatus().equals(Status.READY)) {
				currentView.getMediaPlayer().play();
				currentView.getMediaPlayer().setOnEndOfMedia(new Runnable() {

					@Override
					public void run() {
						checkPlayList();
					}
					
				});
				btn_start.setText("Stop");
				
				this.startReadingRFIDTags();
			} else {
				System.out.println(currentView.getMediaPlayer().getStatus());
			}
		}
	}
	
	private void checkPlayList() {
		// when end of media event occurs, call this method to check if there is any uri in the list
		// yes -> play next uri
		// no -> play intro
		synchronized (playList) {
			if (playList.size() > 0) {
				this.getChildren().remove(currentView);
				String uri = playList.remove(0);
				playList.add(uri);
				MediaPlayer player = new MediaPlayer(new Media(uri));
				MediaView mv = new MediaView(player);
				mv.setFitWidth(1600);
				this.getChildren().add(mv);
				currentView = mv;
				player.play();
				player.setOnEndOfMedia(new Runnable() {
	
					@Override
					public void run() {
						// TODO Auto-generated method stub
						checkPlayList();
					}
					
				});
			} else {
				if (currentView != introView) {
					currentView = introView;
					this.getChildren().add(currentView);
				}
				currentView.getMediaPlayer().play();
			}
		}
	}
	
	private void startReadingRFIDTags() {
		int[] ant = {1, 2, 3, 4};
//		int power;
//		try {
//			power = Integer.parseInt(tf_power.getText());
//			if (power < 5 || power > 31) {
//				power = 24;
//			}
//		} catch (NumberFormatException e) {
//			power = 24;
//		}
		
		try {
			ReaderUtility.getInstance().clearTagList();
//			ReaderUtility.getInstance().setRFPower(power);
			ReaderUtility.getInstance().setRegion(Region.NA);
			ReaderUtility.getInstance().startReading(ant);
		} catch (ReaderException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void dataUpdate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void tagAdded(String epc) {
		String uri = VideoConfig.getInstance().getEPCLink(epc);
		
		if (uri == null) {
			System.out.println("No video file for the epc: "+epc);
			return;
		}
		
		if (currentView == introView) {
			System.out.println("Stop intro video and play video for the tag.");
			currentView.getMediaPlayer().stop();
			insertPlayList(uri);
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					checkPlayList();
				}
				
			});
		} else {
			System.out.println("Add video into play list...");
			insertPlayList(uri);
		}
		
		
	}

	@Override
	public void tagRemoved(String epc) {
		String uri = VideoConfig.getInstance().getEPCLink(epc);
		
		if (uri == null) {
			System.out.println("No video file for the epc: "+epc);
			return;
		}
		
		if (currentView.getMediaPlayer().getMedia().getSource().equals(uri)){
			System.out.println("Remove video: "+uri);
			currentView.getMediaPlayer().stop();
			removePlayList(uri);
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					checkPlayList();
				}
				
			});
		} else {
			removePlayList(uri);
		}
	}
}
