package scannel.decoder;


import java.math.BigInteger;
import java.util.HashMap;

import scannel.reader.MyLogger;

public class GS1Decoder {

	public static enum EPC_SCHEMA{
		SGTIN_96(0),
		SGTIN_198(1),
		SSCC_96(2),
		SGLN_96(3),
		SGLN_195(4),
		GRAI_96(5),
		GRAI_170(6),
		GIAI_96(7),
		GIAI_202(8),
		GSRN_96(9),
		GSRNP(10),
		GDTI_96(11),
		GDTI_113(12),
		GDTI_174(13),
		CPI_96(14),
		CPI_var(15),
		SGCN_96(16),
		ITIP_110(17),
		ITIP_212(18),
		GID_96(19),
		USDoD_96(20),
		ADI_var(21),
		;
		
		
		private final int value;
		
		private EPC_SCHEMA(int value) {
			this.value = value;
		}
		
		public int getValue() {
			return value;
		}
	}
	
//	public final static HashMap charMap = new HashMap<String, String>();
//	static {
//		charMap.put("100011", "#");
//		charMap.put("101101", "-");
//		charMap.put("101111", "/");
//		charMap.put("110000", "0");
//		charMap.put("110001", "1");
//		charMap.put("110010", "2");
//		charMap.put("110011", "3");
//		charMap.put("110100", "4");
//		charMap.put("110101", "5");
//		charMap.put("110110", "6");
//		charMap.put("110111", "7");
//		charMap.put("111000", "8");
//		charMap.put("111001", "9");
//		charMap.put("000001", "A");
//		charMap.put("000010", "B");
//		charMap.put("000011", "C");
//		charMap.put("000100", "D");
//		charMap.put("000101", "E");
//		charMap.put("000110", "F");
//		charMap.put("000111", "G");
//		charMap.put("001000", "H");
//		charMap.put("001001", "I");
//		charMap.put("001010", "J");
//		charMap.put("001011", "K");
//		charMap.put("001100", "L");
//		charMap.put("001101", "M");
//		charMap.put("001110", "N");
//		charMap.put("001111", "O");
//		charMap.put("010000", "P");
//		charMap.put("010001", "Q");
//		charMap.put("010010", "R");
//		charMap.put("010011", "S");
//		charMap.put("010100", "T");
//		charMap.put("010101", "U");
//		charMap.put("010110", "V");
//		charMap.put("010111", "W");
//		charMap.put("011000", "X");
//		charMap.put("011001", "Y");
//		charMap.put("011010", "Z");
//	}
	
	private String hexString;
	private EPC_SCHEMA schema;
	private String header;
	private int filter;
	private int partition;
	private int encode_length;
	private int company_prefix_length;
//	private int reference_length;
	
	public GS1Decoder(String epcString) {
		hexString = epcString;
		header = epcString.substring(0, 2);
	}

	public static String parse7bitString(String binaryStr) {
		if (binaryStr.length()%7 != 0) {
			MyLogger.printLog("Incorrect string length...");
			return null;
		}
		
		int stringLength = binaryStr.length()/7;
		String temp;
		String returnStr = "";
		
		for (int i=0; i<stringLength; i++) {
			temp = binaryStr.substring(0+i*7, (i+1)*7);
			
			// Based on RFID tag standard 14.4.2 "String" Decoding Method, 
			// all 7-bit segments following an all-zero segment must also be all zeros.
			// Therefore, ignore the data after the first all-zero segment.
			if (temp.equals("0000000")) {
				break;
			}
			
			returnStr += new Character((char) Integer.parseInt(temp, 2)).toString();
		}
		
		
		return returnStr;
	}
	
	public static String parseNumericString(String binaryStr) {
		String intValue = new BigInteger(binaryStr, 2).toString();
		
		if (intValue.length() == 1) {
			return null;
		}
		
		if (intValue.startsWith("1")) {
			return intValue.substring(1);
		} else {
			return null;
		}
	}
	
	public static String parseAsciiString(String binaryStr) {
		if (binaryStr.length()%8 != 0) {
			MyLogger.printLog("Format error: The length of binary string must be a muliple of 8.");
			return null;
		}
		
		int word_count = binaryStr.length()/8;
		String returnStr = "";
		for (int i=0; i<word_count; i++) {
			String temp = binaryStr.substring(i*8, (i+1)*8);
			returnStr += new Character((char) Integer.parseInt(temp, 2)).toString();
		}
		
		return returnStr;
	}
	
	public static String parse6bitAsciiStr(String str) {
		if (str == null || str.length() != 6) {
			MyLogger.printLog("Cannot translate incorrect 6-bit ascii string...");
			return null;
		}
		
		if(str.equals("000000")) {
			return null;
		}
		
		String binary;
		if (str.startsWith("0")) {
			binary = "01"+str;
		} else if (str.startsWith("1")) {
			binary = "00"+str;
		} else {
			MyLogger.printLog("Unknown string...");
			return null;
		}
		
		return new Character((char) Integer.parseInt(binary, 2)).toString();
	}
	
	public String parseEPCString(boolean[] support_setting) {
		if (header.equals("2C") && support_setting[EPC_SCHEMA.GDTI_96.getValue()]) {
			// GDTI-96
			schema = EPC_SCHEMA.GDTI_96;
			MyLogger.printLog(schema.name());
			encode_length = 96;
			return this.parseGDTI96HexString(hexString);
		} else if (header.equals("2D") && support_setting[EPC_SCHEMA.GSRN_96.getValue()]) {
			// GSRN-96
			schema = EPC_SCHEMA.GSRN_96;
			MyLogger.printLog(schema.name());
			encode_length = 96;
			return this.parseGSRNHexString(hexString);
		} else if (header.equals("2E") && support_setting[EPC_SCHEMA.GSRNP.getValue()]) {
			// GSRNP
			schema = EPC_SCHEMA.GSRNP;
			MyLogger.printLog(schema.name());
			encode_length = 96;
			return this.parseGSRNHexString(hexString);
		} else if (header.equals("2F") && support_setting[EPC_SCHEMA.USDoD_96.getValue()]) {
			// USDoD-96
			schema = EPC_SCHEMA.USDoD_96;
			MyLogger.printLog(schema.name());
			encode_length = 96;
			return this.parseUSDOD96HexString(hexString);
		} else if (header.equals("30") && support_setting[EPC_SCHEMA.SGTIN_96.getValue()]) {
			// SGTIN-96
			schema = EPC_SCHEMA.SGTIN_96;
			MyLogger.printLog(schema.name());
			encode_length = 96;
			return this.parseSGTIN96HexString(hexString);
		} else if (header.equals("31") && support_setting[EPC_SCHEMA.SSCC_96.getValue()]) {
			// SSCC-96
			schema = EPC_SCHEMA.SSCC_96;
			MyLogger.printLog(schema.name());
			encode_length = 96;
			return this.parseSSCC96HexString(hexString);
		} else if (header.equals("32") && support_setting[EPC_SCHEMA.SGLN_96.getValue()]) {
			// SGLN-96
			schema = EPC_SCHEMA.SGLN_96;
			MyLogger.printLog(schema.name());
			encode_length = 96;
			return this.parseSGLN96HexString(hexString);
		} else if (header.equals("33") && support_setting[EPC_SCHEMA.GRAI_96.getValue()]) {
			// GRAI-96
			schema = EPC_SCHEMA.GRAI_96;
			MyLogger.printLog(schema.name());
			encode_length = 96;
			return this.parseGRAI96HexString(hexString);
		} else if (header.equals("34") && support_setting[EPC_SCHEMA.GIAI_96.getValue()]) {
			// GIAI-96
			schema = EPC_SCHEMA.GIAI_96;
			MyLogger.printLog(schema.name());
			encode_length = 96;
			return this.parseGIAI96HexString(hexString);
		} else if (header.equals("35") && support_setting[EPC_SCHEMA.GID_96.getValue()]) {
			// GID-96
			schema = EPC_SCHEMA.GID_96;
			MyLogger.printLog(schema.name());
			encode_length = 96;
			return this.parseGID96HexString(hexString);
		} else if (header.equals("36") && support_setting[EPC_SCHEMA.SGTIN_198.getValue()]) {
			// SGTIN-198
			schema = EPC_SCHEMA.SGTIN_198;
			MyLogger.printLog(schema.name());
			encode_length = 198;
			return this.parseSGTIN198HexString(hexString);
		} else if (header.equals("37") && support_setting[EPC_SCHEMA.GRAI_170.getValue()]) {
			// GRAI-170
			schema = EPC_SCHEMA.GRAI_170;
			MyLogger.printLog(schema.name());
			encode_length = 170;
			return this.parseGRAI170HexString(hexString);
		} else if (header.equals("38") && support_setting[EPC_SCHEMA.GIAI_202.getValue()]) {
			// GIAI-202
			schema = EPC_SCHEMA.GIAI_202;
			MyLogger.printLog(schema.name());
			encode_length = 202;
			return this.parseGIAI202HexString(hexString);
		} else if (header.equals("39") && support_setting[EPC_SCHEMA.SGLN_195.getValue()]) {
			// SGLN-195
			schema = EPC_SCHEMA.SGLN_195;
			MyLogger.printLog(schema.name());
			encode_length = 195;
			return this.parseSGLN195HexString(hexString);
		} else if (header.equals("3A") && support_setting[EPC_SCHEMA.GDTI_113.getValue()]) {
			// GDTI-113
			schema = EPC_SCHEMA.GDTI_113;
			MyLogger.printLog(schema.name());
			encode_length = 113;
			return this.parseGDTI113HexString(hexString);
		} else if (header.equals("3B") && support_setting[EPC_SCHEMA.ADI_var.getValue()]) {
			// ADI-var
			schema = EPC_SCHEMA.ADI_var;
			MyLogger.printLog(schema.name());
			return this.parseADIvarHexString(hexString);
		} else if (header.equals("3C") && support_setting[EPC_SCHEMA.CPI_96.getValue()]) {
			// CPI-96
			schema = EPC_SCHEMA.CPI_96;
			MyLogger.printLog(schema.name());
			encode_length = 96;
			return this.parseCPI96HexString(hexString);
		} else if (header.equals("3D") && support_setting[EPC_SCHEMA.CPI_var.getValue()]) {
			// CPI-var
			schema = EPC_SCHEMA.CPI_var;
			MyLogger.printLog(schema.name());
			return this.parseCPIvarHexString(hexString);
		} else if (header.equals("3E") && support_setting[EPC_SCHEMA.GDTI_174.getValue()]) {
			// GDTI-174
			schema = EPC_SCHEMA.GDTI_174;
			MyLogger.printLog(schema.name());
			encode_length = 174;
			return this.parseGDTI174HexString(hexString);
		} else if (header.equals("3F") && support_setting[EPC_SCHEMA.SGCN_96.getValue()]) {
			// SGCN-96
			schema = EPC_SCHEMA.SGCN_96;
			MyLogger.printLog(schema.name());
			encode_length = 96;
			return this.parseSGCN96HexString(hexString);
		} else if (header.equals("40") && support_setting[EPC_SCHEMA.ITIP_110.getValue()]) {
			// ITIP-110
			schema = EPC_SCHEMA.ITIP_110;
			MyLogger.printLog(schema.name());
			encode_length = 110;
			return this.parseITIP110HexString(hexString);
		} else if (header.equals("41") && support_setting[EPC_SCHEMA.ITIP_212.getValue()]) {
			// ITIP-212
			schema = EPC_SCHEMA.ITIP_212;
			MyLogger.printLog(schema.name());
			encode_length = 212;
			return this.parseITIP212HexString(hexString);
		} else {
			return null;
		}
	}
	
