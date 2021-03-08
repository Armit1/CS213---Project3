
package application;

import java.io.*;
import java.util.*;

import javax.swing.JSpinner.DateEditor;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.TextFlow;
import payrollObjects.*;

public class Controller implements EventHandler<ActionEvent> {
	private ObservableList<String> employeeTypeList = FXCollections.observableArrayList("Fulltime", "Parttime",
			"Management");

	@FXML
	private ChoiceBox<String> employeeTypeSelect;
	@FXML
	private RadioButton csButton;
	@FXML
	private RadioButton itButton;
	@FXML
	private RadioButton eceButton;
	
	@FXML
	private RadioButton managerSelect;
	@FXML
	private RadioButton departmentHeadSelect;
	@FXML
	private RadioButton directorSelect;
	@FXML
	private Label employeePayType;
	// @FXML
	// private DateSelector dateSelector;
	@FXML
	private TextArea consoleOutputArea;
	
	private ArrayList<String> database;

	public void initialize() {
		
		String javaVersion = System.getProperty("java.version");
		String javafxVersion = System.getProperty("javafx.version");
	
		employeeTypeSelect.getItems().addAll(employeeTypeList);
		employeeTypeSelect.setValue("Fulltime");
		employeePayType.setText("Annual Salary : ");
		managerSelect.setDisable(true);
		departmentHeadSelect.setDisable(true);
		directorSelect.setDisable(true);
		
		database = getDatabaseItems();
		for (String s : database) {
			consoleOutputArea.appendText(s+ "\n");
		}
		employeeTypeSelect.setOnAction(this);
		managerSelect.setOnAction(this);
		departmentHeadSelect.setOnAction(this);
		directorSelect.setOnAction(this);
		
		

	}

	private static ArrayList<String> getDatabaseItems() {
		ArrayList<String> database = new ArrayList<String>();
		File fileHolder = new File("C:/Users/armit/git/CS213---Project3/proj3/src/application/database.txt");
		Scanner fileReader;
		try {
			fileReader = new Scanner(fileHolder);
			while (fileReader.hasNext())
				database.add(fileReader.nextLine());

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return database;
	}
	
	private void toggleManagementButtons(String selectedEmployeeType) {
		
		switch(selectedEmployeeType) {
		
		case "Management":
		managerSelect.setDisable(false);
		departmentHeadSelect.setDisable(false);
		directorSelect.setDisable(false);
		break;
		
		default:
			managerSelect.setDisable(true);
			departmentHeadSelect.setDisable(true);
			directorSelect.setDisable(true);
			
			managerSelect.setSelected(false);
			departmentHeadSelect.setSelected(false);
			directorSelect.setSelected(false);
			break;
		
		}
	}

	@Override
	public void handle(ActionEvent event) {
		if(event.getSource() == employeeTypeSelect) {
			String selectedEmployeeType = employeeTypeSelect.getValue();
			toggleManagementButtons(selectedEmployeeType);
			consoleOutputArea.appendText(selectedEmployeeType + " employee selected\n");
			
			if(selectedEmployeeType.equals("Fulltime") || selectedEmployeeType.equals("Management")) {
				employeePayType.setText("Annual Salary :");
			}else {
				employeePayType.setText("Hourly Rate :");
			}
		}
		if(event.getSource() == managerSelect) {
			if(managerSelect.isSelected()) {
				departmentHeadSelect.setSelected(false);
				directorSelect.setSelected(false);
			}
		}
		if(event.getSource() == departmentHeadSelect) {
			if(departmentHeadSelect.isSelected()) {
				managerSelect.setSelected(false);
				directorSelect.setSelected(false);
			}
		}
		if(event.getSource() == directorSelect) {
			if(directorSelect.isSelected()) {
				departmentHeadSelect.setSelected(false);
				managerSelect.setSelected(false);
			}
		}
		
		
		
	}
}