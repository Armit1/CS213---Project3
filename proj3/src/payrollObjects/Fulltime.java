package payrollObjects;
import java.text.DecimalFormat;

/**
Extends the Employee class and includes specific data and operations to a full-time employee. 
Includes an annual salary for storing the amount an employee makes annually and a payroll for storing 
the amount an employee is to be paid during each payroll period. 
@author Matthew Brandao, Armit Patel
*/
public class Fulltime extends Employee{
    private double annualSalary;
    private double payroll;
    private static final int PAY_PERIODS = 26;
    
    /**
    Creates an instance of Fulltime. A Fulltime employee is also an instance of Employee.
    
    @param name name of the employee
    @param department department the employee belongs to
    @param dateHired the date the employee was hired
    @param annualSalary the annual salary of the employee
    */
    public Fulltime(String name, String department, Date dateHired, double annualSalary) {
        super(name, department, dateHired);
        this.annualSalary = annualSalary;
        payroll = annualSalary/PAY_PERIODS;
    }
    
    /**
    Compares the profiles of two full time employees.
    
    @return true if the full time employees share the same profile, false if the 
    profiles are different or the parameter is not an instance of Fulltime
    */
    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    /**
    Creates string representation of a full time employee's attributes with their annual salary. 
    Fulltime's attributes also contains Employee's attributes.
    
    @return string representation of Employee and Fulltime
    */
    @Override
    public String toString() {
        DecimalFormat formatter = new DecimalFormat("#,###.00");
        String formatSalary = formatter.format(annualSalary);
        return super.toString() + "::FULL TIME::Annual Salary $" + formatSalary;
    } 

    /**
    Calculates the payment for a full time employee. The bi weekly pay period conversion for 
    annual salary is handled by this function. 
     
    */
    @Override
    public void calculatePayment() {
        this.setPayment(payroll);
    }
    
    /**
    Returns the salary a full time employee is to receive annually.
    
    @return the annualSalary
    */
    public double getAnnualSalary() {
        return annualSalary;
    }
    
    /**
    Gets the amount a full time employee is to receive per payroll period.
    
    @return payroll the amount for payroll
    */
    public double getPayroll() {
        return payroll;
    }
    
    /**
    Sets the salary a full time employee is to receive annually.
    
    @param salary the annual salary to set for the full time employee
    */
    public void setAnnualSalary(double salary) {
        this.annualSalary = salary;
    }
    
    /**
    Sets the amount a full time employee is to receive during a payroll process
    
    @param payroll the amount to set 
    */
    public void setPayroll(double payroll) {
        this.payroll = payroll;
    }
    
}
