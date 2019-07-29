package scannel.reader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import scannel.reader.ReaderConfig.DBType;

public class DatabaseUtility {

	private static DatabaseUtility db_utility;
	private DBType myType;
	private Connection myConn;
	private PreparedStatement myStmt;
	
	private String db_url;
	private String db_name;
	private String db_user;
	private String db_password;
	private String db_table;
	private String field_epc;
	private String field_readerid;
	private String field_time;
	private String readerId;
	
	
	private DatabaseUtility() {
		
	}

	public static DatabaseUtility getInstance() {
		if (db_utility == null) {
			db_utility = new DatabaseUtility();
		}
		
		return db_utility;
	}

	public void destroy() {
		if (db_utility != null) {
			db_utility = null;
		}
	}
	
	public void setDBType(DBType type) {
		myType = type;
	}
	
	public void setDBParameters(String url, String name, String user, String password) {
		this.db_url = url;
		this.db_name = name;
		this.db_user = user;
		this.db_password = password;
	}
	
	public void setTableParameters(String name, String fn_epc, String fn_readerid, String fn_time) {
		this.db_table = name;
		this.field_epc = fn_epc;
		this.field_readerid = fn_readerid;
		this.field_time = fn_time;
	}
	
	public void setReaderId(String id) {
		this.readerId = id;
	}
	
	public void insertTagData() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				if (myType == DBType.MYSQL) {
					inserMySQL();
				} else if (myType == DBType.MS_SQL_SERVER) {
					insertSQLServer();
				}
			}
			
		}).start();
		
	}
	
	private void insertSQLServer() {
		MyLogger.printLog("Start inserting tag data into SQL-Server database...");
		
		myConn = null;
		myStmt = null;
		
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			
			String url = "jdbc:sqlserver://"+db_url+";databaseName="+db_name+";user="+db_user+";password="+db_password;
			myConn = DriverManager.getConnection(url);
			
			String query = "INSERT INTO "+db_table+" ("+field_epc+", "+field_readerid+", "+field_time+") "
					+ "VALUES (?, ?, ?)";
			
			myStmt = myConn.prepareStatement(query);
			
			TagList tagList = ReaderUtility.getInstance().getTagData();
			TagUnit tu;
			
			for (int i=0; i<tagList.size(); i++) {
				tu = tagList.get(i);
				myStmt.setString(1, tu.getEPC());
				myStmt.setString(2, readerId);
				myStmt.setTimestamp(3, new Timestamp(tu.getTime().getTime()));
				myStmt.addBatch();
			}
			
			myStmt.executeBatch();
			
			myStmt.close();
			myConn.close();
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			MyLogger.printErrorLog(e);
		} catch (SQLException e) {
			e.printStackTrace();
			MyLogger.printErrorLog(e);
			
			Platform.runLater(() -> {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText(null);
				alert.setResizable(true);
				alert.setContentText(e.getMessage());
				alert.showAndWait();
			});
			
		} finally {
			if (myStmt != null) {
				try {
					myStmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
			if (myConn != null) {
				try {
					myConn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
			MyLogger.printLog("Complete inserting tag data...");
		}
		
	}
	
	private void inserMySQL() {
		myConn = null;
		myStmt = null;
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			
			String url = "jdbc:mysql://"+db_url+"/"+db_name+"?serverTimezone=UTC";
			myConn = DriverManager.getConnection(url, db_user, db_password);
			
			String query = "INSERT INTO "+db_table+" ("+field_epc+", "+field_readerid+", "+field_time+") "
					+ "VALUES (?, ?, ?)";
			
			myStmt = myConn.prepareStatement(query);
			
			TagList tagList = ReaderUtility.getInstance().getTagData();
			TagUnit tu;
			
			for (int i=0; i<tagList.size(); i++) {
				tu = tagList.get(i);
				myStmt.setString(1, tu.getEPC());
				myStmt.setString(2, readerId);
				myStmt.setTimestamp(3, new Timestamp(tu.getTime().getTime()));
				myStmt.addBatch();
			}
			
			myStmt.executeBatch();
			
			myStmt.close();
			myConn.close();
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			MyLogger.printErrorLog(e);
		} catch (SQLException e) {
			e.printStackTrace();
			MyLogger.printErrorLog(e);
			
			Platform.runLater(() -> {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText(null);
				alert.setResizable(true);
				alert.setContentText(e.getMessage());
				alert.showAndWait();
				
			});
			
		} finally {
			if (myStmt != null) {
				try {
					myStmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
			if (myConn != null) {
				try {
					myConn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
}
