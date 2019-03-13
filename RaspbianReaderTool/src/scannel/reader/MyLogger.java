package scannel.reader;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyLogger {

	// Use MyLogger to print log for testing, so that it's convenient to remove testing log by changing the variable PrintLog
	private final static boolean PrintLog = true;
	
	public MyLogger() {
		// TODO Auto-generated constructor stub
	}
	
	public static void printLog(String str) {
		if (PrintLog) {
			System.out.println(str);
		}
	}
	
	public static void printLog() {
		if (PrintLog) {
			System.out.println();
		}
	}
	
	public static void printErrorLog(Exception e) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		String file_path = ReaderUtility.FILE_DIRECTORY+formatter.format("ErrorLog_"+new Date())+".txt";
		File f = new File(file_path);
		if (!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		
		try {
			PrintWriter pw = new PrintWriter(new FileWriter(f, true));
			pw.write(new Date().toString());
			e.printStackTrace(pw);
			pw.flush();
			pw.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
	}
}
