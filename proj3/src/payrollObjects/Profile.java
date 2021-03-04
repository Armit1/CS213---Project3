package payrollObjects;

/**
Defines the common data and operations for all employee type. Profiles are 
generally used as an attribute of type Employee.
@author Matthew Brandao, Armit Patel
*/
public class Profile {
    private String name;
    private String department;
    private Date dateHired;

    /**
    Creates an instance of Profile. Profiles consist of a name, department title,
    and the date an employee was hired. 
    
    @param name       name of the employee
    @param department department the employee belongs to
    @param dateHired  the date the employee was hired
    */
    public Profile(String name, String department, Date dateHired) {
        this.name = name;
        this.department = department;
        this.dateHired = dateHired;
    }

    /**
    Creates a string representation of a profile's attributes.
    
    @return string representation of name, department, and date hired
    */
    @Override
    public String toString() {
        return String.format("%s::%s::%s", name, department, dateHired);
    }

    /**
    Compares to an instance of Profile.
    
    @return true if the profile's name, department, and date are lexicographically
    equal, false if they are not equal or the object is not an instance of Profile 
    */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Profile) {
            Profile profile = (Profile) obj;
            return profile.getName().equals(this.name) && 
                     profile.getDepartment().equals(this.department) && 
                       profile.getDateHired().compareTo(this.dateHired) == 0;
        }
        return false;
    }

    /**
    Gets the name of employee stored in profile.
    
    @return the name of employee
    */
    public String getName() {
        return name;
    }

    /**
    Gets the department of employee stored in profile.
    
    @return the department of employee
    */
    public String getDepartment() {
        return department;
    }

    /**
    Gets the date employed was hired stored in profile.
    
    @return the date employee was hired
    */
    public Date getDateHired() {
        return dateHired;
    }

    /**
    Determine if all the profile fields are valid. The name is valid if it is 
    not empty. The department is valid if it is not empty and contains CS, ECE,
    or IT. The date is valid if date.isValid() returns true.
    
    @return true if profile is valid, false if profile is not valid
    */
    public boolean isValid() {
        if (name.isEmpty() || department.isEmpty() || dateHired.isValid() == false)
            return false;
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
}
