package vn.edu.greenwich.expensemanagementjavaapp.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Db_Helper extends SQLiteOpenHelper {
    /*
        Configure LiteSQL database with database name "M-Expense_App",
        factory is null and have initial version 1
    */
    public Db_Helper(@Nullable Context context) {
        super(context, "M-Expense_App", null, 1);
    }

    /*
        The onCreate function is a provided function when extending SQLiteOpenHelper
        and is overridden with an input parameter of an SQLiteDatabase to help
        create two tables Trip and Expense.
    */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(Trip_CRUD.SQL_CREATE_TABLE);
        sqLiteDatabase.execSQL(Expense_CRUD.SQL_CREATE_TABLE);
    }

    /*
        The onUpgrade function is available when extending SQLiteOpenHelper is overridden
        with an input parameter including SQLiteDatabase and two version numbers before
        and after the change. The two command lines inside will delete the
        Trip and Expense tables every time the version is changed.
    */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i1, int i2) {
        sqLiteDatabase.execSQL(Trip_CRUD.SQL_DELETE_TABLE);
        sqLiteDatabase.execSQL(Expense_CRUD.SQL_DELETE_TABLE);
    }
}