	private void parseSGTINPartition(int partition) {
		switch (partition) {
		case 0:
			company_prefix_length = 40;
//			reference_length = 4;
			break;
		case 1:
			company_prefix_length = 37;
//			reference_length = 7;
			break;
		case 2:
			company_prefix_length = 34;
//			reference_length = 10;
			break;
		case 3:
			company_prefix_length = 30;
//			reference_length = 14;
			break;
		case 4:
			company_prefix_length = 27;
//			reference_length = 17;
			break;
		case 5:
			company_prefix_length = 24;
//			reference_length = 20;
			break;
		case 6:
			company_prefix_length = 20;
//			reference_length = 24;
			break;
 		default:
 			MyLogger.printLog("Unknown partition value for SGTIN: "+ partition);
			break;
		}
	}
	
	private void parseSSCCPartition(int partition) {
		switch (partition) {
		case 0:
			company_prefix_length = 40;
//			reference_length = 18;
			break;
		case 1:
			company_prefix_length = 37;
//			reference_length = 21;
			break;
		case 2:
			company_prefix_length = 34;
//			reference_length = 24;
			break;
		case 3:
			company_prefix_length = 30;
//			reference_length = 28;
			break;
		case 4:
			company_prefix_length = 27;
//			reference_length = 31;
			break;
		case 5:
			company_prefix_length = 24;
//			reference_length = 34;
			break;
		case 6:
			company_prefix_length = 20;
//			reference_length = 38;
			break;
 		default:
 			MyLogger.printLog("Unknown partition value for SSCC: "+ partition);
			break;
		}
	}
	
	private void parseSGLNPartition(int partition) {
		switch (partition) {
		case 0:
			company_prefix_length = 40;
//			reference_length = 1;
			break;
		case 1:
			company_prefix_length = 37;
//			reference_length = 4;
			break;
		case 2:
			company_prefix_length = 34;
//			reference_length = 7;
			break;
		case 3:
			company_prefix_length = 30;
//			reference_length = 11;
			break;
		case 4:
			company_prefix_length = 27;
//			reference_length = 14;
			break;
		case 5:
			company_prefix_length = 24;
//			reference_length = 17;
			break;
		case 6:
			company_prefix_length = 20;
//			reference_length = 21;
			break;
 		default:
 			MyLogger.printLog("Unknown partition value for SGLN: "+ partition);
			break;
		}
	}
	
	private void parseGRAIPartition(int partition) {
		switch (partition) {
		case 0:
			company_prefix_length = 40;
//			reference_length = 4;
			break;
		case 1:
			company_prefix_length = 37;
//			reference_length = 7;
			break;
		case 2:
			company_prefix_length = 34;
//			reference_length = 10;
			break;
		case 3:
			company_prefix_length = 30;
//			reference_length = 14;
			break;
		case 4:
			company_prefix_length = 27;
//			reference_length = 17;
			break;
		case 5:
			company_prefix_length = 24;
//			reference_length = 20;
			break;
		case 6:
			company_prefix_length = 20;
//			reference_length = 24;
			break;
 		default:
 			MyLogger.printLog("Unknown partition value for GRAI: "+ partition);
			break;
		}
	}
	
	private void parseGIAI96Partition(int partition) {
		switch (partition) {
		case 0:
			company_prefix_length = 40;
//			reference_length = 42;
			break;
		case 1:
			company_prefix_length = 37;
//			reference_length = 45;
			break;
		case 2:
			company_prefix_length = 34;
//			reference_length = 48;
			break;
		case 3:
			company_prefix_length = 30;
//			reference_length = 52;
			break;
		case 4:
			company_prefix_length = 27;
//			reference_length = 55;
			break;
		case 5:
			company_prefix_length = 24;
//			reference_length = 58;
			break;
		case 6:
			company_prefix_length = 20;
//			reference_length = 62;
			break;
 		default:
 			MyLogger.printLog("Unknown partition value for GIAI: "+ partition);
			break;
		}
	}
	
	private void parseGIAI202Partition(int partition) {
		switch (partition) {
		case 0:
			company_prefix_length = 40;
//			reference_length = 148;
			break;
		case 1:
			company_prefix_length = 37;
//			reference_length = 151;
			break;
		case 2:
			company_prefix_length = 34;
//			reference_length = 154;
			break;
		case 3:
			company_prefix_length = 30;
//			reference_length = 158;
			break;
		case 4:
			company_prefix_length = 27;
//			reference_length = 161;
			break;
		case 5:
			company_prefix_length = 24;
//			reference_length = 164;
			break;
		case 6:
			company_prefix_length = 20;
//			reference_length = 168;
			break;
 		default:
 			MyLogger.printLog("Unknown partition value for GIAI: "+ partition);
			break;
		}
	}
	
