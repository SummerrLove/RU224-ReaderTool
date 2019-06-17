package scannel.reader;
import java.math.BigInteger;

import scannel.decoder.GS1Decoder.EPC_SCHEMA;
import scannel.ui.DecodeSettingFrame.ENCODE_TYPE;

public class EPCDecoder {

	public EPCDecoder() {
		
	}

	public static String parseEPCString(String hexString, ENCODE_TYPE type, boolean[] schemaSetting) {
		if (type == ENCODE_TYPE.UDC) {
			MyLogger.printLog("Currently not implemented.");
			return null;
//		} else if (type == ENCODE_TYPE.EAN_UPC) {
//			if ((schemaSetting != null) && (schemaSetting[EPC_SCHEMA.SGTIN_96.getValue()])) {
//				return parseSGTIN96HexString(hexString);
//			} else {
//				MyLogger.printLog("Currently we only support EPC schema 'sgtin-96' ");
//				return null;
//			}
		} else if (type == ENCODE_TYPE.EAN_UPC_EAS) {
			MyLogger.printLog("Currently not implemented.");
			return null;
		} else if (type == ENCODE_TYPE.RAWDATA){
			return hexString;
		} else {
			MyLogger.printLog("Unknown encode type.");
			return null;
		}
	}
	
}
