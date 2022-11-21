package vn.edu.greenwich.expensemanagementjavaapp.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

public class Trip_CRUD {
    //Name table and name col
    public static final String COL_NAME_ID = "Id";
    public static final String TABLE_NAME = "Trip";
    public static final String COL_NAME_IMAGE = "Image";
    public static final String COL_NAME_NAME = "Name";
    public static final String COL_NAME_DESTINATION = "Destination";
    public static final String COL_NAME_DATE = "Date";
    public static final String COL_NAME_DESCRIPTION = "Description";
    public static final String COL_NAME_NOTE = "Note";
    public static final String COL_NAME_TOPIC = "Topic";
    public static final String COL_NAME_REQUIRED_RISK_ASSESSMENT = "RequiredRiskAssessment";

    //Query create table
    public static final String SQL_CREATE_TABLE = "Create table if not exists " + TABLE_NAME +
            " (" +
            COL_NAME_ID + " integer primary key autoincrement," +
            COL_NAME_NAME + " text not null," +
            COL_NAME_DESTINATION + " text not null," +
            COL_NAME_DATE + " text not null," +
            COL_NAME_DESCRIPTION + " text," +
            COL_NAME_NOTE + "  text not null," +
            COL_NAME_TOPIC + "  text not null," +
            COL_NAME_REQUIRED_RISK_ASSESSMENT + " integer not null," +
            COL_NAME_IMAGE + " text" +
            ")";

    //Query drop table
    public static final String SQL_DELETE_TABLE = "Drop table if exists" + TABLE_NAME;

    public static Trip getDetail(Context context, int IdTrip){
        Db_Helper helper = new Db_Helper(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cs = db.rawQuery("Select * from "+ TABLE_NAME + " where " + COL_NAME_ID + "=" + IdTrip, null);
        cs.moveToFirst();
        Trip dataTrip = new Trip(0, null, null, null, null, null, null, 0, null);
        while (!cs.isAfterLast()){
            int Id = cs.getInt(0);
            String Name = cs.getString(1);
            String Destination = cs.getString(2);
            String Date = cs.getString(3);
            String Description = cs.getString(4);
            String Note = cs.getString(5);
            String Topic = cs.getString(6);
            int RequiredRiskAssessment = cs.getInt(7);
            String Image = cs.getString(8);
            dataTrip = new Trip(Id, Name, Destination, Date, Description, Note, Topic, RequiredRiskAssessment, Image);
            cs.moveToNext();
        }
        cs.close();
        db.close();

        return dataTrip;
    }

    public static ArrayList<Trip> getAll(Context context){
        ArrayList<Trip> listTrips = new ArrayList<>();
        Db_Helper helper = new Db_Helper(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cs = db.rawQuery("Select * from " +TABLE_NAME, null);
        cs.moveToFirst();
        while (!cs.isAfterLast()){
            int Id = cs.getInt(0);
            String Name = cs.getString(1);
            String Destination = cs.getString(2);
            String Date = cs.getString(3);
            String Description = cs.getString(4);
            String Note = cs.getString(5);
            String Topic = cs.getString(6);
            int RequiredRiskAssessment = cs.getInt(7);
            String Image = cs.getString(8);
            Trip itemTrip = new Trip(Id, Name, Destination, Date, Description, Note, Topic, RequiredRiskAssessment, Image);
            listTrips.add(itemTrip);
            cs.moveToNext();
        }
        cs.close();
        db.close();

        return listTrips;
    }

    public static boolean insert(Context context,
                                 String Name,
                                 String Destination,
                                 String Date,
                                 String Description,
                                 String Note,
                                 String Topic,
                                 int RequiredRiskAssessment,
                                 String Image){
        Db_Helper helper = new Db_Helper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COL_NAME_NAME, Name);
        values.put(COL_NAME_DESTINATION, Destination);
        values.put(COL_NAME_DATE, Date);
        values.put(COL_NAME_DESCRIPTION, Description);
        values.put(COL_NAME_NOTE, Note);
        values.put(COL_NAME_TOPIC, Topic);
        values.put(COL_NAME_REQUIRED_RISK_ASSESSMENT, RequiredRiskAssessment);
        values.put(COL_NAME_IMAGE, Image);

        long row = db.insert("Trip", null, values);

        return (row > 0);
    }

    public static boolean update (Context context, Trip trip){
        Db_Helper helper = new Db_Helper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        //create contentValues to update

        ContentValues values = new ContentValues();
        values.put(COL_NAME_NAME, trip.getName());
        values.put(COL_NAME_DESTINATION, trip.getDestination());
        values.put(COL_NAME_DATE, trip.getDate());
        values.put(COL_NAME_DESCRIPTION, trip.getDescription());
        values.put(COL_NAME_NOTE, trip.getNote());
        values.put(COL_NAME_TOPIC, trip.getTopic());
        values.put(COL_NAME_REQUIRED_RISK_ASSESSMENT, trip.getRequiredRiskAssessment());
        values.put(COL_NAME_IMAGE, trip.getImage());

        //Update trip
        int row = db.update(TABLE_NAME, values, COL_NAME_ID + "=?", new String[]{trip.getId() + ""});

        return  (row > 0);
    }

    public static boolean delete(Context context, int IdTrip){
        Db_Helper helper = new Db_Helper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        int row = db.delete(TABLE_NAME, COL_NAME_ID + "=?", new String[]{IdTrip + ""});
        return (row > 0);
    }

    public static boolean reset(Context context){
        Db_Helper helper = new Db_Helper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        int row = db.delete(TABLE_NAME, null, null);
        return (row > 0);
    }

    public static ArrayList<Trip> filterList(Context context, String TripName, String TripDestination, String TripDate){
        ArrayList<Trip> listTrips = new ArrayList<>();
        Db_Helper helper = new Db_Helper(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor cs = db.rawQuery("Select * from "+ TABLE_NAME +" where" +
                " "+ COL_NAME_NAME +" like " + "'%" + TripName + "%'" +
                " and "+ COL_NAME_DESTINATION +" like" + "'%" + TripDestination + "%'" +
                " and "+ COL_NAME_DATE +" like" + "'%" + TripDate + "%'", null);

        cs.moveToFirst();
        while (!cs.isAfterLast()){
            int Id = cs.getInt(0);
            String Name = cs.getString(1);
            String Destination = cs.getString(2);
            String Date = cs.getString(3);
            String Description = cs.getString(4);
            String Note = cs.getString(5);
            String Topic = cs.getString(6);
            int RequiredRiskAssessment = cs.getInt(7);
            String Image = cs.getString(8);
            Trip itemTrip = new Trip(Id, Name, Destination, Date, Description, Note, Topic, RequiredRiskAssessment, Image);
            listTrips.add(itemTrip);
            cs.moveToNext();
        }
        cs.close();
        db.close();

        return listTrips;
    }
}
