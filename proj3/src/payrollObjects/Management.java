package payrollObjects;
import java.text.DecimalFormat;
/**
Extends the Fulltime class and includes specific data and operations to a full-time
employee with a management role. Managers recieve compensation on top of their annual 
salary depending on their management title.
@author Matthew Brandao, Armit Patel
*/
public class Management extends Fulltime {
    private double compensation;
    private String managerTitle;

    /**
    Creates an instance of Management. Management is also an instance of Fulltime.
    A manager has all the attributes of a full time employee along with a bonus compensation.
     
    @param name name of the manager
    @param department department the manager belongs to
    @param dateHired the date the manager was hired
    @param annualSalary the amount of the manager's annual salary
    @param manageRole the role ID of the manager
    */
    public Management(String name, String department, Date dateHired, double annualSalary, int manageRole) {
        super(name, department, dateHired, annualSalary);
        compensate(manageRole); // init compensation and manager title
        this.setPayroll(this.getPayroll() + compensation);
    }

    /**
    Compares the profile of two managers.
    
    @return true if the full managers share the same profile, false if the 
    profiles are different or the parameter is not an instance of Management
    */
    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
    
    /**
    Creates string representation of a manager's attributes with their manager compensation. 
    A manager's attributes also contains a full time employee's attributes. 
    
    @return string string representation of an managers's attributes
    */
    @Override
    public String toString() {
        DecimalFormat formatter = new DecimalFormat("#,##0.00");
        String formatCompensation = formatter.format(compensation);
        return super.toString() + "::" + managerTitle + " Compensation $" + formatCompensation;
    }  
    
    /**
    Calculates the payment for a manager employee.
    */
    @Override
    public void calculatePayment() {
        super.calculatePayment();
    }

    /**
    Assigns the appropriate compensation amount to the manager based on the manager's role.
     
    @param manageRole the ID of the manager role
    */
    private void compensate(int manageRole) {
        final int PAY_PERIODS = 26;
        final int MANAGER_ID = 1, HEAD_ID = 2, DIR_ID = 3;
        final double MANAGER_COMP = 5000, HEAD_COMP = 9500, DIR_COMP = 12000;
        switch (manageRole) {
        case MANAGER_ID:
            compensation = MANAGER_COMP;
            managerTitle = "Manager";
            break;
        case HEAD_ID:
            compensation = HEAD_COMP;
            managerTitle = "DepartmentHead";
            break;
        case DIR_ID:
            compensation = DIR_COMP;
            managerTitle = "Director";
        }
        compensation /= PAY_PERIODS;
    }

    /**
    Returns the compensation a manager is to receive annually.
    
    @return the annual compensation
    */
    public double getCompensation() {
        return compensation;
    }
}