	private void parseGSRNPartition(int partition) {
		switch (partition) {
		case 0:
			company_prefix_length = 40;
//			reference_length = 18;
			break;
		case 1:
			company_prefix_length = 37;
//			reference_length = 21;
			break;
		case 2:
			company_prefix_length = 34;
//			reference_length = 24;
			break;
		case 3:
			company_prefix_length = 30;
//			reference_length = 28;
			break;
		case 4:
			company_prefix_length = 27;
//			reference_length = 31;
			break;
		case 5:
			company_prefix_length = 24;
//			reference_length = 34;
			break;
		case 6:
			company_prefix_length = 20;
//			reference_length = 38;
			break;
 		default:
 			MyLogger.printLog("Unknown partition value for GSRN: "+ partition);
			break;
		}
	}
	
	private void parseGDTIPartition(int partition) {
		switch (partition) {
		case 0:
			company_prefix_length = 40;
//			reference_length = 1;
			break;
		case 1:
			company_prefix_length = 37;
//			reference_length = 4;
			break;
		case 2:
			company_prefix_length = 34;
//			reference_length = 7;
			break;
		case 3:
			company_prefix_length = 30;
//			reference_length = 11;
			break;
		case 4:
			company_prefix_length = 27;
//			reference_length = 14;
			break;
		case 5:
			company_prefix_length = 24;
//			reference_length = 17;
			break;
		case 6:
			company_prefix_length = 20;
//			reference_length = 21;
			break;
 		default:
 			MyLogger.printLog("Unknown partition value for GDTI: "+ partition);
			break;
		}
	}
	
	private void parseCPIPartition(int partition) {
		switch (partition) {
		case 0:
			company_prefix_length = 40;
//			reference_length = 11;
			break;
		case 1:
			company_prefix_length = 37;
//			reference_length = 14;
			break;
		case 2:
			company_prefix_length = 34;
//			reference_length = 17;
			break;
		case 3:
			company_prefix_length = 30;
//			reference_length = 21;
			break;
		case 4:
			company_prefix_length = 27;
//			reference_length = 24;
			break;
		case 5:
			company_prefix_length = 24;
//			reference_length = 27;
			break;
		case 6:
			company_prefix_length = 20;
//			reference_length = 31;
			break;
 		default:
 			MyLogger.printLog("Unknown partition value for CPI: "+ partition);
			break;
		}
	}
	
	private void parseCPIvarPartition(int partition) {
		switch (partition) {
		case 0:
			company_prefix_length = 40;
			break;
		case 1:
			company_prefix_length = 37;
			break;
		case 2:
			company_prefix_length = 34;
			break;
		case 3:
			company_prefix_length = 30;
			break;
		case 4:
			company_prefix_length = 27;
			break;
		case 5:
			company_prefix_length = 24;
			break;
		case 6:
			company_prefix_length = 20;
			break;
 		default:
 			MyLogger.printLog("Unknown partition value for CPI-var: "+ partition);
			break;
		}
	}
	
	private void parseSGCNPartition(int partition) {
		switch (partition) {
		case 0:
			company_prefix_length = 40;
//			reference_length = 1;
			break;
		case 1:
			company_prefix_length = 37;
//			reference_length = 4;
			break;
		case 2:
			company_prefix_length = 34;
//			reference_length = 7;
			break;
		case 3:
			company_prefix_length = 30;
//			reference_length = 11;
			break;
		case 4:
			company_prefix_length = 27;
//			reference_length = 14;
			break;
		case 5:
			company_prefix_length = 24;
//			reference_length = 17;
			break;
		case 6:
			company_prefix_length = 20;
//			reference_length = 21;
			break;
 		default:
 			MyLogger.printLog("Unknown partition value for SGCN: "+ partition);
			break;
		}
	}
	
	private void parseITIPPartition(int partition) {
		switch (partition) {
		case 0:
			company_prefix_length = 40;
//			reference_length = 4;
			break;
		case 1:
			company_prefix_length = 37;
//			reference_length = 7;
			break;
		case 2:
			company_prefix_length = 34;
//			reference_length = 10;
			break;
		case 3:
			company_prefix_length = 30;
//			reference_length = 14;
			break;
		case 4:
			company_prefix_length = 27;
//			reference_length = 17;
			break;
		case 5:
			company_prefix_length = 24;
//			reference_length = 20;
			break;
		case 6:
			company_prefix_length = 20;
//			reference_length = 24;
			break;
 		default:
 			MyLogger.printLog("Unknown partition value for ITIP: "+ partition);
			break;
		}
	}
	
	private String parseSGTIN96HexString(String epcString) {
		if (epcString.length() != 24) {
			MyLogger.printLog("Incorrect data length for sgtin-96 hexString.");
			return null;
		}
		
		String binaryStr =String.format("%96s", new BigInteger(epcString,16).toString(2)).replace(" ", "0");
		MyLogger.printLog(binaryStr);
		
		String str_header = binaryStr.substring(0, 8);
		String str_filterValue = binaryStr.substring(8, 11);
		filter = Integer.parseInt(str_filterValue, 2);
		String str_partitionValue = binaryStr.substring(11, 14);
		partition = Integer.parseInt(str_partitionValue, 2);
		this.parseSGTINPartition(partition);
		
		// digit length for company prefix
		int cp_digit_length = 12 - partition;
		// digit length for item reference
		int ir_digit_length = partition + 1;
		
		String temp = binaryStr.substring(14, 58);
		String str_companyPrefix = String.format("%"+cp_digit_length+"s", new BigInteger(temp.substring(0, company_prefix_length), 2).toString(10)).replace(" ", "0");
		MyLogger.printLog(str_companyPrefix);
		String str_itemReference = String.format("%"+ir_digit_length+"s", new BigInteger(temp.substring(company_prefix_length), 2).toString(10)).replace(" ", "0");
		MyLogger.printLog(str_itemReference);
		
		String gtin_temp = str_itemReference.substring(0, 1)
							+ str_companyPrefix
							+ str_itemReference.substring(1);
		
		char[] chars = gtin_temp.toCharArray();
		if (chars.length != 13) {
			MyLogger.printLog("Format error...");
			return null;
		} else {
			MyLogger.printLog(gtin_temp);
			
			int checksum = 0;
			int index = 0;
			for (int i=0; i<chars.length; i++) {
				index = (chars.length - 1) - i ;
				if (i%2 == 0) {
					checksum += Character.getNumericValue(chars[index]) * 3;
				} else if (i%2 == 1) {
					checksum += Character.getNumericValue(chars[index]);
				}
				
			}
			
			int check_bit = 0;
			if (checksum%10 > 0) {
				check_bit = 10 - (checksum%10);
			}
			String str_GTIN14 = gtin_temp+check_bit;
			MyLogger.printLog(str_GTIN14);
			
			// the last 38 bits are serial number
			String str_serialNumber = binaryStr.substring(58, 96);
			String serialNumber =String.format("%12s", new BigInteger(str_serialNumber, 2).toString(10)).replace(" ", "0");
			MyLogger.printLog(str_GTIN14+"." + serialNumber);
			
			return str_GTIN14+"."+serialNumber;
		}
	}
	
	private String parseSGTIN198HexString(String epcString) {
		if (epcString.length() != 50) {
			MyLogger.printLog("Incorrect data length for sgtin-198 hexString.");
			return null;
		}
		
		
		String binaryStr =String.format("%200s", new BigInteger(epcString,16).toString(2)).replace(" ", "0").substring(0, 198);
		MyLogger.printLog(binaryStr);
		
		String str_header = binaryStr.substring(0, 8);
		String str_filterValue = binaryStr.substring(8, 11);
		filter = Integer.parseInt(str_filterValue, 2);
		String str_partitionValue = binaryStr.substring(11, 14);
		partition = Integer.parseInt(str_partitionValue, 2);
		this.parseSGTINPartition(partition);
		
		// digit length for company prefix
		int cp_digit_length = 12 - partition;
		// digit length for item reference
		int im_digit_length = partition + 1;
		
		String temp = binaryStr.substring(14, 58);
		String str_companyPrefix = String.format("%0"+cp_digit_length+"d", Integer.parseInt(temp.substring(0, company_prefix_length), 2));
		MyLogger.printLog(str_companyPrefix);
		String str_itemReference = String.format("%0"+im_digit_length+"d", Integer.parseInt(temp.substring(company_prefix_length, 44), 2));
		MyLogger.printLog(str_itemReference);
		
		String gtin_temp = str_itemReference.substring(0, 1)
							+ str_companyPrefix
							+ str_itemReference.substring(1);
		
		char[] chars = gtin_temp.toCharArray();
		if (chars.length != 13) {
			MyLogger.printLog("Format error...");
			return null;
		} else {
			System.out.println(gtin_temp);
			
			int checksum = 0;
			int index = 0;
			for (int i=0; i<chars.length; i++) {
				index = (chars.length - 1) - i ;
				if (i%2 == 0) {
					checksum += Character.getNumericValue(chars[index]) * 3;
				} else if (i%2 == 1) {
					checksum += Character.getNumericValue(chars[index]);
				}
				
			}
			
			int check_bit = 0;
			if (checksum%10 > 0) {
				check_bit = 10 - (checksum%10);
			}
			String str_GTIN = gtin_temp+check_bit;
			
			MyLogger.printLog(str_GTIN);
			String str_serialNumber = binaryStr.substring(58);
			String serialNumber = parse7bitString(str_serialNumber);
			MyLogger.printLog(str_GTIN+"." + serialNumber);
			
			return str_GTIN+"."+serialNumber;
		}
	}
	
