import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import scannel.gpio.DigitalIOController;
import scannel.reader.ReaderConfig;
import scannel.reader.ReaderUtility;
import scannel.ui.MainWindow;

public class ReaderTool extends Application {

	private final static String version = "v1.11.07";
	private final static String subject = "Basic - Fix TID/UserBank Reading Issues";

	@Override
	public void start(Stage mainStage) throws Exception {
		DigitalIOController.getInstance().start();
		
		System.out.println();
		System.out.println("==========================");
		System.out.println("RU-224 ReaderTool");
		System.out.println("version: "+version);
//		System.out.println("subject: "+subject);
		System.out.println("==========================");
		System.out.println();
		
		
		MainWindow main = new MainWindow();
		Scene scene = new Scene(main, 1000, 800);
		mainStage.setScene(scene);
		mainStage.setTitle("Scannel ReaderTool - "+version);
		mainStage.show();
	}

	@Override
	public void stop(){
		DigitalIOController.getInstance().destroy();
		ReaderUtility.getInstance().destroy();
		ReaderConfig.getInstance().saveConfig();
		ReaderConfig.getInstance().destroy();
		System.out.println("ReaderTool closed!");
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
