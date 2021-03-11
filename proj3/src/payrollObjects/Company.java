package payrollObjects;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
A container class that defines the abstract data type Company to hold the
company list of employees and it's operations. This class is responsible 
for adding and removing employees to the company. It is also responsible 
for processing payments on the company payroll, searching for employees, 
sorting lists of the employee, printing lists of the earnings of employees, 
resizing the list of employees, and adjusting hours for part time employees.
@author Matthew Brandao, Armit Patel
*/
public class Company {
    private Employee[] emplist;
    private int numEmployee;
    private static final int CAPACITY = 4;
    private static final int ERROR = -1;

    /**
    Creates an array of Employee with an initial capacity of four employees. Stores a
    counter for the amount of employees in the company.
    */
    public Company() {
        emplist = new Employee[CAPACITY];
        numEmployee = 0;
    }

    /**
    Finds the index of the Employee that was passed to the method in the Company.
      
    @param employee reference to an instance of Employee
    @return array index position of the employee in the company list, -1 if not found
    */
    private int find(Employee employee) {
        for (int index = 0; index < numEmployee; index++) {
            if (emplist[index].equals(employee))
                return index;
        }
        return ERROR;
    }

    /**
    Increases the capacity of the company list by a size of four employees. 
    Creates a new array and copies the elements from the old array while preserving the
    original sequence. The reference to the array of employees is passed to the new
    array.
    */
    private void grow() {
        Employee[] newEmpList = new Employee[emplist.length + CAPACITY];
        for (int i = 0; i < emplist.length; i++)
            newEmpList[i] = emplist[i];
        emplist = newEmpList;
    }

    /**
    Adds a new employee to the end of the company list and increments the 
    employee counter. Invokes grow method if the company is full before 
    adding the new employee.
      
    @param emplyee reference to an instance of an Employee
    @return true if the employee does not exist in the list, false if the employee
    already exists 
    */
    public boolean add(Employee employee) { // check the profile before adding
        boolean exists = false;
        if (numEmployee >= emplist.length)
            grow();
        if (find(employee) == ERROR) {
            emplist[numEmployee] = employee;
            numEmployee++;
            exists = true;
        }
        return exists;
    }

    /**
    Removes an employee from the company list and decrements the employee counter. 
    The elements are shifted to handle empty spaces in the company list. Does not alter 
    the list if the employee does not exist in the company.
    
    @param employee reference to an instance of Employee to remove
    @return true if the employee was successfully removed, false if the employee does not
    exist
    */
    public boolean remove(Employee employee) { // maintain the original sequence
        int target = find(employee);
        boolean empExists = false;
        if (target != ERROR) {
            for (int i = target; i < emplist.length - 1; i++) {
                emplist[i] = emplist[i + 1];
                if (emplist[i + 1] == null)
                    break;
            }
            numEmployee--;
            emplist[numEmployee] = null;
            empExists = true;
        }
        return empExists;
    }

    /**
    Sets the working hours for a part time employee. Does not alter any data
    if the employee does not exist or the employee is not a part time employee.
     
    @param employee reference to instance of Employee with hours to set
    @return true if the employee exists and is an instance of Parttime, false
    if the employee does not exist or is not an instance of Parttime
    */
    public boolean setHours(Employee employee) { // set working hours for a part time
        int foundIndex = find(employee);
        boolean empExists = false;
        if (foundIndex != ERROR) {
            if (emplist[foundIndex] instanceof Parttime) {
                empExists = true;
                ((Parttime) emplist[foundIndex]).setHoursWorked(((Parttime) employee).getHoursWorked());
            }
        }
        return empExists;
    }

    /**
    Proccess the payments for all employees in the company. 
    */
    public void processPayments() {
        for (int i = 0; i < numEmployee; i++)
            emplist[i].calculatePayment();
    }

	/**
	 * Prints the original list of employees in the company. If there are no
	 * employees present in the list it will output a warning.
	 */
	public String print() { // print earning statements for all employees
		if (numEmployee == 0) {
			return null;
		}
		return getList();
	}

	/**
	 * Sorts the list of employees present in the company by department in ascending
	 * order and prints the list of employees and their attributes in ascending
	 * order to the console. The list is sorted in-memory. If there are no employees
	 * present in the list it will not sort and instead it will output a warning.
	 */
	public String printByDepartment() { // print earning statements by department
		if (numEmployee == 0) {
			return null;
		}
		final int GREATER = 1;
		for (int i = 0; i < numEmployee; i++) {
			for (int j = 0; j < numEmployee - i - 1; j++) {
				String currDep = emplist[j].getProfile().getDepartment();
				String nextDep = emplist[j + 1].getProfile().getDepartment();
				int compare = currDep.compareTo(nextDep);
				if (compare >= GREATER) {
					Employee temp = emplist[j];
					emplist[j] = emplist[j + 1];
					emplist[j + 1] = temp;
				}
			}
		}
		return getList();
	}

	/**
	 * Sorts the list of employees present in the company by date published in
	 * ascending order and prints the list of employees and their attributes in
	 * ascending order to the console. The list is sorted in-memory. If there are no
	 * employees present in the list it will not sort and instead it will output a
	 * warning.
	 */
	public String printByDate() { // print earning statements by date hired
		if (numEmployee == 0) {
			return null;
		}
		final int GREATER = 1;
		for (int i = 0; i < numEmployee; i++) {
			for (int j = 0; j < numEmployee - i - 1; j++) {
				Profile currProfile = emplist[j].getProfile();
				Profile nextProfile = emplist[j + 1].getProfile();
				Date currentDate = currProfile.getDateHired();
				Date nextDate = nextProfile.getDateHired();
				int compareResult = currentDate.compareTo(nextDate);
				if (compareResult == GREATER) {
					Employee temp = emplist[j];
					emplist[j] = emplist[j + 1];
					emplist[j + 1] = temp;
				}
			}
		}
		return getList();
	}

	/**
	 * Helper function that prints the current list of company employees that are
	 * stored in-memory of the Company object.
	 */
	private String getList() {
		String ret = "";
		for (int i = 0; i < numEmployee; i++)
			ret += emplist[i].toString() + "\n";
		return ret;
	}
    
    /**
    Checks if the company currently holds any employees. 
    
    @return true if the company does not hold any employees, false 
    if the company does hold employees
    */
    public boolean isEmpty() {
        if (numEmployee == 0) 
            return true;
        return false;
    }
    
    public void exportDatabase(File targetFile) throws NullPointerException {
		try {
			PrintWriter pw = new PrintWriter(targetFile);
			pw.print(getList());
			pw.close();
		} catch (FileNotFoundException e) {
			return;
		}
	}
}