	private String parseSSCC96HexString(String epcString) {
		if (epcString.length() != 24) {
			MyLogger.printLog("Incorrect data length for SSCC-96 hexString.");
			return null;
		}
		
		
		String binaryStr =String.format("%96s", new BigInteger(epcString,16).toString(2)).replace(" ", "0");
		MyLogger.printLog(binaryStr);
		
		String str_header = binaryStr.substring(0, 8);
		String str_filterValue = binaryStr.substring(8, 11);
		filter = Integer.parseInt(str_filterValue, 2);
		String str_partitionValue = binaryStr.substring(11, 14);
		partition = Integer.parseInt(str_partitionValue, 2);
		this.parseSSCCPartition(partition);
		
		// digit length for company prefix
		int cp_digit_length = 12 - partition;
		// digit length for serial reference
		int sr_digit_length = partition + 5;
		
		String temp = binaryStr.substring(14, 72);
		String str_companyPrefix = String.format("%"+cp_digit_length+"s", new BigInteger(temp.substring(0, company_prefix_length), 2).toString(10)).replace(" ", "0");
		MyLogger.printLog("company prefix: "+str_companyPrefix);
		String str_serialReference = String.format("%"+sr_digit_length+"s", new BigInteger(temp.substring(company_prefix_length), 2).toString(10)).replace(" ", "0");
		MyLogger.printLog("serial reference: "+str_serialReference);
		
		String sscc_temp = str_serialReference.substring(0, 1)
							+ str_companyPrefix
							+ str_serialReference.substring(1);
		
		char[] chars = sscc_temp.toCharArray();
		if (chars.length != 17) {
			MyLogger.printLog("Format error...");
			return null;
		} else {
			MyLogger.printLog(sscc_temp);
			
			int checksum = 0;
			int index = 0;
			for (int i=0; i<chars.length; i++) {
				index = (chars.length - 1) - i ;
				if (i%2 == 0) {
					checksum += Character.getNumericValue(chars[index]) * 3;
				} else if (i%2 == 1) {
					checksum += Character.getNumericValue(chars[index]);
				}
				
			}
			
			int check_bit = 0;
			if (checksum%10 > 0) {
				check_bit = 10 - (checksum%10);
			}
			String str_SSCC = sscc_temp+check_bit;
			MyLogger.printLog(str_SSCC);
			
			// the last 24 bits of data are reserved bits, which are all zero bits
			String str_reserved = binaryStr.substring(72);
			
			return str_SSCC;
		}
	}
	
	private String parseSGLN96HexString(String epcString) {
		if (epcString.length() != 24) {
			MyLogger.printLog("Incorrect data length for SGLN-96 hexString.");
			return null;
		}
		
		
		String binaryStr =String.format("%96s", new BigInteger(epcString,16).toString(2)).replace(" ", "0");
		MyLogger.printLog(binaryStr);
		
		String str_header = binaryStr.substring(0, 8);
		String str_filterValue = binaryStr.substring(8, 11);
		filter = Integer.parseInt(str_filterValue, 2);
		String str_partitionValue = binaryStr.substring(11, 14);
		partition = Integer.parseInt(str_partitionValue, 2);
		this.parseSGLNPartition(partition);
		
		// digit length for company prefix
		int cp_digit_length = 12 - partition;
		// digit length for location reference
		int lr_digit_length = partition;
		
		String temp = binaryStr.substring(14, 55);
		String str_companyPrefix = String.format("%"+cp_digit_length+"s", new BigInteger(temp.substring(0, company_prefix_length), 2).toString()).replace(" ", "0");
		MyLogger.printLog("company prefix: "+str_companyPrefix);
		String str_locationReference = String.format("%"+lr_digit_length+"s", new BigInteger(temp.substring(company_prefix_length), 2).toString()).replace(" ", "0");
		MyLogger.printLog("location reference: "+str_locationReference);
		
		String sgln_temp = str_companyPrefix + str_locationReference;
		
		char[] chars = sgln_temp.toCharArray();
		if (chars.length != 12) {
			MyLogger.printLog("Format error...");
			return null;
		} else {
			MyLogger.printLog(sgln_temp);
			
			int checksum = 0;
			int index = 0;
			for (int i=0; i<chars.length; i++) {
				index = (chars.length - 1) - i ;
				if (i%2 == 0) {
					checksum += Character.getNumericValue(chars[index]) * 3;
				} else if (i%2 == 1) {
					checksum += Character.getNumericValue(chars[index]);
				}
				
			}
			
			int check_bit = 0;
			if (checksum%10 > 0) {
				check_bit = 10 - (checksum%10);
			}
			String str_SGLN = sgln_temp+check_bit;
			MyLogger.printLog(str_SGLN);
			
			// 41-bit extension
			String str_extension = binaryStr.substring(55, 96);
			String extension = new BigInteger(str_extension, 2).toString();
			MyLogger.printLog(str_SGLN+"." + extension);
			
			return str_SGLN+"."+extension;
		}
	}
	
	private String parseSGLN195HexString(String epcString) {
		if (epcString.length() != 49) {
			MyLogger.printLog("Incorrect data length for SGLN-195 hexString.");
			return null;
		}
		
		
		String binaryStr =String.format("%196s", new BigInteger(epcString,16).toString(2)).replace(" ", "0").substring(0, 195);
		MyLogger.printLog(binaryStr);
		
		String str_header = binaryStr.substring(0, 8);
		String str_filterValue = binaryStr.substring(8, 11);
		filter = Integer.parseInt(str_filterValue, 2);
		String str_partitionValue = binaryStr.substring(11, 14);
		partition = Integer.parseInt(str_partitionValue, 2);
		this.parseSGLNPartition(partition);
		
		// digit length for company prefix
		int cp_digit_length = 12 - partition;
		// digit length for location reference
		int lr_digit_length = partition;
		
		String temp = binaryStr.substring(14, 55);
		String str_companyPrefix = String.format("%"+cp_digit_length+"s", new BigInteger(temp.substring(0, company_prefix_length), 2).toString()).replace(" ", "0");
		MyLogger.printLog("company prefix: "+str_companyPrefix);
		String str_locationReference = String.format("%"+lr_digit_length+"s", new BigInteger(temp.substring(company_prefix_length), 2).toString()).replace(" ", "0");
		MyLogger.printLog("location reference: "+str_locationReference);
		
		String sgln_temp = str_companyPrefix + str_locationReference;
		
		char[] chars = sgln_temp.toCharArray();
		if (chars.length != 12) {
			MyLogger.printLog("Format error...");
			return null;
		} else {
			MyLogger.printLog(sgln_temp);
			
			int checksum = 0;
			int index = 0;
			for (int i=0; i<chars.length; i++) {
				index = (chars.length - 1) - i ;
				if (i%2 == 0) {
					checksum += Character.getNumericValue(chars[index]) * 3;
				} else if (i%2 == 1) {
					checksum += Character.getNumericValue(chars[index]);
				}
				
			}
			
			int check_bit = 0;
			if (checksum%10 > 0) {
				check_bit = 10 - (checksum%10);
			}
			String str_SGLN = sgln_temp+check_bit;
			MyLogger.printLog(str_SGLN);
			
			// 140-bit extension
			String str_extension = binaryStr.substring(55);
			String extension = parse7bitString(str_extension);
			MyLogger.printLog(str_SGLN+"." + extension);
			
			return str_SGLN+"."+extension;
		}
	}
	
