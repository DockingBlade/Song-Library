package app;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import view.SonglibController;
import javafx.scene.control.ListView;

public class SonglibApp extends Application{
	public void start(Stage primaryStage) 
			throws Exception {
				// TODO Auto-generated method stub
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("/view/Songlib.fxml"));
				GridPane root = (GridPane)loader.load();
				Scene scene = new Scene(root);
				SonglibController listController = loader.getController();
				listController.start(primaryStage);
				
				primaryStage.setScene(scene);
				primaryStage.setResizable(false);
				primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				    @Override
				    public void handle(WindowEvent event) {
				    	listController.end();
				    }
				});
				primaryStage.show();
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);

	}
}
