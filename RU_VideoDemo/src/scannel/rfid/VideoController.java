package scannel.rfid;

import java.util.HashMap;
import java.util.List;

public class VideoController {

	private static VideoController videoController;
	
	private String introUri;
	private String[] epcs;
	private List epcList;
	private HashMap videoUri;
	
	private VideoController() {
		// TODO Auto-generated constructor stub
	}

	public VideoController getInstance() {
		if (videoController == null) {
			videoController = new VideoController();
		}
		
		return videoController;
	}
	
	public void endOFMedia() {
		
	}
	
	public void tagDetected(String epc) {
		
	}
	
	public void tagRemoved(String epc) {
		
	}
}
