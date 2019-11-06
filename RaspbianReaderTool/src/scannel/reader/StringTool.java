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
	
	public static boolean isBinaryString(String str) {
		return str.matches("[01]+");
	}
	
	public static short[] toShortArray(String str) {
		if (str==null || !isHexString(str)) {
			MyLogger.printLog("Not hex string: "+str);
			return null;
		}
		
		// if the string cannot be divided into complete short strings, add "0" to the end of string to make it complete
		while (str.length()%4 != 0) {
			str += "0";
		}
		
		short[] data = new short[str.length()/4];
		for (int i=0; (i+1)*4<str.length(); i++) {
			String temp = str.substring(i*4, (i+1)*4);
			data[i] = Short.parseShort(temp, 16);
		}
		
		return data;
	}
	
	/**
	 * Convert a binary string into a byte array which is used as mask data is Select function. 
	 * This method add 0 to the end if the remain of the string is less than 8 digits. Therefore, the 
	 * return result may be different from regular conversion.
	 *  
	 * @param str the binary string to be converted
	 * @return the byte array
	 */
	public static byte[] toByteArray(String str) {
		if (str==null || !isBinaryString(str)) {
			return null;
		}
		
		while (str.length()%8 != 0) {
			str += "0";
		}
		
		byte[] data = DatatypeConverter.parseHexBinary(Integer.toHexString(Integer.parseInt(str, 2)));
		return data;
	}
	
	public static byte[] convertShortArraytoByteArray(short[] shortData) {
		byte[] byteData = new byte[shortData.length * 2];
		for (int i = 0; i < shortData.length; i++) {
			byteData[i * 2] = (byte) ((shortData[i] >> 8) & 0xff);
			byteData[i * 2 + 1] = (byte) ((shortData[i]) & 0xff);
		}
		
		return byteData;
	}
}
