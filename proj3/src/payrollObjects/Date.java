package payrollObjects;
import java.util.Calendar;
/**
This class defines the properties and operations of a Date object. A date
consists of a month, day, and year.
@author Matthew Brandao, Armit Patel
*/
public class Date implements Comparable<Date>{
    private int year;
    private int month;
    private int day;

    /**
    Creates an instance of Date and initializes attribute values to the value of
    the string passed. Splits string contents from expected "/" delimiter into
    month, day, and year.
      
    @param date a date represented as a string
    */
    public Date(String date) {
        String[] dateSegments = date.split("/");
        final int FIRST_PARAM = 0;
        final int SECOND_PARAM = 1;
        final int THIRD_PARAM = 2;
        if (dateSegments[FIRST_PARAM].length() > 0 && dateSegments[SECOND_PARAM].length() 
                > 0 && dateSegments[THIRD_PARAM].length() > 0) {
            this.month = Integer.parseInt(dateSegments[FIRST_PARAM]);
            this.day = Integer.parseInt(dateSegments[SECOND_PARAM]);
            this.year = Integer.parseInt(dateSegments[THIRD_PARAM]);
        } 
        else {
            this.month = 0;
            this.day = 0;
            this.year = 0;
        }
    }

    /**
    Creates an instance of Date and initializes attributes representing today's
    date.
    */
    public Date() {
        // Calendar.MONTH returns months 0-11, offset is needed for correct output
        final int MONTH_OFFSET = 1;
        this.month = Calendar.getInstance().get(Calendar.MONTH) + MONTH_OFFSET;
        this.day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        this.year = Calendar.getInstance().get(Calendar.YEAR);
    }
    
    /**
    Compares an instance of Date to a relative instance of Date that is provided as parameter. 
    Determines whether this date is earlier, the same, or greater than the relative date.
      
    @param relativeDate the date this instance of Date will be compare to.
    @return -1 if this date is earlier than the relative date, 0 if this date is
    the same as the relative date, 1 if this date is newer than the relative date
    */
    @Override
    public int compareTo(Date date) {
        final int GREATER = 1;
        final int EQUAL = 0;
        final int LESS = -1;
        int relativeYear = date.getYear();
        int relativeMonth = date.getMonth();
        int relativeDay = date.getDay();
        if (year > relativeYear)
            return GREATER;
        if (year < relativeYear)
            return LESS;
        if (year == relativeYear && month > relativeMonth)
            return GREATER;
        if (year == relativeYear && month < relativeMonth)
            return LESS;
        if (year == relativeYear && month == relativeMonth && day > relativeDay)
            return GREATER;
        if (year == relativeYear && month == relativeMonth && day < relativeDay)
            return LESS;
        else
            return EQUAL;
    }

    /**
    Returns a string representation of the Date in the format 
    month/day/year.
    
    @return the string representation of Date
    */
    @Override
    public String toString() {
        return String.format("%d/%d/%d", month, day, year);
    }

    /**
    Returns an integer value of the year of this instance of Date.
     
    @return the year of the date
    */
    public int getYear() {
        return year;
    }

    /**
    Returns an integer value of the month of this instance of Date.
      
    @return the month of the date
    */
    public int getMonth() {
        return month;
    }

    /**
    Returns an integer value of the day of this instance of Date.
     
    @return the day of the date
    */
    public int getDay() {
        return day;
    }

