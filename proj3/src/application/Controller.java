
package application;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import payrollObjects.Date;
import payrollObjects.Employee;
import payrollObjects.Fulltime;
import payrollObjects.Management;
import payrollObjects.Parttime;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import payrollObjects.*;


/**
*Stores information entered by user from GUI and processes Events. This class is responsible for implementing the functionality
*of the GUI and responding to events. Buttons and text fields are updated dynamically based on user selections.
@author Matthew Brandao, Armit Patel
*/
public class Controller {

	@FXML
	private TextArea consoleOutputArea;
	@FXML
	private TextField nameField;
	@FXML
	private TextField salaryField;
	@FXML
	private TextField rateField;
	@FXML
	private TextField hoursField;
	@FXML
	private DatePicker datePicker;
	@FXML
	private ToggleGroup empTypeGroup;
	@FXML
	private ToggleGroup departmentGroup;
	@FXML
	private ToggleGroup manageGroup;
	@FXML
	private RadioButton managerSelect;
	@FXML
	private RadioButton departmentHeadSelect;
	@FXML
	private RadioButton directorSelect;
	@FXML
	private Button setHoursBTN;
	@FXML
	private Label rateLabel;
	@FXML
	private Label hoursLabel;
	@FXML
	private Label salaryLabel;

	private static Company company = new Company();

	/**
	  *Function that confirms all entries for adding an employee are Valid.
	  *Passes valid entries to employeeHelper.
	    */
	@FXML
	void addEmployee(ActionEvent event) {
		String name = nameField.getText();
		if (name.isEmpty()) {
			consoleOutputArea.appendText("Please enter a name.\n");
			return;
		}
		RadioButton selectedDept = (RadioButton) departmentGroup.getSelectedToggle();
		String department = selectedDept.getText();
		Date dateHired;
		try {
			LocalDate localDate = datePicker.getValue();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
			String dateString = localDate.format(formatter);
			dateHired = new Date(dateString);
			if (!dateHired.isValid()) {
				consoleOutputArea.appendText("Please select a valid date.\n");
				return;
			}
		} catch (NullPointerException e) {
			consoleOutputArea.appendText("Please choose a date.\n");
			return;
		}
		RadioButton selectedEmployee = (RadioButton) empTypeGroup.getSelectedToggle();
		String employeeType = selectedEmployee.getText();
		employeeHelper(name, department, dateHired, employeeType);
	}

	/**
	  *Helper function that formats all entries of GUI and adds employee to database.
	    */
	private void employeeHelper(String name, String department, Date dateHired, String employeeType) {
		switch (employeeType) {
		case "Full Time":
			try {
				String salaryString = salaryField.getText();
				if (salaryString.isEmpty()) {
					consoleOutputArea.appendText("Please provide a salary.\n");
					return;
				}
				double annualSalary = Double.parseDouble(salaryString);
				Fulltime newFullTime = new Fulltime(name, department, dateHired, annualSalary);
				if (annualSalary < 0) { // validate salary
					consoleOutputArea.appendText("Salary cannot be negative.\n");
					return;
				}
				if (!company.add(newFullTime)) { // check if employee exists
					consoleOutputArea.appendText("Employee is already in the list.\n");
					return;
				}
				company.add(newFullTime);
			} catch (NumberFormatException e) {
				consoleOutputArea.appendText("Invalid salary format.\n");
				return;
			}
			break;
		case "Part Time":
			double hourlyRate;
			try {
				String rateString = rateField.getText();
				if (rateString.isEmpty()) {
					consoleOutputArea.appendText("Please enter a hourly rate.\n");
					return;
				}
				hourlyRate = Double.parseDouble(rateString);
				Parttime newPartTime = new Parttime(name, department, dateHired, hourlyRate);
				if (hourlyRate < 0) { // validate pay rate
					consoleOutputArea.appendText("Pay rate cannot be negative.\n");
					return;
				}
				if (!company.add(newPartTime)) { // check if employee exists
					consoleOutputArea.appendText("Employee is already in the list.\n");
					return;
				}
				company.add(newPartTime);
			} catch (NumberFormatException e) {
				consoleOutputArea.appendText("Invalid hours format.\n");
				return;
			}
			break;
		case "Management":
			try {
				final int MAN_ID = 1, DEP_HEAD_ID = 2, DIR_ID = 3;
				if (salaryField.getText().isEmpty()) {
					consoleOutputArea.appendText("Please provide a salary.\n");
					return;
				}
				double annualSalary = Double.parseDouble(salaryField.getText());
				RadioButton selectedRole = (RadioButton) manageGroup.getSelectedToggle();
				int manageRole;
				if (selectedRole.getText().equals("Manager"))
					manageRole = MAN_ID;
				else if (selectedRole.getText().equals("Department Head"))
					manageRole = DEP_HEAD_ID;
				else
					manageRole = DIR_ID;
				Management newManagement = new Management(name, department, dateHired, annualSalary, manageRole);
				if (annualSalary < 0) { // validate salary
					consoleOutputArea.appendText("Salary cannot be negative.\n");
					return;
				}
				if (!company.add(newManagement)) { // check if employee exists
					consoleOutputArea.appendText("Employee is already in the list.\n");
					return;
				}
				company.add(newManagement);
			} catch (NumberFormatException e) {
				consoleOutputArea.appendText("Invalid salary format.\n");
				return;
			}
			break;
		}
		consoleOutputArea.appendText("Employee added.\n");
	}

