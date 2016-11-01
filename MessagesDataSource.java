package com.codeshastra.coderr.provideameal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MessagesDataSource {
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = {MySQLiteHelper.MESSAGE_ID,
            MySQLiteHelper.COLUMN_ADDRESS, MySQLiteHelper.COLUMN_NAME, MySQLiteHelper.COLUMN_NUMBER, MySQLiteHelper.COLUMN_MEALS, MySQLiteHelper.COLUMN_EMAIL};

    public MessagesDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public MessagesDataSource() {
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Message createMessage(String title, String description, String time, String meals, String email) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_NAME, title);
        values.put(MySQLiteHelper.COLUMN_ADDRESS, description);
        values.put(MySQLiteHelper.COLUMN_NUMBER, time);
        values.put(MySQLiteHelper.COLUMN_MEALS, meals);
        values.put(MySQLiteHelper.COLUMN_EMAIL, email);
        long insertId = database.insert(MySQLiteHelper.TABLE_COMMENTS, null,
                values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_COMMENTS,
                allColumns, MySQLiteHelper.MESSAGE_ID + " = " + insertId, null,
                null, null, null, null);
        cursor.moveToFirst();
        Message newMessage = cursorToMessage(cursor);
        Log.e("Create Message", "New DB entry");
        cursor.close();
        return newMessage;
    }

    public void deleteMessage(Message message) {
        long id = message.getId();
        System.out.println("Comment deleted with id: " + id);
        database.delete(MySQLiteHelper.TABLE_COMMENTS, MySQLiteHelper.MESSAGE_ID
                + " = " + id, null);
    }

    public List<Message> getAllMessages() {
        List<Message> messages = new ArrayList<Message>();
        Cursor cursor = database.query(MySQLiteHelper.TABLE_COMMENTS,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Message message = cursorToMessage(cursor);
            messages.add(message);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return messages;
    }

    public Message getMessageFromMessage(String messageString) {
        //Add second parameter 'type' for each fragment in dash
        Cursor cursor = database.query(MySQLiteHelper.TABLE_COMMENTS,
                allColumns, null, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Message message = cursorToMessage(cursor);
            if (message.getAddress().equals(messageString)) {
                return message;
            }
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return null;
    }

    private Message cursorToMessage(Cursor cursor) {
        Message message = new Message();
        message.setId(cursor.getLong(0));
        message.setAddress(cursor.getString(1));
        message.setName(cursor.getString(2));
        message.setNumber(cursor.getString(3));
        message.setMeals(cursor.getString(4));
        message.setEmail(cursor.getString(5));
        return message;
    }

    public void deleteAll() {
        // Delete All Rows
        database.delete(MySQLiteHelper.TABLE_COMMENTS, null, null);
        // database.execSQL("DELETE FROM message"); //delete all rows in a table
        Log.e("", "Deleted all user info from sqlite");
        database.close();
    }
}
