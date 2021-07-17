package com.example.basicbankingapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Calendar;

public class CustomerDatabase extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "customer_list.db";
    public static final String TABLE_NAME = "customer_data";
    public static final String COL1 = "ID";
    public static final String COL2 = "NAME";
    public static final String COL3 = "EMAIL";
    public static final String COL4 = "BALANCE";

    public static final String TRANSFER_DATABASE_NAME = "transfer_list.db";
    public static final String TRANSFER_TABLE_NAME = "transfer_data";
    public static final String COL01 = "DATE";
    public static final String COL02 = "SENDER";
    public static final String COL03 = "RECIPIENT";
    public static final String COL04 = "TRANSFER_AMOUNT";

    public CustomerDatabase(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY, NAME TEXT, " +
                " EMAIL TEXT, BALANCE TEXT)");
        db.execSQL("CREATE TABLE " +TRANSFER_TABLE_NAME+" (DATE TEXT PRIMARY KEY, " +
                "SENDER TEXT, RECIPIENT TEXT, TRANSFER_AMOUNT TEXT)");
        db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES(0,'John', 'johnrambo@gmail.com', '30024')");
        db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES(1,'Mike', 'mikewisdom@gmail.com', '34002')");
        db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES(2,'Loki', 'lokilawfyson@yahoomail.com', '45050')");
        db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES(3,'Tom', 'tomholland@gmail.com', '26640')");
        db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES(4,'Joseph', 'josephmayor@gmail.com', '50430')");
        db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES(5,'Mary', 'marykingg@gmail.com', '46350')");
        db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES(6,'Peter', 'peterparker@gmail.com', '34530')");
        db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES(7,'Nikolas', 'nikolasmarvin@gmail.com', '57430')");
        db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES(8,'Graham', 'josephmayor@gmail.com', '49660')");
        db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES(9,'Alexander', 'alexanderbell@gmail.com', '80360')");
        db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES(10,'Putin', 'rasputin@gmail.com', '36370')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TRANSFER_TABLE_NAME);
        onCreate(db);
    }

    //  returns cursor pointing towards entire database
    public Cursor getListContents() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }

    //  returns cursor pointing towards particular data
    public Cursor getDataFromList(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE ID = " + (id), null);
    }

    //  returns cursor pointing towards entire database excluding particular data
    public Cursor getDataFromListExcept(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM "+TABLE_NAME+" EXCEPT SELECT * FROM "+
                                                TABLE_NAME +" WHERE ID = " + (id), null);
    }

    public Cursor getTransfersListContents(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TRANSFER_TABLE_NAME, null);
    }

    //  Returns true if transfer was successful, else returns false
    public boolean updateBalance(Cursor sender, Cursor recipient, long amount) {
        SQLiteDatabase db = this.getWritableDatabase();
        long senderPastBalance, recipientPastBalance;
        String senderPastBalanceText, recipientPastBalanceText;

        sender.moveToFirst();
        recipient.moveToFirst();

        senderPastBalance = Long.parseLong(sender.getString(3));
        recipientPastBalance = Long.parseLong(recipient.getString(3));

        if(amount <= senderPastBalance) {
            senderPastBalance -= amount;
            recipientPastBalance += amount;

            senderPastBalanceText = "" + senderPastBalance;
            recipientPastBalanceText = "" + recipientPastBalance;

            int senderId = Integer.parseInt(sender.getString(0));
            int recipientId = Integer.parseInt(recipient.getString(0));

            db.execSQL("update " + TABLE_NAME + " set " + COL4 + " = " + senderPastBalanceText +
                    " where " + COL1 + " = " + senderId);
            db.execSQL("update " + TABLE_NAME + " set " + COL4 + " = " + recipientPastBalanceText +
                    " where " + COL1 + " = " + recipientId);

            updateTransfersTable(sender.getString(1), recipient.getString(1),
                    "" + amount);
            return true;
        }
        else {
            return false;
        }
    }

    public void updateTransfersTable(String senderName, String recipientName, String transferAmount){
        SQLiteDatabase db = this.getWritableDatabase();
        Calendar calendar = Calendar.getInstance();
        String time = "" + calendar.getTime();

        db.execSQL("INSERT INTO " + TRANSFER_TABLE_NAME + " VALUES('" + time + "', '" + senderName + "', " +
                "'" + recipientName + "', '"+transferAmount+"')");
    }
}