	private String parseGRAI96HexString(String epcString) {
		if (epcString.length() != 24) {
			MyLogger.printLog("Incorrect data length for GRAI-96 hexString.");
			return null;
		}
		
		
		String binaryStr =String.format("%96s", new BigInteger(epcString,16).toString(2)).replace(" ", "0");
		MyLogger.printLog(binaryStr);
		
		String str_header = binaryStr.substring(0, 8);
		String str_filterValue = binaryStr.substring(8, 11);
		filter = Integer.parseInt(str_filterValue, 2);
		String str_partitionValue = binaryStr.substring(11, 14);
		partition = Integer.parseInt(str_partitionValue, 2);
		this.parseGRAIPartition(partition);
		
		// digit length for company prefix
		int cp_digit_length = 12 - partition;
		// digit length for asset type
		int at_digit_length = partition;
		
		String temp = binaryStr.substring(14, 58);
		String str_companyPrefix = String.format("%"+cp_digit_length+"s", new BigInteger(temp.substring(0, company_prefix_length), 2).toString()).replace(" ", "0");
		MyLogger.printLog("company prefix: "+str_companyPrefix);
		String str_assetType = String.format("%"+at_digit_length+"s", new BigInteger(temp.substring(company_prefix_length), 2).toString()).replace(" ", "0");
		MyLogger.printLog("asset type: "+str_assetType);
		
		String grai_temp = str_companyPrefix + str_assetType;
		
		char[] chars = grai_temp.toCharArray();
		if (chars.length != 12) {
			MyLogger.printLog("Format error...");
			return null;
		} else {
			MyLogger.printLog(grai_temp);
			
			int checksum = 0;
			int index = 0;
			for (int i=0; i<chars.length; i++) {
				index = (chars.length - 1) - i ;
				if (i%2 == 0) {
					checksum += Character.getNumericValue(chars[index]) * 3;
				} else if (i%2 == 1) {
					checksum += Character.getNumericValue(chars[index]);
				}
				
			}
			
			int check_bit = 0;
			if (checksum%10 > 0) {
				check_bit = 10 - (checksum%10);
			}
			String str_GRAI = grai_temp+check_bit;
			MyLogger.printLog(str_GRAI);
			
			// 38-bit serial number
			String str_serial = binaryStr.substring(58, 96);
			String serial = new BigInteger(str_serial, 2).toString();
			MyLogger.printLog(str_GRAI+"." + serial);
			
			return str_GRAI+"."+serial;
		}
	}
	
	private String parseGRAI170HexString(String epcString) {
		if (epcString.length() != 43) {
			MyLogger.printLog("Incorrect data length for GRAI-170 hexString.");
			return null;
		}
		
		String binaryStr =String.format("%172s", new BigInteger(epcString,16).toString(2)).replace(" ", "0").substring(0,  170);
		MyLogger.printLog(binaryStr);
		
		String str_header = binaryStr.substring(0, 8);
		String str_filterValue = binaryStr.substring(8, 11);
		filter = Integer.parseInt(str_filterValue, 2);
		String str_partitionValue = binaryStr.substring(11, 14);
		partition = Integer.parseInt(str_partitionValue, 2);
		this.parseGRAIPartition(partition);
		
		// digit length for company prefix
		int cp_digit_length = 12 - partition;
		// digit length for asset type
		int at_digit_length = partition;
		
		String temp = binaryStr.substring(14, 58);
		String str_companyPrefix = String.format("%"+cp_digit_length+"s", new BigInteger(temp.substring(0, company_prefix_length), 2).toString()).replace(" ", "0");
		MyLogger.printLog("company prefix: "+str_companyPrefix);
		String str_assetType = String.format("%"+at_digit_length+"s", new BigInteger(temp.substring(company_prefix_length), 2).toString()).replace(" ", "0");
		MyLogger.printLog("asset type: "+str_assetType);
		
		String grai_temp = str_companyPrefix + str_assetType;
		
		char[] chars = grai_temp.toCharArray();
		if (chars.length != 12) {
			MyLogger.printLog("Format error...");
			return null;
		} else {
			MyLogger.printLog(grai_temp);
			
			int checksum = 0;
			int index = 0;
			for (int i=0; i<chars.length; i++) {
				index = (chars.length - 1) - i ;
				if (i%2 == 0) {
					checksum += Character.getNumericValue(chars[index]) * 3;
				} else if (i%2 == 1) {
					checksum += Character.getNumericValue(chars[index]);
				}
				
			}
			
			int check_bit = 0;
			if (checksum%10 > 0) {
				check_bit = 10 - (checksum%10);
			}
			String str_GRAI = grai_temp+check_bit;
			MyLogger.printLog(str_GRAI);
			
			// 112-bit serial number
			String str_serial = binaryStr.substring(58);
			String serial = parse7bitString(str_serial);
			MyLogger.printLog(str_GRAI+"." + serial);
			
			return str_GRAI+"."+serial;
		}
	}
	
	private String parseGIAI96HexString(String epcString) {
		if (epcString.length() != 24) {
			MyLogger.printLog("Incorrect data length for GIAI-96 hexString.");
			return null;
		}
		
		
		String binaryStr =String.format("%96s", new BigInteger(epcString,16).toString(2)).replace(" ", "0");
		MyLogger.printLog(binaryStr);
		
		String str_header = binaryStr.substring(0, 8);
		String str_filterValue = binaryStr.substring(8, 11);
		filter = Integer.parseInt(str_filterValue, 2);
		String str_partitionValue = binaryStr.substring(11, 14);
		partition = Integer.parseInt(str_partitionValue, 2);
		this.parseGIAI96Partition(partition);
		
		// digit length for company prefix
		int cp_digit_length = 12 - partition;
		// max digit length for asset reference
		int ar_digit_length_max = partition + 13;
		
		String temp = binaryStr.substring(14, 96);
		String str_companyPrefix = String.format("%"+cp_digit_length+"s", new BigInteger(temp.substring(0, company_prefix_length), 2).toString()).replace(" ", "0");
		MyLogger.printLog("company prefix: "+str_companyPrefix);
		String str_assetReference = new BigInteger(temp.substring(company_prefix_length), 2).toString();
		MyLogger.printLog("asset reference: "+str_assetReference);
		if (str_assetReference.length() > ar_digit_length_max) {
			MyLogger.printLog("length of asset reference exceed the max digit limit...");
			return null;
		}
		
		String giai_temp = str_companyPrefix + str_assetReference;
		
		char[] chars = giai_temp.toCharArray();
		if (chars.length > 25) {
			MyLogger.printLog("Format error...");
			return null;
		} else {
			MyLogger.printLog(giai_temp);
			
//			int checksum = 0;
//			int index = 0;
//			for (int i=0; i<chars.length; i++) {
//				index = (chars.length - 1) - i ;
//				if (i%2 == 0) {
//					checksum += Character.getNumericValue(chars[index]) * 3;
//				} else if (i%2 == 1) {
//					checksum += Character.getNumericValue(chars[index]);
//				}
//				
//			}
//			
//			int check_bit = 0;
//			if (checksum%10 > 0) {
//				check_bit = 10 - (checksum%10);
//			}
//			String str_GIAI96 = giai_temp+check_bit;
			String str_GIAI96 = giai_temp;
			MyLogger.printLog(str_GIAI96);
			
			
			return str_GIAI96;
		}
	}
	
	private String parseGIAI202HexString(String epcString) {
		if (epcString.length() != 51) {
			MyLogger.printLog("Incorrect data length for GIAI-202 hexString.");
			return null;
		}
		
		String binaryStr =String.format("%204s", new BigInteger(epcString,16).toString(2)).replace(" ", "0").substring(0, 202);
		MyLogger.printLog(binaryStr);
		
		String str_header = binaryStr.substring(0, 8);
		String str_filterValue = binaryStr.substring(8, 11);
		filter = Integer.parseInt(str_filterValue, 2);
		String str_partitionValue = binaryStr.substring(11, 14);
		partition = Integer.parseInt(str_partitionValue, 2);
		this.parseGIAI202Partition(partition);
		
		// digit length for company prefix
		int cp_digit_length = 12 - partition;
		// max digit length for asset type
		int at_digit_length_max = partition + 18;
		
		String temp = binaryStr.substring(14);
		String str_companyPrefix = String.format("%"+cp_digit_length+"s", new BigInteger(temp.substring(0, company_prefix_length), 2).toString()).replace(" ", "0");
		MyLogger.printLog("company prefix: "+str_companyPrefix);
		String str_assetReference = parse7bitString(temp.substring(company_prefix_length));
		MyLogger.printLog("asset reference: "+str_assetReference);
		if (str_assetReference.length() > at_digit_length_max) {
			MyLogger.printLog("length of asset reference exceed the max digit limit...");
			return null;
		}
		
		return str_companyPrefix + str_assetReference;
	}
	
