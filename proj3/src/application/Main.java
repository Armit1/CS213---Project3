package application;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application  {

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

    public static void main(String[] args) {
        launch(args);
    }
}
