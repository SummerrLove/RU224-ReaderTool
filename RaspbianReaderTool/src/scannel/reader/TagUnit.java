package scannel.reader;

import java.util.Date;

public class TagUnit {

	private String epc;
	private int readCount;
	private Date time;
	private int readFrequency;
	private int antennaId;
	private String tid;
	private String userBank;
	
	public TagUnit(String epc) {
		this.epc = epc;
		readCount = 0;
	}
	
	public TagUnit(String epc, int count){
		this.epc = epc;
		readCount = count;
	}
	
	public TagUnit(String epc, int count, int frequency) {
		this.epc = epc;
		readCount = count;
		readFrequency = frequency;
//		this.tid = tid;
//		this.userBank = userBank;
	}

	public void setEPC(String epc){
		this.epc = epc;
	}
	
	public String getEPC(){
		return this.epc;
	}
	
	public void setTid(String tid) {
		this.tid = tid;
	}
	
	public String getTid() {
		return this.tid;
	}
	
	public void setUserBank(String userBank) {
		this.userBank = userBank;
	}
	
	public String getUserBank() {
		return this.userBank;
	}
	
	public void addReadCount(int count){
		readCount += count;
	}
	
	public void setReadCount(int count) {
		readCount = count;
	}
	
	public int getReadCount(){
		return readCount;
	}
	
	public void setTime(Date time) {
		this.time = time;
	}
	
	public Date getTime() {
		return time;
	}
	
	public void setReadFrequency(int frequency) {
		readFrequency = frequency;
	}
	
	public int getReadFrequency() {
		return readFrequency;
	}
	
	public void setAntennaId(int antenna) {
		antennaId = antenna;
	}
	
	public int getAntennaId() {
		return antennaId;
	}
	
	public void reset(){
		epc = null;
		readCount = 0;
		readFrequency = 0;
		tid = null;
		userBank = null;
	}
}