	/**
	  *Clears all fields of GUI.
	    */
	@FXML
	void clearFields(ActionEvent event) {
		nameField.clear();
		datePicker.setValue(null);
		salaryField.clear();
		rateField.clear();
		hoursField.clear();
	}

	/**
	  *Checks if company database is empty, otherwise processes payments and outputs to console area.
	    */
	@FXML
	void computePayments(ActionEvent event) {
		if (company.isEmpty())
			consoleOutputArea.appendText("Employee database is empty.\n");
		else {
			company.processPayments();
			consoleOutputArea.appendText("Payments calculated.\n");
		}
	}

	/**
	  *Helper function that exports file when export option is clicked.
	    */
	@FXML
	void exportFile(ActionEvent event) {
		if (company.isEmpty()) {
			consoleOutputArea.appendText("No data exists in current database to export.\n");
			return;
		}
		FileChooser chooser = new FileChooser();
		chooser.setTitle("Open Target File for the Export");
		chooser.getExtensionFilters().addAll(new ExtensionFilter("Text Files", "*.txt"),
				new ExtensionFilter("All Files", "*.*"));
		Stage stage = new Stage();
		File targetFile = chooser.showSaveDialog(stage); // get the reference of the target file
		// write code to write to the file.
		try {
			company.exportDatabase(targetFile);
			consoleOutputArea.appendText("Database exported.");
		} catch (NullPointerException e) {
			return;
		}
	}
	
	/**
	  *Helper function that imports file when import option is clicked.
	    */
	@FXML
	void importFile(ActionEvent event) throws FileNotFoundException {
		FileChooser chooser = new FileChooser();
		chooser.setTitle("Open Source File for the Import");
		chooser.getExtensionFilters().addAll(new ExtensionFilter("Text Files", "*.txt"),
				new ExtensionFilter("All Files", "*.*"));
		Stage stage = new Stage();
		File sourceFile = chooser.showOpenDialog(stage); // get the reference of the source file
		// write code to read from the file.
		try {
			Scanner in = new Scanner(sourceFile);
			while (in.hasNextLine()) {
				String entry = in.nextLine();
				String[] args = entry.split(",");
				String employeeType = args[0];
				String name = args[1];
				String department = args[2];
				Date dateHired = new Date(args[3]);
				switch (employeeType) {
				case "P":
					double rate = Double.parseDouble(args[4]);
					company.add(new Parttime(name, department, dateHired, rate));
					break;
				case "M":
					double managerSalary = Double.parseDouble(args[4]);
					int manageRole = Integer.parseInt(args[5]);
					company.add(new Management(name, department, dateHired, managerSalary, manageRole));
					break;
				case "F":
					double fullTimeSalary = Double.parseDouble(args[4]);
					company.add(new Fulltime(name, department, dateHired, fullTimeSalary));
					break;
				}
			}
			in.close();
		} catch (NullPointerException e) {
			return;
		} catch (ArrayIndexOutOfBoundsException e) {
			consoleOutputArea.appendText("Error with file contents for importing database.\n");
			return;
		}
		consoleOutputArea.appendText("Database imported.\n");
	}
	/**
	  *Checks if employee database is empty, otherwise prints earnings statements sorted by Date to consoleOutputArea
	    */
	@FXML
	void printByDate(ActionEvent event) {
		String displayDB = company.printByDate();
		if (displayDB == null) {
			consoleOutputArea.appendText("Employee database is empty.\n");
			return;
		}
		consoleOutputArea.appendText("--Printing by date--\n" + displayDB);
	}

	/**
	  *Checks if employee database is empty, otherwise prints earnings statements sorted by Department to consoleOutputArea
	    */
	@FXML
	void printByDepartment(ActionEvent event) {
		String displayDB = company.printByDepartment();
		if (displayDB == null) {
			consoleOutputArea.appendText("Employee database is empty.\n");
			return;
		}
		consoleOutputArea.appendText("--Printing by department--\n" + displayDB);
	}

