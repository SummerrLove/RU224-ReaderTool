package scannel.reader;

import java.util.ArrayList;
import java.util.Date;

import javax.xml.bind.DatatypeConverter;

import com.thingmagic.TagReadData;

public class TagList {

	ArrayList<TagUnit> tagList;
	
	public TagList() {
		tagList = new ArrayList<TagUnit>();
	}

	public void addTag(TagUnit tu) {
		if (tagList  == null) {
			tagList = new ArrayList<TagUnit>();
		}
		
		tagList.add(tu);
	}
	
	public void addTag(String epc, int count){
		System.out.println("TagList.addTag(), epc="+epc+", count="+count);
		if (tagList == null){
			System.out.println("No tag list, so create new one and add the tag data...");
			tagList = new ArrayList<TagUnit>();
			TagUnit tag = new TagUnit(epc, count);
			tagList.add(tag);
		} else {
			TagUnit tag = this.checkList(epc);
			if (tag == null){
				System.out.println("No tag data with epc ["+epc+"] was found in the list.");
				TagUnit newTag = new TagUnit(epc, count);
				tagList.add(newTag);
			} else {
				System.out.println("Tag found! So add read count.");
				tag.addReadCount(count);
			}
		}
		System.out.println();
	}
	
	public void addTag(TagReadData trd){
//		System.out.println("TagList.addTag() with trd, epc="+trd.epcString()+", count="+trd.getReadCount()+", time="+new Date().toString()+", frequency="+trd.getFrequency());
		if (tagList == null){
//			System.out.println("No tag list, so create new one and add the tag data...");
			tagList = new ArrayList<TagUnit>();
			TagUnit tag = new TagUnit(trd.epcString(), trd.getReadCount(), trd.getFrequency(),  DatatypeConverter.printHexBinary(trd.getTIDMemData()), DatatypeConverter.printHexBinary(trd.getUserMemData()));
			tag.setTime(new Date());
			tag.setAntennaId(trd.getAntenna());
			tagList.add(tag);
		} else {
			TagUnit tag = this.checkList(trd.epcString());
			if (tag == null){
//				System.out.println("No tag data with epc ["+trd.epcString()+"] was found in the list.");
				TagUnit newTag = new TagUnit(trd.epcString(), trd.getReadCount(), trd.getFrequency(), DatatypeConverter.printHexBinary(trd.getTIDMemData()), DatatypeConverter.printHexBinary(trd.getUserMemData()));
				newTag.setTime(new Date());
				newTag.setAntennaId(trd.getAntenna());
				tagList.add(newTag);
			} else {
//				System.out.println("Tag found! So add read count and set read frequency.");
				tag.addReadCount(trd.getReadCount());
				tag.setReadFrequency(trd.getFrequency());
				tag.setAntennaId(trd.getAntenna());
				tag.setTime(new Date());
				
				if (tag.getTid().equals("") && trd.getTIDMemData() != null) {
					String tid = DatatypeConverter.printHexBinary(trd.getTIDMemData());
					MyLogger.printLog("set TID: "+tid);
					tag.setTid(tid);
				}
				
				if (tag.getUserBank().equals("") && trd.getUserMemData() != null) {
					String userBank = DatatypeConverter.printHexBinary(trd.getUserMemData());
					MyLogger.printLog("set User Bank: "+userBank);
					tag.setUserBank(userBank);
				} 
			}
		}
//		System.out.println();
	}
	
	public void removeTag(String epc){
		System.out.println("TagList.removeTag(), epc="+epc);
		if (tagList == null){
			return;
		} else {
			TagUnit tag = this.checkList(epc);
			tagList.remove(tag);
		}
	}
	
	public void reset(){
		if (tagList != null){
			tagList.clear();
		}
	}
	
	public int size(){
		if (tagList != null){
			return tagList.size();
		} else {
			return -1;
		}
	}
	
	public TagUnit get(int index){
		if (tagList != null){
			return tagList.get(index);
		} else {
			return null;
		}
	}
	
	private TagUnit checkList(String epc){
		if (tagList != null){
			for (int i=0; i<tagList.size(); i++){
				TagUnit tag = tagList.get(i);
				if (tag.getEPC().equals(epc)){
					return tag;
				}
			}
		}
		
		return null;
	}
	
	public void printListContent(){
		if (tagList != null){
			for (int i=0; i<tagList.size(); i++){
				TagUnit tag = tagList.get(i);
				System.out.println("epc=" + tag.getEPC() +", readCount=" + tag.getReadCount());
			}
		}
	}
}
