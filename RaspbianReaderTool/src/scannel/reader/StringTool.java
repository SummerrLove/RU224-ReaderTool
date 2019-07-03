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
}
