package application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import scannel.reader.ReaderConfig;
import scannel.reader.ReaderUtility;
import scannel.ui.MainWindow;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	
	private static Stage mainStage;
	private static MainWindow root;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			mainStage = primaryStage;
			root = new MainWindow();
			Scene scene = new Scene(root, 1600, 1000);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void stop(){
//		DigitalIOController.getInstance().destroy();
		ReaderUtility.getInstance().disconnectReader();
		ReaderUtility.getInstance().destroy();
		ReaderConfig.getInstance().saveConfig();
		ReaderConfig.getInstance().destroy();
		System.out.println("ReaderTool closed!");
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public static Stage getMainStage() {
		return mainStage;
	}
}
