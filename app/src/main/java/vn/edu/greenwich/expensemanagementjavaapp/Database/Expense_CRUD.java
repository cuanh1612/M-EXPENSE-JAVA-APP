package vn.edu.greenwich.expensemanagementjavaapp.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

public class Expense_CRUD {
    //  Defines column and field names of Expense table to help reuse code.
    public static final String TABLE_NAME = "Expense";
    public static final String COL_NAME_ID = "Id";
    public static final String COL_NAME_TYPE = "Type";
    public static final String COL_NAME_DATE = "Date";
    public static final String COL_NAME_TIME = "Time";
    public static final String COL_NAME_AMOUNT = "Amount";
    public static final String COL_NAME_ADDITIONAL_COMMENTS = "AdditionalComments";
    public static final String COL_NAME_LOCATION = "Location";
    public static final String COL_NAME_TRIP_ID = "TripId";

    //Query create table
    public static final String SQL_CREATE_TABLE = "Create table if not exists "+ TABLE_NAME +
            " ("+
            COL_NAME_ID + " integer primary key autoincrement," +
            COL_NAME_TYPE + " text not null," +
            COL_NAME_DATE + " text not null," +
            COL_NAME_TIME + " text not null," +
            COL_NAME_AMOUNT + " integer not null," +
            COL_NAME_ADDITIONAL_COMMENTS + " text," +
            COL_NAME_LOCATION + " text  not null," +
            COL_NAME_TRIP_ID + " integer not null," +
            "foreign key ("+ COL_NAME_TRIP_ID +") references Trip ("+ Trip_CRUD.COL_NAME_ID +")" +
            " on delete cascade" +
            ")";

    //  Query drop table
    public static final String SQL_DELETE_TABLE = "Drop table if exists" + TABLE_NAME;

    //  The getAll method helps to get and return a list of all expenses in the system.
    public static ArrayList<Expense> getAll(Context context){
        //  Create new ArrayList Expenses
        ArrayList<Expense> listExpenses = new ArrayList<>();
        //  Get db helper
        Db_Helper helper = new Db_Helper(context);

        //Get Cursor and move to first to perform a loop that retrieves each expense.
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cs = db.rawQuery("Select * from " + TABLE_NAME, null);
        cs.moveToFirst();

        /*
            Loop until the cursor is after last get each expense and add it into
            ArrayList listExpenses and return that list.
        */
        while (!cs.isAfterLast()){
            int Id = cs.getInt(0);
            String Type = cs.getString(1);
            String Date = cs.getString(2);
            String Time = cs.getString(3);
            int Amount = cs.getInt(4);
            String AdditionalComments = cs.getString(5);
            String Location = cs.getString(6);
            int TripId = cs.getInt(7);
            Expense itemExpense = new Expense(Id, Type, Date, Time, Amount, AdditionalComments, Location, TripId);
            listExpenses.add(itemExpense);
            cs.moveToNext();
        }

        // Close cursor and db helper
        cs.close();
        db.close();

        return listExpenses;
    }

    /*
        The method get Expenses By Trip helps get expenses belonging to the trip with the same
        id as the IdTrip parameter passed in. It includes two arguments, context, and IdTrip.
    */
    public static ArrayList<Expense> getExpensesByTrip(Context context, int IdTrip){
        //  Create new ArrayList Expenses
        ArrayList<Expense> listExpenses = new ArrayList<>();

        //  Get db helper
        Db_Helper helper = new Db_Helper(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        //Get Cursor by query and move to first to perform a loop that retrieves each expense.
        Cursor cs = db.rawQuery("Select * from "+ TABLE_NAME +" where "+ COL_NAME_TRIP_ID +"=" + IdTrip, null);
        cs.moveToFirst();

        /*
            Loop until the cursor is after last get each expense and add it into
            ArrayList listExpenses and return that list.
        */
        while (!cs.isAfterLast()){
            int Id = cs.getInt(0);
            String Type = cs.getString(1);
            String Date = cs.getString(2);
            String Time = cs.getString(3);
            int Amount = cs.getInt(4);
            String AdditionalComments = cs.getString(5);
            String Location = cs.getString(6);
            int TripId = cs.getInt(7);
            Log.i("yyyy", "getExpensesByTrip: " + Id);

            Expense itemExpense = new Expense(  Id,
                                                Type,
                                                Date,
                                                Time,
                                                Amount,
                                                AdditionalComments,
                                                Location,
                                                TripId);
            listExpenses.add(itemExpense);
            cs.moveToNext();
        }

        // Close cursor and db helper
        cs.close();
        db.close();

        return listExpenses;
    }

    /*
        The insert method has input parameters including context, Type, Date,
        Time, Amount, AdditionalComments, String Location, TripId to help
        insert a new expense into the database.
    */
    public static boolean insert(Context context,
                                 String Type,
                                 String Date,
                                 String Time,
                                 int Amount,
                                 String AdditionalComments,
                                 String Location,
                                 int TripId){
        //  Get db helper
        Db_Helper helper = new Db_Helper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        //  Create new ContentValues 'values' contains new expense information.
        ContentValues values = new ContentValues();

        //  Put the data into 'values'
        values.put(COL_NAME_TYPE, Type);
        values.put(COL_NAME_DATE, Date);
        values.put(COL_NAME_TIME, Time);
        values.put(COL_NAME_AMOUNT, Amount);
        values.put(COL_NAME_ADDITIONAL_COMMENTS, AdditionalComments);
        values.put(COL_NAME_LOCATION, Location);
        values.put(COL_NAME_TRIP_ID, TripId);

        //  Insert a new an expense
        long row = db.insert(TABLE_NAME, null, values);

        /*
            Returns true when 'row' is 1 (insert succeeded),
            returns false when 'row' is 0 (insert failed).
        */
        return (row > 0);
    }

    public static boolean reset(Context context){
        //  Get db helper
        Db_Helper helper = new Db_Helper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        //Clear all data in table Expense
        int row = db.delete(TABLE_NAME, null, null);

        /*
            Returns true when 'row' is 1 (clear data succeeded),
            returns false when 'row' is 0 (clear data failed).
        */
        return (row > 0);
    }
}
