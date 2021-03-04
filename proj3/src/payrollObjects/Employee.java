package payrollObjects;
import java.text.DecimalFormat;

/**
Defines the common data and operations for all employee type. Each employee has
a profile that uniquely defines the employee.
@author Matthew Brandao, Armit Patel 
*/
public class Employee {
    private Profile profile;
    private double payment = 0;
    
    /**
    Creates an instance of Employee. 
    
    @param name name of employee in the format last name, first name
    @param department the department an employee is in
    @param dateHired the date an employee is hired
    */
    public Employee(String name, String department, Date dateHired) {
        profile = new Profile(name, department, dateHired);
    }
    
    /**
    Compares the profile of two employees.
    
    @return true if the employees share the same profile, false if the 
    profiles are different or the parameter is not an instance of Employee
    */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Employee) {
            Employee employee = (Employee) obj;
            return this.profile.equals(employee.getProfile());
        }
        return false;
    }

    /**
    Returns a string representation of an employee's profile attributes and payment amount.
    
    @return string string representation of an employee's attributes 
    */
    @Override
    public String toString() {
        DecimalFormat formatter = new DecimalFormat("#,##0.00");
        String formatPayment = formatter.format(payment);
        return profile.toString() + String.format("::Payment $%s", formatPayment);
    }
    
    /**
    Calculates the payment for employee. Automatically handles the bi weekly pay period calculations through
    sub classes. 
    */
    public void calculatePayment() {    
    }

    /**
    Returns a reference to an employee's profile.
    
    @return the profile of the employee
    */
    public Profile getProfile() {
        return profile;
    }

    /**
    Sets the payment of the employee to be paid.
    
    @param payment the payment to set
    */
    public void setPayment(double payment) {
        this.payment = payment;
    }
    
    
}