	/**
	  *Checks if employee database is empty, otherwise prints earnings statements to consoleOutputArea.
	    */
	@FXML
	void printEmployees(ActionEvent event) {
		String displayDB = company.print();
		if (displayDB == null) {
			consoleOutputArea.appendText("Employee database is empty.\n");
			return;
		}
		consoleOutputArea.appendText("--Printing all employees--\n" + displayDB);
	}

	/**
	  *Formats input from GUI form and removes employee.
	    */
	@FXML
	void removeEmployee(ActionEvent event) {
		String name = nameField.getText();
		if (name.isEmpty()) {
			consoleOutputArea.appendText("Please enter a name.\n");
			return;
		}
		RadioButton selectedDept = (RadioButton) departmentGroup.getSelectedToggle();
		String department = selectedDept.getText();
		Date dateHired;
		try {
			LocalDate localDate = datePicker.getValue();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
			String dateString = localDate.format(formatter);
			dateHired = new Date(dateString);
			if (!dateHired.isValid()) {
				consoleOutputArea.appendText("Please select a valid date.\n");
				return;
			}
		} catch (java.lang.NullPointerException e) {
			consoleOutputArea.appendText("Please select a date.\n");
			return;
		}
		if (company.remove(new Employee(name, department, dateHired)))
			consoleOutputArea.appendText("Employee removed.\n");
		else
			consoleOutputArea.appendText("Employee could not be found.\n");
	}

	/**
	  *Formats input from GUI form and sets hours for Parttime employee.
	    */
	@FXML
	void setHours(ActionEvent event) {
		final int MIN_HOURS = 0, MAX_HOURS = 100;
		if (nameField.getText().isEmpty()) {
			consoleOutputArea.appendText("Please enter a name.\n");
			return;
		}
		RadioButton selectedDept = (RadioButton) departmentGroup.getSelectedToggle();
		String department = selectedDept.getText();
		Date dateHired;
		try {
			LocalDate localDate = datePicker.getValue();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
			String dateString = localDate.format(formatter);
			dateHired = new Date(dateString);
			if (!dateHired.isValid()) {
				consoleOutputArea.appendText("Please select a valid date.\n");
				return;
			}
			int hours = Integer.parseInt(hoursField.getText());
			if (hours < MIN_HOURS) {
				consoleOutputArea.appendText("Hours cannot be negative.\n");
				return;
			}
			if (hours > MAX_HOURS) { // validate hours
				consoleOutputArea.appendText("Invalid Hours: over 100.\n");
				return;
			}
			if (!company.setHours(new Parttime(nameField.getText(), department, dateHired, hours))) {
				consoleOutputArea.appendText("Employee does not exist.\n");
				return;
			}
			consoleOutputArea.appendText("Working hours set.\n");
		} catch (java.lang.NullPointerException e) {
			consoleOutputArea.appendText("Please select a date.\n");
			return;
		} catch (NumberFormatException e) {
			consoleOutputArea.appendText("Please enter valid hours.\n");
		}
	}

	/**
	  *Updates GUI form when FullTime employee is selected.
	    */
	@FXML
	void selectFullRB(ActionEvent event) {
		hoursField.setDisable(true);
		hoursLabel.setDisable(true);
		rateField.setDisable(true);
		rateLabel.setDisable(true);
		salaryField.setDisable(false);
		salaryLabel.setDisable(false);
		managerSelect.setDisable(true);
		departmentHeadSelect.setDisable(true);
		directorSelect.setDisable(true);
		setHoursBTN.setDisable(true);
	}

	/**
	  *Updates GUI form when Management employee is selected.
	    */
	@FXML
	void selectManageRB(ActionEvent event) {
		hoursField.setDisable(true);
		hoursLabel.setDisable(true);
		rateField.setDisable(true);
		rateLabel.setDisable(true);
		salaryField.setDisable(false);
		salaryLabel.setDisable(false);
		managerSelect.setDisable(false);
		departmentHeadSelect.setDisable(false);
		directorSelect.setDisable(false);
		setHoursBTN.setDisable(true);
	}

	/**
	  *Updates GUI form when PartTime employee is selected.
	    */
	@FXML
	void selectPartRB(ActionEvent event) {
		hoursField.setDisable(false);
		hoursLabel.setDisable(false);
		rateField.setDisable(false);
		rateLabel.setDisable(false);
		salaryField.setDisable(true);
		salaryLabel.setDisable(true);
		managerSelect.setDisable(true);
		departmentHeadSelect.setDisable(true);
		directorSelect.setDisable(true);
		setHoursBTN.setDisable(false);
	}
}