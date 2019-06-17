package scannel.reader;

public interface DataUpdateListener {

	public void dataUpdate();
	public void tagAdded(String epc);
	public void tagRemoved(String epc);
}