	private String parseGSRNHexString(String epcString) {
		if (epcString.length() != 24) {
			MyLogger.printLog("Incorrect data length for GSRN hexString.");
			return null;
		}
		
		
		String binaryStr =String.format("%96s", new BigInteger(epcString,16).toString(2)).replace(" ", "0");
		MyLogger.printLog(binaryStr);
		
		String str_header = binaryStr.substring(0, 8);
		String str_filterValue = binaryStr.substring(8, 11);
		filter = Integer.parseInt(str_filterValue, 2);
		String str_partitionValue = binaryStr.substring(11, 14);
		partition = Integer.parseInt(str_partitionValue, 2);
		this.parseGSRNPartition(partition);
		
		// digit length for company prefix
		int cp_digit_length = 12 - partition;
		// digit length for service reference
		int sr_digit_length = partition + 5;
		
		String temp = binaryStr.substring(14, 72);
		String str_companyPrefix = String.format("%"+cp_digit_length+"s", new BigInteger(temp.substring(0, company_prefix_length), 2).toString()).replace(" ", "0");
		MyLogger.printLog("company prefix: "+str_companyPrefix);
		String str_serviceReference = String.format("%"+sr_digit_length+"s", new BigInteger(temp.substring(company_prefix_length), 2).toString()).replace(" ", "0");
		MyLogger.printLog("service reference: "+str_serviceReference);
		
		String gsrn_temp = str_companyPrefix + str_serviceReference;
		
		char[] chars = gsrn_temp.toCharArray();
		if (chars.length != 17) {
			MyLogger.printLog("Format error...");
			return null;
		} else {
			System.out.println(gsrn_temp);
			
			int checksum = 0;
			int index = 0;
			for (int i=0; i<chars.length; i++) {
				index = (chars.length - 1) - i ;
				if (i%2 == 0) {
					checksum += Character.getNumericValue(chars[index]) * 3;
				} else if (i%2 == 1) {
					checksum += Character.getNumericValue(chars[index]);
				}
				
			}
			
			int check_bit = 0;
			if (checksum%10 > 0) {
				check_bit = 10 - (checksum%10);
			}
			String str_GSRN = gsrn_temp+check_bit;
			MyLogger.printLog(str_GSRN);
			
			
			return str_GSRN;
		}
	}
	
	private String parseGDTI96HexString(String epcString) {
		if (epcString.length() != 24) {
			MyLogger.printLog("Incorrect data length for GDTI-96 hexString.");
			return null;
		}
		
		
		String binaryStr =String.format("%96s", new BigInteger(epcString,16).toString(2)).replace(" ", "0");
		MyLogger.printLog(binaryStr);
		
		String str_header = binaryStr.substring(0, 8);
		String str_filterValue = binaryStr.substring(8, 11);
		filter = Integer.parseInt(str_filterValue, 2);
		String str_partitionValue = binaryStr.substring(11, 14);
		partition = Integer.parseInt(str_partitionValue, 2);
		this.parseGDTIPartition(partition);
		
		// digit length for company prefix
		int cp_digit_length = 12 - partition;
		// digit length for document type
		int dt_digit_length = partition;
		
		String temp = binaryStr.substring(14, 55);
		String str_companyPrefix = String.format("%"+cp_digit_length+"s", new BigInteger(temp.substring(0, company_prefix_length), 2).toString()).replace(" ", "0");
		MyLogger.printLog("company prefix: "+str_companyPrefix);
		String str_documentType = String.format("%"+dt_digit_length+"s", new BigInteger(temp.substring(company_prefix_length), 2).toString()).replace(" ", "0");
		MyLogger.printLog("document type: "+str_documentType);
		
		String gdti_temp = str_companyPrefix + str_documentType;
		
		char[] chars = gdti_temp.toCharArray();
		if (chars.length != 12) {
			MyLogger.printLog("Format error...");
			return null;
		} else {
			MyLogger.printLog(gdti_temp);
			
			int checksum = 0;
			int index = 0;
			for (int i=0; i<chars.length; i++) {
				index = (chars.length - 1) - i ;
				if (i%2 == 0) {
					checksum += Character.getNumericValue(chars[index]) * 3;
				} else if (i%2 == 1) {
					checksum += Character.getNumericValue(chars[index]);
				}
				
			}
			
			int check_bit = 0;
			if (checksum%10 > 0) {
				check_bit = 10 - (checksum%10);
			}
			String str_GDTI = gdti_temp+check_bit;
			MyLogger.printLog(str_GDTI);
			
			// 41-bit serial
			String str_serial = binaryStr.substring(55, 96);
			String serial = new BigInteger(str_serial, 2).toString();
			MyLogger.printLog(str_GDTI+"." + serial);
			
			return str_GDTI+"."+serial;
		}
	}
	
	private String parseGDTI113HexString(String epcString) {
		if (epcString.length() != 29) {
			MyLogger.printLog("Incorrect data length for GDTI-113 hexString.");
			return null;
		}
		
		
		String binaryStr =String.format("%116s", new BigInteger(epcString,16).toString(2)).replace(" ", "0").substring(0, 113);
		MyLogger.printLog(binaryStr);
		
		String str_header = binaryStr.substring(0, 8);
		String str_filterValue = binaryStr.substring(8, 11);
		filter = Integer.parseInt(str_filterValue, 2);
		String str_partitionValue = binaryStr.substring(11, 14);
		partition = Integer.parseInt(str_partitionValue, 2);
		this.parseGDTIPartition(partition);
		
		// digit length for company prefix
		int cp_digit_length = 12 - partition;
		// digit length for document type
		int dt_digit_length = partition;
		
		String temp = binaryStr.substring(14, 55);
		String str_companyPrefix = String.format("%"+cp_digit_length+"s", new BigInteger(temp.substring(0, company_prefix_length), 2).toString()).replace(" ", "0");
		MyLogger.printLog("company prefix: "+str_companyPrefix);
		String str_documentType = String.format("%"+dt_digit_length+"s", new BigInteger(temp.substring(company_prefix_length), 2).toString()).replace(" ", "0");
		MyLogger.printLog("document type: "+str_documentType);
		
		String gdti_temp = str_companyPrefix + str_documentType;
		
		char[] chars = gdti_temp.toCharArray();
		if (chars.length != 12) {
			MyLogger.printLog("Format error...");
			return null;
		} else {
			MyLogger.printLog(gdti_temp);
			
			int checksum = 0;
			int index = 0;
			for (int i=0; i<chars.length; i++) {
				index = (chars.length - 1) - i ;
				if (i%2 == 0) {
					checksum += Character.getNumericValue(chars[index]) * 3;
				} else if (i%2 == 1) {
					checksum += Character.getNumericValue(chars[index]);
				}
				
			}
			
			int check_bit = 0;
			if (checksum%10 > 0) {
				check_bit = 10 - (checksum%10);
			}
			String str_GDTI = gdti_temp+check_bit;
			MyLogger.printLog(str_GDTI);
			
			// 58-bit serial
			String str_serial = binaryStr.substring(55);
			String serial = parseNumericString(str_serial);
			MyLogger.printLog(str_GDTI+"." + serial);
			
			return str_GDTI+"."+serial;
		}
	}
	