    /**
    Checks if a date is valid. A date is valid if the year, month, and day are in
    correct formatting. A correct year contains the values 1900 to the value of
    the current year. A correct month contains the values 1 to 12. A correct
    day's upper bound value depends on whether the month is a long month(31),
    medium month(30), short month(28), or a leap year(29). The lower bound for
    day's value in all scenarios is 1.
      
    @return true if the date is valid, false if the date is not valid
    */
    public boolean isValid() {
        boolean leapYear = false;
        final int MONTH_OFFSET = 1;
        final int FIRST_DAY = 1;
        final int LONG_MONTH_DAY = 31;
        final int MEDIUM_MONTH_DAY = 30;
        final int SHORT_MONTH_DAY = 29;
        final int OLDEST_YEAR = 1900, NEWEST_YEAR = 2021;
        final int TODAY_MONTH = Calendar.getInstance().get(Calendar.MONTH) + MONTH_OFFSET;
        final int TODAY_DAY_MONTH = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        final int JANUARY = 1, FEBRUARY = 2, MARCH = 3, APRIL = 4, MAY = 5, 
                    JUNE = 6, JULY = 7, AUGUST = 8, SEPTEMBER = 9, OCTOBER = 10, 
                        NOVEMBER = 11, DECEMBER = 12;
        // valid year
        if (year < OLDEST_YEAR || year > NEWEST_YEAR)
            return false;
        // valid month
        if (month < JANUARY || month > DECEMBER)
            return false;
        /// valid 31 months
        if (month == JANUARY || month == MARCH || month == MAY || month == JULY 
                || month == AUGUST || month == OCTOBER || month == DECEMBER) {
            if (day < FIRST_DAY || day > LONG_MONTH_DAY)
                return false;
        }
        // valid 30 day months
        if (month == APRIL || month == JUNE || month == SEPTEMBER || month == NOVEMBER) {
            if (day < FIRST_DAY || day > MEDIUM_MONTH_DAY)
                return false;
        }
        // check if date is past today's date
        if (((month > TODAY_MONTH) && year == NEWEST_YEAR) || ((day > TODAY_DAY_MONTH)
                && (month == TODAY_MONTH) && year == NEWEST_YEAR))
            return false;
        // leap year
        if (month == FEBRUARY) {
            leapYear = isLeapYear();
            if (leapYear == true && (day < FIRST_DAY || day > SHORT_MONTH_DAY))
                return false;
            else if (leapYear == false && (day < FIRST_DAY || day >= SHORT_MONTH_DAY))
                return false;
        }
        // date is valid
        return true;
    }
    
    /**
    Determines if the integer value of this instance of Date's year attribute 
    is a leap year or not.
      
    @return leapYear true if the year attribute is a leap year, false if the year 
    attribute is not a leap year
    */
    private boolean isLeapYear() {
        boolean leapYear = false;
        final int QUADRENNIAL = 4;
        final int CENTENNIAL = 100;
        final int QUATERCENTENNIAL = 400;
        if (year % QUADRENNIAL == 0) {
            if (year % CENTENNIAL == 0) {
                if (year % QUATERCENTENNIAL == 0) {
                    leapYear = true;
                }
            } else {
                leapYear = true;
            }
        } else {
            leapYear = false;
        }
        return leapYear;
    }

