package githubrepoeditor;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.stage.Stage;

public class MainApp extends Application{
	
	public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
        //loader.setController(this);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        
        primaryStage.setTitle("Github Repo Editor");
        primaryStage.setScene(scene);
        
        //AppProperties.setStage(primaryStage);
        //AppProperties.setScene(scene);
        //AppProperties.debugPrint(scene);
        //AppProperties.debugPrint(AppProperties.getScene());
        
        primaryStage.show();
        //IO.println(scene);
        
        
    }
	
	public static void main(String[] args) {
        launch(args);
        //IO.println(args[0]);
    }
}
