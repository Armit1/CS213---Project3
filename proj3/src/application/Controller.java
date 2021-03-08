
package application;

import java.io.*;
import java.util.*;

import javax.swing.JSpinner.DateEditor;

import payrollObjects.Date;
import payrollObjects.Employee;
import payrollObjects.Fulltime;
import payrollObjects.Management;
import payrollObjects.Parttime;
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
	@FXML
	private DatePicker datepicker;
	@FXML
	private Button printEarnings;
	@FXML
	private Button printByDate;
	@FXML
	private Button printByDept;
	@FXML
	private Button addAction;
	@FXML
	private Button removeAction;
	@FXML
	private Button updateAction;
	@FXML
	private TextArea consoleOutputArea;

	private static Company company = new Company();
	private static final int FIRST_ARG = 1;
	private static final int SECOND_ARG = 2;
	private static final int THIRD_ARG = 3;
	private static final int FOURTH_ARG = 4;
	private static final int FIFTH_ARG = 5;

	public void initialize() {

		String javaVersion = System.getProperty("java.version");
		String javafxVersion = System.getProperty("javafx.version");

		employeeTypeSelect.getItems().addAll(employeeTypeList);
		employeeTypeSelect.setValue("Fulltime");
		employeePayType.setText("Annual Salary : ");
		managerSelect.setDisable(true);
		departmentHeadSelect.setDisable(true);
		directorSelect.setDisable(true);

		company = genCompanyObject();
		
		setActions();
	}
	
	private void setActions() {
		employeeTypeSelect.setOnAction(this);
		managerSelect.setOnAction(this);
		departmentHeadSelect.setOnAction(this);
		directorSelect.setOnAction(this);
		printEarnings.setOnAction(this);
		printByDate.setOnAction(this);
		printByDept.setOnAction(this);
		addAction.setOnAction(this);
		removeAction.setOnAction(this);
		updateAction.setOnAction(this);
	}
	
	/**
    Adds a part time employee to the company list. Checks if the pay rate
    is not negative, if the department is valid, and if the date is 
    valid. If the employee does not exist in the company they are added, otherwise
    the employee will not be added to the list and a warning will display. 
     
    @param tokens the args for constructing a part time employee
    */
    private void addPartTime(String[] tokens) {
        String name = tokens[1];
        String department = tokens[2];
        Date dateHired = new Date(tokens[3]);
        double hourlyRate = Double.parseDouble(tokens[4]);
        Parttime newPartTime = new Parttime(name, department, dateHired, hourlyRate);
        boolean isDepartment = checkDepartment(department);
        boolean isDateHired = newPartTime.getProfile().getDateHired().isValid();
        if (hourlyRate < 0) { // validate pay rate
            System.out.println("Pay rate cannot be negative.");
            return;
        }
        if (!isDepartment) { // validate department
            System.out.println("'" + department + "' is not a valid department code.");
            return;
        }
        if (!isDateHired) { // validate date 
            System.out.println(dateHired.toString() + " is not a valid date!");
            return;
        }
        if (!company.add(newPartTime)) { // check if employee exists
            System.out.println("Employee is already in the list.");
            return;
        }
        System.out.println("Employee added.");
    }

    /**
    Adds a fulltime employee to the company list. Checks if the salary
    is not negative, if the department is valid, and if the date is 
    valid. If the employee does not exist in the company they are added, otherwise
    the employee will not be added to the list and a warning will display. 
     
    @param tokens the args for constructing a full time employee
    */
    private void addFullTime(String[] tokens) {
        String name = tokens[FIRST_ARG];
        String department = tokens[SECOND_ARG];
        Date dateHired = new Date(tokens[THIRD_ARG]);
        double annualSalary = Double.parseDouble(tokens[FOURTH_ARG]);
        Fulltime newFullTime = new Fulltime(name, department, dateHired, annualSalary);
        boolean isDepartment = checkDepartment(department);
        boolean isDateHired = newFullTime.getProfile().getDateHired().isValid();
        if (annualSalary < 0) { // validate salary
            System.out.println("Salary cannot be negative.");
            return;
        }
        if (!isDepartment) { // validate department
            System.out.println("'" + department + "' is not a valid department code.");
            return;
        }
        if (!isDateHired) { // validate date
            System.out.println(dateHired.toString() + " is not a valid date!");
            return;
        }
        if (!company.add(newFullTime)) { // check if employee exists
            System.out.println("Employee is already in the list.");
            return;
        }
        System.out.println("Employee added.");
    }

    /**
    Adds a fulltime management employee to the company list. Checks if the manager role exists, 
    if salary is not negative, if the department is valid, and if the date is 
    valid. If the employee does not exist in the company they are added, otherwise
    the employee will not be added to the list and a warning will display. 
     
    @param tokens the args for constructing a full time management employee
    */
    private void addManagement(String[] tokens) {
        final int MANAGER = 1;
        final int DEP_HEAD = 2;
        final int DIRECTOR = 3;
        String name = tokens[FIRST_ARG];
        String department = tokens[SECOND_ARG];
        Date dateHired = new Date(tokens[THIRD_ARG]);
        double annualSalary = Double.parseDouble(tokens[FOURTH_ARG]);
        int manageRole = Integer.parseInt(tokens[FIFTH_ARG]);
        if (manageRole != MANAGER && manageRole != DEP_HEAD && manageRole != DIRECTOR) {
            System.out.println("Invalid management code.");
            return;
        }
        Management newManagement = new Management(name, department, dateHired, annualSalary, manageRole);
        boolean isDepartment = checkDepartment(department);
        boolean isDateHired = newManagement.getProfile().getDateHired().isValid();
        if (annualSalary < 0) { // validate salary
            System.out.println("Salary cannot be negative.");
            return;
        }
        if (!isDepartment) { // validate department code
            System.out.println("'" + department + "' is not a valid department code.");
            return;
        }
        if (!isDateHired) { // validate date
            System.out.println(dateHired.toString() + " is not a valid date!");
            return;
        }
        if (!company.add(newManagement)) { // check if employee exists
            System.out.println("Employee is already in the list.");
            return;
        }
        System.out.println("Employee added.");
    }

    /**
    Removes an employee from the company list. Checks if there are at least one
    or more employees in the company before attempting to remove the employee. If the 
    employee exists then the employee is removed, otherwise a message is displayed notifying
    that the employee does not exist.
     
    @param tokens the name, department, and date of the employee to remove
    */
    private void removeEmployee(String[] tokens) {
        if (company.isEmpty()) { // check for employees
            System.out.println("Employee database  is empty.");
            return;
        }
        String removeName = tokens[FIRST_ARG];
        String removeDep = tokens[SECOND_ARG];
        Date removeDate = new Date(tokens[THIRD_ARG]);
        Employee removeEmp = new Employee(removeName, removeDep, removeDate);
        if (!company.remove(removeEmp)) { // validate if employee exists
            System.out.println("Employee does not exist.");
            return;
        } 
        System.out.println("Employee removed.");
    }

    /**
    Sets the hours for a part time employee in the company list. Checks if 
    there are at least one or more employees in the company before attempting 
    to set hours. Checks if the working hours are negative, if the working 
    hours are over 100, and if the employee exists in the company list.
     
    @param tokens
    */
    private void setHours(String[] tokens) {
        if (company.isEmpty()) { // check for employees
            System.out.println("Employee database is empty.");
            return;
        }        
        final int MIN_HOURS = 0;
        final int MAX_HOURS = 100;
        String name = tokens[FIRST_ARG];
        String department = tokens[SECOND_ARG];
        Date dateHired = new Date(tokens[THIRD_ARG]);
        int hoursWorked = Integer.parseInt(tokens[FOURTH_ARG]);
        Parttime hoursToSet = new Parttime(name, department, dateHired, hoursWorked);
        
        if (hoursWorked < MIN_HOURS) { // validate hours
            System.out.println("Working hours cannot be negative.");
            return;
        } 
        if (hoursWorked > MAX_HOURS) { // validate hours
            System.out.println("Invalid Hours: over 100.");
            return;
        }
        if (!company.setHours(hoursToSet)) { // check if employee exists
            System.out.println("Employee does not exist.");
            return;
        }
        System.out.println("Working hours set.");
    }

    /**
    Proccesses the payments for all employees in the company list. Checks 
    if there are at least one or more employees in the company before attempting
    to process payments. 
    */
    private void calculatePayment() {
        if (company.isEmpty()) { // check for employees
            System.out.println("Employee database is empty.");
            return;
        }
        company.processPayments();
        System.out.println("Calculation of employee payments is done.");
    }

    /**
    Checks if a department corresponds to at least one of the existing departments
    "CS", "ECE", or "IT" in the company. Department comparisons are case sensitive.
     
    @param department the string of the department to check
    @return true if the department exists in the company, false if the department
    does not exist
    */
    private boolean checkDepartment(String department) {
        switch (department) {
        case "CS":
            return true;
        case "ECE":
            return true;
        case "IT":
            return true;
        default:
            return false;
        }
    }

	private Company genCompanyObject() {
		File fileHolder = new File("C:/Users/armit/git/CS213---Project3/proj3/src/application/database.txt");
		Scanner fileReader;
		String[] tokens;
		String command;
		try {
			fileReader = new Scanner(fileHolder);
			while (fileReader.hasNext()) {
				command = fileReader.nextLine();
				System.out.println(command);
				tokens = command.split(",");
				switch(tokens[0]){
				case "P":
					addPartTime(tokens);
					break;
				case "F":
					addFullTime(tokens);
					break;
				case "M":
					addManagement(tokens);
					break;
				
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return company;
	}

	private void toggleManagementButtons(String selectedEmployeeType) {

		switch (selectedEmployeeType) {

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
	
	private void printHandler(ActionEvent event) {
		consoleOutputArea.setText("");
		if(event.getSource() == printEarnings) {
			consoleOutputArea.appendText(company.print());
		}else if(event.getSource() == printByDate) {
			consoleOutputArea.appendText(company.printByDate());
		}else {
			consoleOutputArea.appendText(company.printByDepartment());
		}
	}
	
	

	@Override
	public void handle(ActionEvent event) {
		
		if (event.getSource() == employeeTypeSelect) {
			String selectedEmployeeType = employeeTypeSelect.getValue();
			toggleManagementButtons(selectedEmployeeType);
			consoleOutputArea.appendText(selectedEmployeeType + " employee selected\n");

			if (selectedEmployeeType.equals("Fulltime") || selectedEmployeeType.equals("Management")) {
				employeePayType.setText("Annual Salary :");
			} else {
				employeePayType.setText("Hourly Rate :");
			}
		}
		
		if (event.getSource() == managerSelect) {
			if (managerSelect.isSelected()) {
				departmentHeadSelect.setSelected(false);
				directorSelect.setSelected(false);
			}
		}
		
		if (event.getSource() == departmentHeadSelect) {
			if (departmentHeadSelect.isSelected()) {
				managerSelect.setSelected(false);
				directorSelect.setSelected(false);
			}
		}
		
		if (event.getSource() == directorSelect) {
			if (directorSelect.isSelected()) {
				departmentHeadSelect.setSelected(false);
				managerSelect.setSelected(false);
			}
		}

		if(event.getSource() == printEarnings || event.getSource() == printByDate || event.getSource() == printByDept) {
			printHandler(event);
		}
	}
}