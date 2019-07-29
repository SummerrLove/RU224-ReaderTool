package scannel.reader;

import javax.xml.bind.DatatypeConverter;

public class StringTool {

	public StringTool() {
		
	}

	public static String hexToAsciiString(String hexString) {
		if (hexString == null) {
			return null;
		}
		
		return new String(DatatypeConverter.parseHexBinary(hexString));
	}
	
	public static String asciiToHexString(String string) {
		if (string == null) {
			return null;
		}
		
		String hex = "";
		byte[] bytes = string.getBytes();
		for (int i=0; i<bytes.length; i++) {
			hex += Integer.toHexString(bytes[i]);
		}
		
		return hex;
	}
	
	public static boolean isHexString(String str) {
		return str.matches("[0-9a-fA-F]+");
	}
	
	public static short[] toShortArray(String str) {
		if (str==null || !isHexString(str)) {
			MyLogger.printLog("Not hex string: "+str);
			return null;
		}
		
		if (str.length()%2 != 0) {
			str += "0";
		}
		
		short[] data = new short[str.length()/2];
		for (int i=0; (i+1)*2<str.length(); i++) {
			String temp = str.substring(i*2, (i+1)*2);
			data[i] = Short.parseShort(temp, 16);
		}
		
		return data;
	}
}
