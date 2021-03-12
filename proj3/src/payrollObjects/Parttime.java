package payrollObjects;

/**
Extends the Employee class and includes specific data and operations to a part time employee.
Part time employees contain an hourly pay rate and an amount of hours worked. Parttime employees
can be paid overtime for the ranges 81 to 100 hours (inclusive).   
@author Matthew Brandao, Armit Patel
*/
public class Parttime extends Employee {
    private double hourlyRate;
    private int hoursWorked;
    
    /**
    Creates an instance of Parttime. A Parttime employee is also an instance of Employee.
     
    @param name name of the part time employee
    @param department department the part time employee belongs to
    @param dateHired the date the part time employee was hired
    @param hoursWorked the hours the part time employee worked
    */
    public Parttime(String name, String department, Date dateHired, int hoursWorked) {
        super(name, department, dateHired);
        this.hoursWorked = hoursWorked;
    }
    
    /**
    Creates an instance of Parttime. A Parttime employee is also an instance of Employee.
     
    @param name name of the part time employee
    @param department department the part time employee belongs to
    @param dateHired the date the part time employee was hired
    @param hourlyRate the hourly rate the part time employee gets paid
    */
    public Parttime(String name, String department, Date dateHired, double hourlyRate) {
        super(name, department, dateHired);
        this.hourlyRate = hourlyRate;
        hoursWorked = 0;
    }

    /**
    Compares the profile of two part time employees.
    
    @return true if the part time employees share the same profile, false if the 
    profiles are different or the parameter is not an instance of Parttime
    */
    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    /**
    Creates string representation of a part time employee's attributes with their hourly rate and hours worked. 
    Parttime's attributes also contains Employee's attributes.
    
    @return string representation of Employee and Parttime
    */
    @Override
    public String toString() {
        return super.toString() + String.format("::PART TIME::Hourly Rate $%,.2f::"
                + "Hours worked this period: %d", hourlyRate, hoursWorked);
    }   
    
    /**
    Calculates the payment for a part time employee by finding the product of 
    the hours worked and the hourly rate of the part time employee. Overtime 
    calculations are handled by this function. 
    */
    @Override
    public void calculatePayment() {
        final double OT_RATE = 1.5;
        final int OT_HOURS = 80;
        final int MAX_HOURS = 100;
        double paymentToSet;
        if (hoursWorked > OT_HOURS && hoursWorked <= MAX_HOURS) {
            double overPay = (hoursWorked - OT_HOURS) * (OT_RATE * hourlyRate);
            double regularPay = OT_HOURS * hourlyRate;
            paymentToSet = overPay + regularPay;
        }
        else 
            paymentToSet = hoursWorked * hourlyRate;        
        this.setPayment(paymentToSet);
    }

    /**
    Get the hours the part time employee has worked.
    *@return the hours worked
    */
    public int getHoursWorked() {
        return hoursWorked;
    }
    
    /**
    *Get the hourly rate the part time employee gets paid.
    *@return the hourly rate
    */
    public double getHourlyRate() {
        return hourlyRate;
    }

    /**
    *Sets the hours the part time employee has worked.
    *@param hoursWorked the hours worked to set
    */
    public void setHoursWorked(int hoursWorked) {
        this.hoursWorked = hoursWorked;
    }
}
