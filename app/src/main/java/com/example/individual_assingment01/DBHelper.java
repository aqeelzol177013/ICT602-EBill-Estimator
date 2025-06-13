package com.example.individual_assingment01;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "electricity_bill.db";
    public static final int DB_VERSION = 1;

    public static final String TABLE_BILLS = "bills";
    public static final String COL_MONTH = "month";
    public static final String COL_UNITS = "units";
    public static final String COL_REBATE = "rebate";
    public static final String COL_TOTAL = "total_charge";
    public static final String COL_FINAL = "final_cost";

    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_BILLS + "(" +
            COL_MONTH + " TEXT, " +
            COL_UNITS + " REAL, " +
            COL_REBATE + " REAL, " +
            COL_TOTAL + " REAL, " +
            COL_FINAL + " REAL);";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BILLS);
        onCreate(db);
    }

    public void insertBill(String month, double units, double rebate, double total, double finalCost) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_MONTH, month);
        values.put(COL_UNITS, units);
        values.put(COL_REBATE, rebate);
        values.put(COL_TOTAL, total);
        values.put(COL_FINAL, finalCost);
        db.insert(TABLE_BILLS, null, values);
        db.close();
    }

    public Cursor getAllBills() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_BILLS, null, null, null, null, null, null);
    }
    public Cursor getBillByMonth(String month) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_BILLS, null, COL_MONTH + " = ?", new String[]{month}, null, null, null);
    }
}