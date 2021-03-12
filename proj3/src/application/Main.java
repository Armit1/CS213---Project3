package application;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
/**
This is the main class for the JavaFX application. Its purpose can be 
described as a driver class for the GUI application. The preferred size
for the scene to be set and the title of the application are also defined 
in this class.
@author Matthew Brandao, Armit Patel
*/
public class Main extends Application  {
	
	/**
	This method is the main entry point for this JavaFX application. The source
	for the root, title, and size are defined in this method. 
	*/
    @Override
    public void start(Stage primaryStage) throws Exception{
    	try {
        Parent root = FXMLLoader.load(getClass().getResource("View.fxml"));
        primaryStage.setTitle("Payroll Processing");
        primaryStage.setScene(new Scene(root, 700, 550));
        primaryStage.show();
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    }
    /**
	This main method is responsible for the action of launching the 
	application.
	
	@param args args for system to run
	*/
    public static void main(String[] args) {
        launch(args);
    }
}