    /**
    The testbed main to thoroughly test the isValid() method with created test
    cases.
      
    @param args
    */
    public static void main(String[] args) {
        System.out.println("----------Test Case # 1----------");
        System.out.print("Input: 31/2/2000\tExpected Output: false" 
                                + "\tTest Result: " + new Date("31/2/2000").isValid());
        if (new Date("31/2/2000").isValid() == false)
            System.out.println("  PASSED");
        else
            System.out.println("  FAILED");
        System.out.println("\n----------Test Case # 2----------");
        System.out.print("Input: 13/2/2020\tExpected Output: false" 
                                + "\tTest Result: " + new Date("13/2/2020").isValid());
        if (new Date("13/2/2020").isValid() == false)
            System.out.println("  PASSED");
        else
            System.out.println("  FAILED");
        System.out.println("\n----------Test Case # 3----------");
        System.out.print("Input: 2/29/2021\tExpected Output: false" 
                                + "\tTest Result: " + new Date("2/29/2021").isValid());
        if (new Date("2/29/2021").isValid() == false)
            System.out.println("  PASSED");
        else
            System.out.println("  FAILED");
        System.out.println("\n----------Test Case # 4----------");
        System.out.print("Input: 4/31/2009\tExpected Output: false" 
                                + "\tTest Result: " + new Date("4/31/2009").isValid());
        if (new Date("4/31/2009").isValid() == false)
            System.out.println("  PASSED");
        else
            System.out.println("  FAILED");
        System.out.println("\n----------Test Case # 5----------");
        System.out.print("Input: 3/32/2009\tExpected Output: false" 
                                + "\tTest Result: " + new Date("3/32/2009").isValid());
        if (new Date("3/32/2009").isValid() == false)
            System.out.println("  PASSED");
        else
            System.out.println("  FAILED");
        System.out.println("\n----------Test Case # 6----------");
        System.out.print("Input: 3/31/1800\tExpected Output: false" 
                                + "\tTest Result: " + new Date("3/31/1800").isValid());
        if (new Date("3/31/1800").isValid() == false)
            System.out.println("  PASSED");
        else
            System.out.println("  FAILED");
        System.out.println("\n----------Test Case # 7----------");
        System.out.print("Input: 10/30/2022\tExpected Output: false" 
                                + "\tTest Result: " + new Date("10/30/2022").isValid());
        if (new Date("10/30/2022").isValid() == false )
            System.out.println("  PASSED");
        else
            System.out.println("  FAILED");
        System.out.println("\n----------Test Case # 8----------");
        System.out.print("Input: 3/30/2021\tExpected Output: false" 
                                + "\tTest Result: " + new Date("3/30/2021").isValid());
        if (new Date("3/30/2021").isValid() == false)
            System.out.println("  PASSED");
        else
            System.out.println("  FAILED");
        System.out.println("\n----------Test Case # 9----------");
        System.out.print("Input: 12/31/1899\tExpected Output: false" 
                                + "\tTest Result: " + new Date("12/31/1899").isValid());
        if (new Date("12/31/1899").isValid() == false)
            System.out.println("  PASSED");
        else
            System.out.println("  FAILED");
        System.out.println("\n----------Test Case # 10----------");
        System.out.print("Input: 2/8/2021\t\tExpected Output: false" 
                                + "\tTest Result: " + new Date("2/8/2021").isValid());
        if (new Date("2/8/2021").isValid() == false)
            System.out.println("  PASSED");
        else
            System.out.println("  FAILED");
        // this case below will fail when ran past 2/7/2021
        System.out.println("\n----------Test Case # 11----------");
        System.out.print("Input: 3//2021\t\tExpected Output: false" 
                                + "\tTest Result: " + new Date("3//2021").isValid());
        if (new Date("3//2021").isValid() == false)
            System.out.println("  PASSED");
        else
            System.out.println("  FAILED");
        System.out.println("\n----------Test Case # 12----------");
        System.out.print("Input: 2/30/2020\tExpected Output: false" 
                                + "\tTest Result: " + new Date("2/30/2020").isValid());
        if (new Date("2/30/2020").isValid() == false)
            System.out.println("  PASSED");
        else
            System.out.println("  FAILED");
        System.out.println("\n----------Test Case # 13----------");
        System.out.print("Input: 1/0/2019\t\tExpected Output: false" 
                                + "\tTest Result: " + new Date("1/0/2019").isValid());
        if (new Date("1/0/2019").isValid() == false)
            System.out.println("  PASSED");
        else
            System.out.println("  FAILED");
        System.out.println("\n----------Test Case # 14----------");
        System.out.print("Input: 0/30/2020\tExpected Output: false" 
                                + "\tTest Result: " + new Date("0/30/2020").isValid());
        if (new Date("0/30/2020").isValid() == false)
            System.out.println("  PASSED");
        else
            System.out.println("  FAILED");
        System.out.println("\n----------Test Case # 15----------");
        System.out.print("Input: 4/15/0000\tExpected Output: false" 
                                + "\tTest Result: " + new Date("4/15/0000").isValid());
        if (new Date("4/15/0000").isValid() == false)
            System.out.println("  PASSED");
        else
            System.out.println("  FAILED");
        System.out.println("\n----------Test Case # 16----------");
        System.out.print("Input: 2/29/2016\tExpected Output: true" 
                                + "\tTest Result: " + new Date("2/29/2016").isValid());
        if (new Date("2/29/2016").isValid() == true)
            System.out.println("  PASSED");
        else
            System.out.println("  FAILED");
        System.out.println("\n----------Test Case # 17----------");
        System.out.print("Input: 5/17/2020\tExpected Output: true" 
                                + "\tTest Result: " + new Date("5/17/2020").isValid());
        if (new Date("5/17/2020").isValid() == true)
            System.out.println("  PASSED");
        else
            System.out.println("  FAILED");
        System.out.println("\n----------Test Case # 18----------");
        System.out.print("Input: 2/7/2021\t\tExpected Output: true" 
                                + "\tTest Result: " + new Date().isValid());
        if (new Date().isValid() == true)
            System.out.println("  PASSED");
        else
            System.out.println("  FAILED");
        System.out.println("\n----------Test Case # 19----------");
        System.out.print("Input: 002/029/02008\tExpected Output: true" 
                                + "\tTest Result: " + new Date("002/029/02008").isValid());
        if (new Date("002/029/02008").isValid() == true)
            System.out.println("  PASSED");
        else
            System.out.println("  FAILED");
        System.out.println("\n----------Test Case # 20----------");
        System.out.print("Input: 1/1/1900\t\tExpected Output: true" 
                                + "\tTest Result: " + new Date("1/1/1900").isValid());
        if (new Date("1/1/1900").isValid() == true)
            System.out.println("  PASSED");
        else
            System.out.println("  FAILED");
    }
}