	private String parseGDTI174HexString(String epcString) {
		if (epcString.length() != 44) {
			MyLogger.printLog("Incorrect data length for GDTI-174 hexString.");
			return null;
		}
		
		
		String binaryStr =String.format("%176s", new BigInteger(epcString,16).toString(2)).replace(" ", "0").substring(0, 174);
		MyLogger.printLog(binaryStr);
		
		String str_header = binaryStr.substring(0, 8);
		String str_filterValue = binaryStr.substring(8, 11);
		filter = Integer.parseInt(str_filterValue, 2);
		String str_partitionValue = binaryStr.substring(11, 14);
		partition = Integer.parseInt(str_partitionValue, 2);
		this.parseGDTIPartition(partition);
		
		// digit length for company prefix
		int cp_digit_length = 12 - partition;
		// digit length for document type
		int dt_digit_length = partition;
		
		String temp = binaryStr.substring(14, 55);
		String str_companyPrefix = String.format("%"+cp_digit_length+"s", new BigInteger(temp.substring(0, company_prefix_length), 2).toString()).replace(" ", "0");
		MyLogger.printLog("company prefix: "+str_companyPrefix);
		String str_documentType = String.format("%"+dt_digit_length+"s", new BigInteger(temp.substring(company_prefix_length), 2).toString()).replace(" ", "0");
		MyLogger.printLog("document type: "+str_documentType);
		
		String gdti_temp = str_companyPrefix + str_documentType;
		
		char[] chars = gdti_temp.toCharArray();
		if (chars.length != 12) {
			MyLogger.printLog("Format error...");
			return null;
		} else {
			MyLogger.printLog(gdti_temp);
			
			int checksum = 0;
			int index = 0;
			for (int i=0; i<chars.length; i++) {
				index = (chars.length - 1) - i ;
				if (i%2 == 0) {
					checksum += Character.getNumericValue(chars[index]) * 3;
				} else if (i%2 == 1) {
					checksum += Character.getNumericValue(chars[index]);
				}
				
			}
			
			int check_bit = 0;
			if (checksum%10 > 0) {
				check_bit = 10 - (checksum%10);
			}
			String str_GDTI = gdti_temp+check_bit;
			MyLogger.printLog(str_GDTI);
			
			// 119-bit serial
			String str_serial = binaryStr.substring(55);
			String serial = parse7bitString(str_serial);
			MyLogger.printLog(str_GDTI+"." + serial);
			
			return str_GDTI+"."+serial;
		}
	}
	
	private String parseCPI96HexString(String epcString) {
		if (epcString.length() != 24) {
			MyLogger.printLog("Incorrect data length for CPI-96 hexString.");
			return null;
		}
		
		
		String binaryStr =String.format("%96s", new BigInteger(epcString,16).toString(2)).replace(" ", "0");
		MyLogger.printLog(binaryStr);
		
		String str_header = binaryStr.substring(0, 8);
		String str_filterValue = binaryStr.substring(8, 11);
		filter = Integer.parseInt(str_filterValue, 2);
		String str_partitionValue = binaryStr.substring(11, 14);
		partition = Integer.parseInt(str_partitionValue, 2);
		this.parseCPIPartition(partition);
		
		// digit length for company prefix
		int cp_digit_length = 12 - partition;
		// digit length for component reference
		int cr_digit_length = partition + 3;
		
		String temp = binaryStr.substring(14, 65);
		String str_companyPrefix = String.format("%"+cp_digit_length+"s", new BigInteger(temp.substring(0, company_prefix_length), 2).toString()).replace(" ", "0");
		MyLogger.printLog("company prefix: "+str_companyPrefix);
//		String str_componentReference = String.format("%"+cr_digit_length+"s", new BigInteger(temp.substring(company_prefix_length), 2).toString()).replace(" ", "0");
		String str_componentReference = new BigInteger(temp.substring(company_prefix_length), 2).toString();
		MyLogger.printLog("component reference: "+str_componentReference);
		
		String cpi_temp = str_companyPrefix + "." + str_componentReference;
		
			
		// 31-bit serial
		String str_serial = binaryStr.substring(65, 96);
		String serial = new BigInteger(str_serial, 2).toString();
		MyLogger.printLog("serial: "+serial);
		
		MyLogger.printLog(cpi_temp+"." + serial);
			
		return cpi_temp+"."+serial;
	}
	
	private String parseCPIvarHexString(String epcString) {
		int l = epcString.length()*4;
		String binaryStr =String.format("%"+l+"s", new BigInteger(epcString,16).toString(2)).replace(" ", "0");
		MyLogger.printLog("binary string length: "+l);
		
		String str_header = binaryStr.substring(0, 8);
		String str_filterValue = binaryStr.substring(8, 11);
		filter = Integer.parseInt(str_filterValue, 2);
		String str_partitionValue = binaryStr.substring(11, 14);
		partition = Integer.parseInt(str_partitionValue, 2);
		this.parseCPIvarPartition(partition);

		String temp = binaryStr.substring(14);
		String str_companyPrefix = temp.substring(0, company_prefix_length);
		MyLogger.printLog("binary company prefix: "+str_companyPrefix);
		String C = new BigInteger(str_companyPrefix, 2).toString();
		MyLogger.printLog("company prefix: "+C);
		
		String P = "";
		String str_partReference = temp.substring(company_prefix_length);
		String str_serial = "";
		MyLogger.printLog("binary part reference: "+str_partReference);
		for (int counter=0; counter<(str_partReference.length()/6); counter++) {
			String str = str_partReference.substring(counter*6, (counter+1)*6);
			if (str.equals("000000")) {
				int index = (counter+1)*6;
				str_serial = str_partReference.substring(index, index+40);
				break;
			} else {
				P += parse6bitAsciiStr(str);
			}
		}
		MyLogger.printLog("part reference: "+P);
		
		
		// From GS1 web site, the serial number is calculated with the first 38-bits of the 40-bits string.
		// Therefore, the serial number is different.
		MyLogger.printLog("binary serial number: "+str_serial);
		String S = new BigInteger(str_serial, 2).toString();
		MyLogger.printLog("serial number: "+S);
		
		return C+"."+P+"."+S;
	}
	
	private String parseSGCN96HexString(String epcString) {
		if (epcString.length() != 24) {
			MyLogger.printLog("Incorrect data length for SGCN-96 hexString.");
			return null;
		}
		
		
		String binaryStr =String.format("%96s", new BigInteger(epcString,16).toString(2)).replace(" ", "0");
		MyLogger.printLog(binaryStr);
		
		String str_header = binaryStr.substring(0, 8);
		String str_filterValue = binaryStr.substring(8, 11);
		filter = Integer.parseInt(str_filterValue, 2);
		String str_partitionValue = binaryStr.substring(11, 14);
		partition = Integer.parseInt(str_partitionValue, 2);
		this.parseSGCNPartition(partition);
		
		// digit length for company prefix
		int cp_digit_length = 12 - partition;
		// digit length for coupon reference
		int cr_digit_length = partition;
		
		String temp = binaryStr.substring(14, 55);
		String str_companyPrefix = String.format("%"+cp_digit_length+"s", new BigInteger(temp.substring(0, company_prefix_length), 2).toString()).replace(" ", "0");
		MyLogger.printLog("company prefix: "+str_companyPrefix);
		String str_couponReference = String.format("%"+cr_digit_length+"s", new BigInteger(temp.substring(company_prefix_length), 2).toString()).replace(" ", "0");
		MyLogger.printLog("coupon reference: "+str_couponReference);
		
		String sgcn_temp = str_companyPrefix + str_couponReference;
		
		char[] chars = sgcn_temp.toCharArray();
		if (chars.length != 12) {
			MyLogger.printLog("Format error...");
			return null;
		} else {
			MyLogger.printLog(sgcn_temp);
			
			int checksum = 0;
			int index = 0;
			for (int i=0; i<chars.length; i++) {
				index = (chars.length - 1) - i ;
				if (i%2 == 0) {
					checksum += Character.getNumericValue(chars[index]) * 3;
				} else if (i%2 == 1) {
					checksum += Character.getNumericValue(chars[index]);
				}
				
			}
			
			int check_bit = 0;
			if (checksum%10 > 0) {
				check_bit = 10 - (checksum%10);
			}
			String str_SGCN = sgcn_temp+check_bit;
			MyLogger.printLog(str_SGCN);
			
			// 41-bit serial
			String str_serial = binaryStr.substring(55, 96);
			String serial = parseNumericString(str_serial);
			MyLogger.printLog(str_SGCN+"." + serial);
			
			return str_SGCN+"."+serial;
		}
	}
	
