package com.codeshastra.coderr.provideameal;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by dhruvesh mehta on 16-09-2016.
 */
public class MySQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_COMMENTS = "message";
    public static final String MESSAGE_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_ADDRESS = "address";
    public static final String COLUMN_NUMBER = "contact";
    public static final String COLUMN_MEALS = "meals";
    //public static final String COLUMN_TYPE = "type";
    //public static final String COLUMN_ETC = "etc";

    private static final String DATABASE_NAME = "messages.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_COMMENTS + "(" + MESSAGE_ID
            + " integer primary key autoincrement, " + COLUMN_ADDRESS
                + " text not null, " + COLUMN_NAME
            + " text not null, " + COLUMN_NUMBER + " text not null, "+ COLUMN_MEALS + " text not null " + ");";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMMENTS);
        onCreate(db);
    }
}
