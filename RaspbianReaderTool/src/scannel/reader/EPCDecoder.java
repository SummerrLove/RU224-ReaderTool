package scannel.reader;
import java.math.BigInteger;

import scannel.ui.DecodeSettingFrame.EAN_TYPE;
import scannel.ui.DecodeSettingFrame.ENCODE_TYPE;

public class EPCDecoder {

	
	public EPCDecoder() {
		
	}

	public static String parseEPCString(String hexString, ENCODE_TYPE type) {
		if (type == ENCODE_TYPE.EAN_UPC) {
			return parseSGTIN96HexString(hexString);
		} else {
			MyLogger.printLog("Currently we only support EAN/UPC.");
			return null;
		}
	}
	
	private static String parseSGTIN96HexString(String hexString) {
		if (hexString.length() != 24) {
			MyLogger.printLog("Incorrect format for hexString.");
			return null;
		}
		
		if (!hexString.startsWith("30")) {
			MyLogger.printLog("The header value is not SGTIN-96.");
			return null;
		}
		
		String binaryStr =String.format("%96s", new BigInteger(hexString,16).toString(2)).replace(" ", "0");
		MyLogger.printLog(binaryStr);
//		System.out.println(binaryStr.length());
		
		String str_header = binaryStr.substring(0, 8);
		String str_filterValue = binaryStr.substring(8, 11);
		String str_partitionValue = binaryStr.substring(11, 14);
		int partition = Integer.parseInt(str_partitionValue, 2);
		
		String temp = binaryStr.substring(14, 58);
		int companyPrefix_length = getCompanyPrefixLength(Integer.parseInt(str_partitionValue, 2));
		int itemReference_length = 44 - companyPrefix_length;
		String str_companyPrefix = getCompanyPrefix(partition, temp.substring(0, companyPrefix_length));
		MyLogger.printLog(str_companyPrefix);
		String str_itemReference = getItemReference(partition, temp.substring(companyPrefix_length, 44));
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
			
			int total = 0;
			for (int i=0; i<chars.length; i++) {
				if (i%2 == 0) {
					total += Character.getNumericValue(chars[i]) * 3;
				} else if (i%2 == 1) {
					total += Character.getNumericValue(chars[i]);
				}
				
			}
			int check_bit = 10 - (total%10);
			String str_GTIN14 = gtin_temp+check_bit;
			
			MyLogger.printLog(str_GTIN14);
			String str_serialNumber = binaryStr.substring(58, 96);
//			int serialNumber = Integer.parseInt(str_serialNumber, 2);
			String serialNumber =String.format("%12s", new BigInteger(str_serialNumber, 2).toString(10)).replace(" ", "0");
			MyLogger.printLog(str_GTIN14+"." + serialNumber);
			
			return str_GTIN14+"."+serialNumber;
		}
	}
	
	private static int getCompanyPrefixLength(int partition) {
		switch (partition) {
		case 0:
			return 40;
		case 1:
			return 37;
		case 2:
			return 34;
		case 3:
			return 30;
		case 4:
			return 27;
		case 5:
			return 24;
		case 6:
			return 20;
		default:
			return -1;
		}
	}
	
	private static String getCompanyPrefix(int partition, String binaryString) {
		int decimalValue = Integer.parseInt(binaryString, 2);
		String companyPrefix = null;
		
		switch (partition) {
		case 0:
			companyPrefix = String.format("%012d", decimalValue);
			break;
		case 1:
			companyPrefix = String.format("%011d", decimalValue);
			break;
		case 2:
			companyPrefix = String.format("%010d", decimalValue);
			break;
		case 3:
			companyPrefix = String.format("%09d", decimalValue);
			break;
		case 4:
			companyPrefix = String.format("%08d", decimalValue);
			break;
		case 5:
			companyPrefix = String.format("%07d", decimalValue);
			break;
		case 6:
			companyPrefix = String.format("%06d", decimalValue);
			break;
		default:
			break;
		}
		
		return companyPrefix;
	}
	
	private static String getItemReference(int partition, String binaryString) {
		int decimalValue = Integer.parseInt(binaryString, 2);
		String itemReference = null;
		
		switch (partition) {
		case 0:
			itemReference = String.format("%01d", decimalValue);
			break;
		case 1:
			itemReference = String.format("%02d", decimalValue);
			break;
		case 2:
			itemReference = String.format("%03d", decimalValue);
			break;
		case 3:
			itemReference = String.format("%04d", decimalValue);
			break;
		case 4:
			itemReference = String.format("%05d", decimalValue);
			break;
		case 5:
			itemReference = String.format("%06d", decimalValue);
			break;
		case 6:
			itemReference = String.format("%07d", decimalValue);
			break;
		default:
			break;
		}
		
		return itemReference;
	}
}
