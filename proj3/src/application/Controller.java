  
package application;

import javax.swing.JSpinner.DateEditor;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class Controller {
	private ObservableList<String> employeeTypeList = FXCollections.observableArrayList("Fulltime","Parttime","Management");
	
    @FXML
    private ChoiceBox<String> employeeTypeSelect;
    @FXML
    private RadioButton csButton;
    @FXML
    private RadioButton itButton;
    @FXML
    private RadioButton eceButton;
   // @FXML
    //private DateSelector dateSelector;
    @FXML
    private TextArea consoleOutputArea;

    public void initialize() {
        String javaVersion = System.getProperty("java.version");
        String javafxVersion = System.getProperty("javafx.version");
        employeeTypeSelect.setValue("Fulltime");
        employeeTypeSelect.getItems().addAll(employeeTypeList);
    }
}