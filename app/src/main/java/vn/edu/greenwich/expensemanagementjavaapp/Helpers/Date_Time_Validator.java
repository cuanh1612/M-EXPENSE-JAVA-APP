package vn.edu.greenwich.expensemanagementjavaapp.Helpers;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Date_Time_Validator {
    public static boolean isValidDate(String inDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(inDate.trim());
        } catch (ParseException pe) {
            return false;
        }
        return true;
    }

    public static boolean isValidTime(String inTime){
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
        timeFormat.setLenient(false);
        try {
            timeFormat.parse(inTime.trim());
        } catch (ParseException pe) {
            return false;
        }
        return true;
    }
}