	private String parseITIP110HexString(String epcString) {
		if (epcString.length() != 28) {
			MyLogger.printLog("Incorrect data length for ITIP-110 hexString.");
			return null;
		}
		
		
		String binaryStr =String.format("%112s", new BigInteger(epcString,16).toString(2)).replace(" ", "0").substring(0, 110);
		MyLogger.printLog(binaryStr);
		
		String str_header = binaryStr.substring(0, 8);
		String str_filterValue = binaryStr.substring(8, 11);
		filter = Integer.parseInt(str_filterValue, 2);
		String str_partitionValue = binaryStr.substring(11, 14);
		partition = Integer.parseInt(str_partitionValue, 2);
		this.parseITIPPartition(partition);
		
		// digit length for company prefix
		int cp_digit_length = 12 - partition;
		// digit length for item reference
		int ir_digit_length = partition + 1;
		
		String temp = binaryStr.substring(14, 58);
		String str_companyPrefix = String.format("%"+cp_digit_length+"s", new BigInteger(temp.substring(0, company_prefix_length), 2).toString()).replace(" ", "0");
		MyLogger.printLog("company prefix: "+str_companyPrefix);
		String str_itemReference = String.format("%"+ir_digit_length+"s", new BigInteger(temp.substring(company_prefix_length), 2).toString()).replace(" ", "0");
		MyLogger.printLog("coupon reference: "+str_itemReference);
		
		String itip_temp = str_itemReference.substring(0, 1)
							+ str_companyPrefix
							+ str_itemReference.substring(1);
		
		char[] chars = itip_temp.toCharArray();
		if (chars.length != 13) {
			MyLogger.printLog("Format error...");
			return null;
		} else {
			MyLogger.printLog(itip_temp);
			
			int checksum = 0;
			int index = 0;
			for (int i=0; i<chars.length; i++) {
				index = (chars.length - 1) - i ;
				if (i%2 == 0) {
					checksum += Character.getNumericValue(chars[index]) * 3;
				} else if (i%2 == 1) {
					checksum += Character.getNumericValue(chars[index]);
				}
				
			}
			
			int check_bit = 0;
			if (checksum%10 > 0) {
				check_bit = 10 - (checksum%10);
			}
			String str_ITIP = itip_temp+check_bit;
			MyLogger.printLog(str_ITIP);
			
			String piece = parse7bitString(binaryStr.substring(58, 65));
			String total = parse7bitString(binaryStr.substring(65, 72));
			MyLogger.printLog(str_ITIP+"."+piece+total);
			
			
			// 38-bit serial
			String str_serial = binaryStr.substring(72);
			String serial = new BigInteger(str_serial, 2).toString();
			MyLogger.printLog(str_ITIP+"."+piece+total+"."+serial);
			
			return str_ITIP+"."+piece+total+"."+serial;
		}
	}
	
	private String parseITIP212HexString(String epcString) {
		if (epcString.length() != 53) {
			MyLogger.printLog("Incorrect data length for ITIP-212 hexString.");
			return null;
		}
		
		
		String binaryStr =String.format("%212s", new BigInteger(epcString,16).toString(2)).replace(" ", "0");
		MyLogger.printLog(binaryStr);
		
		String str_header = binaryStr.substring(0, 8);
		String str_filterValue = binaryStr.substring(8, 11);
		filter = Integer.parseInt(str_filterValue, 2);
		String str_partitionValue = binaryStr.substring(11, 14);
		partition = Integer.parseInt(str_partitionValue, 2);
		this.parseITIPPartition(partition);
		
		// digit length for company prefix
		int cp_digit_length = 12 - partition;
		// digit length for item reference
		int ir_digit_length = partition + 1;
		
		String temp = binaryStr.substring(14, 58);
		String str_companyPrefix = String.format("%"+cp_digit_length+"s", new BigInteger(temp.substring(0, company_prefix_length), 2).toString()).replace(" ", "0");
		MyLogger.printLog("company prefix: "+str_companyPrefix);
		String str_itemReference = String.format("%"+ir_digit_length+"s", new BigInteger(temp.substring(company_prefix_length), 2).toString()).replace(" ", "0");
		MyLogger.printLog("coupon reference: "+str_itemReference);
		
		String itip_temp = str_itemReference.substring(0, 1)
				+ str_companyPrefix
				+ str_itemReference.substring(1);
		
		char[] chars = itip_temp.toCharArray();
		if (chars.length != 13) {
			MyLogger.printLog("Format error...");
			return null;
		} else {
			System.out.println(itip_temp);
			
			int checksum = 0;
			int index = 0;
			for (int i=0; i<chars.length; i++) {
				index = (chars.length - 1) - i ;
				if (i%2 == 0) {
					checksum += Character.getNumericValue(chars[index]) * 3;
				} else if (i%2 == 1) {
					checksum += Character.getNumericValue(chars[index]);
				}
				
			}
			
			int check_bit = 0;
			if (checksum%10 > 0) {
				check_bit = 10 - (checksum%10);
			}
			String str_ITIP = itip_temp+check_bit;
			MyLogger.printLog(str_ITIP);
			
			String piece = this.parse7bitString(binaryStr.substring(58, 65));
			String total = parse7bitString(binaryStr.substring(65, 72));
			MyLogger.printLog(str_ITIP+"."+piece+total);
			
			
			// 140-bit serial
			String str_serial = binaryStr.substring(72);
			String serial = parse7bitString(str_serial);
			MyLogger.printLog(str_ITIP+"."+piece+total+"."+serial);
			
			return str_ITIP+"."+piece+total+"."+serial;
		}
	}
	
	private String parseGID96HexString(String epcString) {
		if (epcString.length() != 24) {
			MyLogger.printLog("Incorrect data length for GID-96 hexString.");
			return null;
		}
		
		
		String binaryStr =String.format("%96s", new BigInteger(epcString,16).toString(2)).replace(" ", "0");
		MyLogger.printLog(binaryStr);
		
		String str_header = binaryStr.substring(0, 8);
		String str_generalManagerNumber = binaryStr.substring(8, 36);
		String str_objectClass = binaryStr.substring(36, 60);
		String str_serialNumber = binaryStr.substring(60);
		
		// General Manager Number
		String M = new BigInteger(str_generalManagerNumber, 2).toString();
		
		// Object Class
		String C = new BigInteger(str_objectClass, 2).toString();
		
		// Serial Number
		String S = new BigInteger(str_serialNumber, 2).toString();
		
		MyLogger.printLog(M + "." + C + "." + S);
		return M + "." + C + "." + S;
		
	}
	
	private String parseUSDOD96HexString(String epcString) {
		if (epcString.length() != 24) {
			MyLogger.printLog("Incorrect data length for GID-96 hexString.");
			return null;
		}
		
		
		String binaryStr =String.format("%96s", new BigInteger(epcString,16).toString(2)).replace(" ", "0");
		MyLogger.printLog(binaryStr);
		
		String str_header = binaryStr.substring(0, 8);
		String str_filter = binaryStr.substring(8, 12);
		String str_cageCode = binaryStr.substring(12, 60);
		String str_serialNumber = binaryStr.substring(60);
		
		String F = String.valueOf(Integer.parseInt(str_filter, 2));
		
		String C = parseAsciiString(str_cageCode);
		
		String S = new BigInteger(str_serialNumber, 2).toString();
		
		return F+"."+C+"."+S;
	}
	
	private String parseADIvarHexString(String epcString) {
		int l = epcString.length()*4;
		String binaryStr =String.format("%"+l+"s", new BigInteger(epcString,16).toString(2)).replace(" ", "0");
		MyLogger.printLog("binary string length: "+l);
		
		String str_header = binaryStr.substring(0, 8);
		String str_filter = binaryStr.substring(8, 14);
		String str_cageCode = binaryStr.substring(14, 50);
		String str_part = binaryStr.substring(50);
		
		filter = Integer.parseInt(str_filter, 2);
		MyLogger.printLog("filter: "+filter);
		
		String cage="", part="", serial="";
		
		for (int counter=0; counter<6; counter++) {
			String temp = str_cageCode.substring(counter*6, (counter+1)*6);
			cage += parse6bitAsciiStr(temp);
		}
		MyLogger.printLog("cage code: "+cage);
		
		for (int counter=0; counter<(str_part.length()/6); counter++) {
			String temp = str_part.substring(counter*6, (counter+1)*6);
			if (temp.equals("000000")) {
				str_part = str_part.substring((counter+1)*6);
				break;
			} else {
				part += parse6bitAsciiStr(temp);
			}
		}
		MyLogger.printLog("part number: "+part);
		
		for (int counter=0; counter<(str_part.length()/6); counter++) {
			String temp = str_part.substring(counter*6, (counter+1)*6);
			if (temp.equals("000000")) {
				str_part = str_part.substring((counter+1)*6);
				break;
			} else {
				serial += parse6bitAsciiStr(temp);
			}
		}
		MyLogger.printLog("serial number: "+serial);
		
		return filter+"."+cage+"."+part+"."+serial;
	}
	
}
