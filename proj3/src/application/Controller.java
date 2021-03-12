package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import payrollObjects.Company;
import payrollObjects.Date;
import payrollObjects.Employee;
import payrollObjects.Fulltime;
import payrollObjects.Management;
import payrollObjects.Parttime;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

/**
Stores information entered by user from GUI and processes Events. This class
is responsible for implementing the functionality of the GUI and responding
to events. Buttons and text fields are updated dynamically based on user
selections.
@author Matthew Brandao, Armit Patel
*/
public class Controller {

	@FXML
	private TextArea textArea;
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
	Function that confirms all entries for adding an employee are Valid. Passes
	valid entries to employeeHelper.
	  
	@param event action that fired this event
	*/
	@FXML
	private void addEmployee(ActionEvent event) {
		String name = nameField.getText();
		if (name.isEmpty()) {
			textArea.appendText("Please enter a name.\n");
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
				textArea.appendText("Please select a valid date.\n");
				return;
			}
		} catch (NullPointerException e) {
			textArea.appendText("Please choose a date.\n");
			return;
		}
		RadioButton selectedEmployee = (RadioButton) empTypeGroup.getSelectedToggle();
		String employeeType = selectedEmployee.getText();
		employeeHelper(name, department, dateHired, employeeType);
	}

	/**
	Helper function that takes employee input and distinguishes between types of
	employee to add to the database. Performs error handling to ensure for all
	types of employees the input is valid.
	  
	@param name name of the employee
	@param department the department of the employee
	@param dateHired the date hired of the employee
	@param employeeType the type of employee
	*/
	private void employeeHelper(String name, String department, Date dateHired, String employeeType) {
		switch (employeeType) {
		case "Full Time":
			try {
				String salaryString = salaryField.getText();
				if (salaryString.isEmpty()) {
					textArea.appendText("Please provide a salary.\n");
					return;
				}
				double annualSalary = Double.parseDouble(salaryString);
				Fulltime newFullTime = new Fulltime(name, department, dateHired, annualSalary);
				if (annualSalary < 0) { // validate salary
					textArea.appendText("Salary cannot be negative.\n");
					return;
				}
				if (!company.add(newFullTime)) { // check if employee exists
					textArea.appendText("Employee is already in the list.\n");
					return;
				}
				company.add(newFullTime);
			} catch (NumberFormatException e) {
				textArea.appendText("Invalid salary format.\n");
				return;
			}
			break;
		case "Part Time":
			double hourlyRate;
			try {
				String rateString = rateField.getText();
				if (rateString.isEmpty()) {
					textArea.appendText("Please enter a hourly rate.\n");
					return;
				}
				hourlyRate = Double.parseDouble(rateString);
				Parttime newPartTime = new Parttime(name, department, dateHired, hourlyRate);
				if (hourlyRate < 0) { // validate pay rate
					textArea.appendText("Pay rate cannot be negative.\n");
					return;
				}
				if (!company.add(newPartTime)) { // check if employee exists
					textArea.appendText("Employee is already in the list.\n");
					return;
				}
				company.add(newPartTime);
			} catch (NumberFormatException e) {
				textArea.appendText("Invalid hours format.\n");
				return;
			}
			break;
		case "Management":
			try {
				final int MAN_ID = 1, DEP_HEAD_ID = 2, DIR_ID = 3;
				if (salaryField.getText().isEmpty()) {
					textArea.appendText("Please provide a salary.\n");
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
					textArea.appendText("Salary cannot be negative.\n");
					return;
				}
				if (!company.add(newManagement)) { // check if employee exists
					textArea.appendText("Employee is already in the list.\n");
					return;
				}
				company.add(newManagement);
			} catch (NumberFormatException e) {
				textArea.appendText("Invalid salary format.\n");
				return;
			}
			break;
		}
		textArea.appendText("Employee added.\n");
	}

	/**
	Clears all fields of the GUI. Radio buttons are not effected.
	  
	@param event action that fired this event
	*/
	@FXML
	private void clearFields(ActionEvent event) {
		nameField.clear();
		datePicker.setValue(null);
		salaryField.clear();
		rateField.clear();
		hoursField.clear();
	}

	/**
	Checks if the company database is empty, otherwise processes payments and
	outputs a message to the text area displaying attempt result.
	  
	@param event
	*/
	@FXML
	private void computePayments(ActionEvent event) {
		if (company.isEmpty())
			textArea.appendText("Employee database is empty.\n");
		else {
			company.processPayments();
			textArea.appendText("Payments calculated.\n");
		}
	}

	/**
	Handles all attempts to export the employee database to a text file on to the
	local machine. The only format allowed for export is the *.txt extension. The
	function will not allow users to export databases that are empty.
	  
	@param event action that fired this event
	*/
	@FXML
	private void exportFile(ActionEvent event) {
		if (company.isEmpty()) {
			textArea.appendText("No data exists in current database to export.\n");
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
			textArea.appendText("Database exported.");
		} catch (NullPointerException e) {
			return;
		}
	}

	/**
	Handles all attempts to import an employee database to the current database.
	The only file formats allowed for import are of the *.txt extension. Upon
	successful import the current database is not overwritten. The new employees
	from the text file are appended to the current database. This function also
	handles cases where the contents of the import file are invalid.
	  
	@param event action that fired this event
	@throws FileNotFoundException
	*/
	@FXML
	private void importFile(ActionEvent event) throws FileNotFoundException {
		final int FIRST_ARG = 0, SECOND_ARG = 1;
		final int THIRD_ARG = 2, FOURTH_ARG = 3;
		final int FIFTH_ARG = 4, SIXTH_ARG = 5;
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
				String employeeType = args[FIRST_ARG];
				String name = args[SECOND_ARG];
				String department = args[THIRD_ARG];
				Date dateHired = new Date(args[FOURTH_ARG]);
				switch (employeeType) {
				case "P":
					double rate = Double.parseDouble(args[FIFTH_ARG]);
					company.add(new Parttime(name, department, dateHired, rate));
					break;
				case "M":
					double managerSalary = Double.parseDouble(args[FIFTH_ARG]);
					int manageRole = Integer.parseInt(args[SIXTH_ARG]);
					company.add(new Management(name, department, dateHired, managerSalary, manageRole));
					break;
				case "F":
					double fullTimeSalary = Double.parseDouble(args[FIFTH_ARG]);
					company.add(new Fulltime(name, department, dateHired, fullTimeSalary));
					break;
				}
			}
			in.close();
		} catch (NullPointerException e) {
			return;
		} catch (ArrayIndexOutOfBoundsException e) {
			textArea.appendText("Error with file contents for importing database.\n");
			return;
		}
		textArea.appendText("Database imported.\n");
	}

	/**
	Checks if employee database is empty, otherwise prints earnings statements
	sorted by Date in ascending order to the text area of the GUI.
	  
	@param event action that fired this event
	*/
	@FXML
	private void printByDate(ActionEvent event) {
		String displayDB = company.printByDate();
		if (displayDB == null) {
			textArea.appendText("Employee database is empty.\n");
			return;
		}
		textArea.appendText("--Printing by date--\n" + displayDB);
	}

	/**
	Checks if employee database is empty, otherwise prints earnings statements
	sorted by Department to the text area of the GUI.
	  
	@param event action that fired this event
	*/
	@FXML
	private void printByDepartment(ActionEvent event) {
		String displayDB = company.printByDepartment();
		if (displayDB == null) {
			textArea.appendText("Employee database is empty.\n");
			return;
		}
		textArea.appendText("--Printing by department--\n" + displayDB);
	}

	/**
	Checks if employee database is empty, otherwise prints earnings statements of
	the current state of the database to the text area of the GUI.
	  
	@param event action that fired this event
	*/
	@FXML
	private void printEmployees(ActionEvent event) {
		String displayDB = company.print();
		if (displayDB == null) {
			textArea.appendText("Employee database is empty.\n");
			return;
		}
		textArea.appendText("--Printing all employees--\n" + displayDB);
	}

	/**
	Collects input fields from the GUI and removes employee from the database.
	Performs error handling to make sure the user is attempting to remove an
	employee in situations where such an action is allowed.
	  
	@param event action that fired this event
	*/
	@FXML
	private void removeEmployee(ActionEvent event) {
		String name = nameField.getText();
		if (name.isEmpty()) {
			textArea.appendText("Please enter a name.\n");
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
				textArea.appendText("Please select a valid date.\n");
				return;
			}
		} catch (java.lang.NullPointerException e) {
			textArea.appendText("Please select a date.\n");
			return;
		}
		if (company.remove(new Employee(name, department, dateHired)))
			textArea.appendText("Employee removed.\n");
		else
			textArea.appendText("Employee could not be found.\n");
	}

	/**
	Collects input from GUI and sets hours for a Parttime employee. Performs
	error handling to make sure the user is attempting to set the hours in
	situations where such an action is allowed.
	  
	 @param event action that fired this event
	*/
	@FXML
	private void setHours(ActionEvent event) {
		final int MIN_HOURS = 0, MAX_HOURS = 100;
		if (nameField.getText().isEmpty()) {
			textArea.appendText("Please enter a name.\n");
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
				textArea.appendText("Please select a valid date.\n");
				return;
			}
			int hours = Integer.parseInt(hoursField.getText());
			if (hours < MIN_HOURS) {
				textArea.appendText("Hours cannot be negative.\n");
				return;
			}
			if (hours > MAX_HOURS) { // validate hours
				textArea.appendText("Invalid Hours: over 100.\n");
				return;
			}
			if (!company.setHours(new Parttime(nameField.getText(), department, dateHired, hours))) {
				textArea.appendText("Employee does not exist.\n");
				return;
			}
			textArea.appendText("Working hours set.\n");
		} catch (java.lang.NullPointerException e) {
			textArea.appendText("Please select a date.\n");
			return;
		} catch (NumberFormatException e) {
			textArea.appendText("Please enter valid hours.\n");
		}
	}

	/**
	Updates GUI form when FullTime employee is selected.
	  
	@param event action that fired this event
	*/
	@FXML
	private void selectFullRB(ActionEvent event) {
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
	Updates GUI form when Management employee is selected.
	  
	@param event action that fired this event
	*/
	@FXML
	private void selectManageRB(ActionEvent event) {
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
	Updates GUI form when PartTime employee is selected.
	  
	@param event action that fired this event
	*/
	@FXML
	private void selectPartRB(ActionEvent event) {
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