import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import scannel.gpio.DigitalIOController;
import scannel.reader.ReaderConfig;
import scannel.reader.ReaderUtility;
import scannel.ui.MainWindow;

public class ReaderTool extends Application {


	@Override
	public void start(Stage mainStage) throws Exception {
//		DigitalIOController.getInstance().start();
		
		MainWindow main = new MainWindow();
		Scene scene = new Scene(main, 1000, 800);
		mainStage.setScene(scene);
		mainStage.setTitle("Scannel ReaderTool");
		mainStage.show();
	}

	@Override
	public void stop(){
//		DigitalIOController.getInstance().destroy();
		ReaderUtility.getInstance().destroy();
		ReaderConfig.getInstance().saveConfig();
		ReaderConfig.getInstance().destroy();
		System.out.println("ReaderTool closed!");
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
