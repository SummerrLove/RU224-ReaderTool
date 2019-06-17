package scannel.rfid;

import java.util.HashMap;

public class TagMonitor {

	private HashMap monitorList = new HashMap();
	
	public TagMonitor() {
		// TODO Auto-generated constructor stub
	}

	public void addMonitoredEPC(String epc, String videoLink) {
		monitorList.put(epc, videoLink);
	}
}
