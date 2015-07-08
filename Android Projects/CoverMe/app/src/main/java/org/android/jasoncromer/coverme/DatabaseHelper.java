package org.android.jasoncromer.coverme;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jason on 6/27/15.
 *
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Name
    public static String DATABASE_NAME = "user_database";

    // Current Database Version
    private static final int DATABASE_VERSION = 1;

    // Name of table
    private static final String USER_TABLE = "user";

    // Keys used in table
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_COMPANY_LOCATION = "company_location";
    private static final String KEY_COMPANY_NAME = "company_name";
    private static final String KEY_DAYS_COVERED = "days_covered";
    private static final String KEY_HOURS_COVERED = "hours_covered";

    // Create table String
    private static final String CREATE_USER_TABLE = "CREATE TABLE "
            + USER_TABLE + "(" + KEY_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_NAME + " TEXT,"
            + KEY_COMPANY_LOCATION + " TEXT,"
            + KEY_COMPANY_NAME + " TEXT,"
            + KEY_DAYS_COVERED + " TEXT,"
            + KEY_HOURS_COVERED + " TEXT );";



    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create User table
        db.execSQL(CREATE_USER_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop table if it already exists
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_USER_TABLE);
        onCreate(db);
    }



    public long addUserDetails(UserModel user) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Creating content values
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, user.userName);
        values.put(KEY_COMPANY_LOCATION, user.userCompanyLocation);
        values.put(KEY_COMPANY_NAME, user.userCompanyName);
        values.put(KEY_DAYS_COVERED, user.userDayCovered);
        values.put(KEY_HOURS_COVERED, user.userHoursCovered);

        // Insert row in user table
        long insert = db.insert(USER_TABLE, null, values);

        return insert;
    }



    public int updateEntry(UserModel user) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Creating content values
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, user.userName);
        values.put(KEY_COMPANY_LOCATION, user.userCompanyLocation);
        values.put(KEY_COMPANY_NAME, user.userCompanyName);
        values.put(KEY_DAYS_COVERED, user.userDayCovered);
        values.put(KEY_HOURS_COVERED, user.userHoursCovered);

        // Update row in user table
        return db.update(USER_TABLE, values, KEY_ID + " = ?", new String[] {String.valueOf(user.userID) });
    }



    public void deleteEntry(long id) {
        // Delete a row in users table based on id
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(USER_TABLE, KEY_ID + " = ?", new String[] {String.valueOf(id) });
    }


    public long getOldestRowID() {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT * FROM " + USER_TABLE;
        Cursor c = db.rawQuery(selectQuery, null);
        long rowID = 0;

        if(c != null) {
            c.moveToFirst();
            //gets first column in table which is id. getLong refers to the column
            rowID = c.getLong(0);
        }

        return rowID;
    }



    public UserModel getUser(long id) {
        SQLiteDatabase db = this.getWritableDatabase();

        // SELECT * FROM users WHERE id = ?
        String selectQuery = "SELECT * FROM " + USER_TABLE + " WHERE "
                + KEY_ID + " = " + id;
        Log.d("Tag", selectQuery);

        Cursor thisCursor = db.rawQuery(selectQuery, null);

        if(thisCursor != null)
            thisCursor.moveToFirst();

        UserModel user = new UserModel();
        user.userID = thisCursor.getInt(thisCursor.getColumnIndex(KEY_ID));
        user.userCompanyLocation = thisCursor.getString(thisCursor.getColumnIndex(KEY_COMPANY_LOCATION));
        user.userCompanyName = thisCursor.getString(thisCursor.getColumnIndex(KEY_COMPANY_NAME));
        user.userDayCovered = thisCursor.getString(thisCursor.getColumnIndex(KEY_DAYS_COVERED));
        user.userHoursCovered = thisCursor.getString(thisCursor.getColumnIndex(KEY_HOURS_COVERED));

        return user;
    }



    public List<UserModel> getAllUsersList() {
        List<UserModel> usersArrayList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + USER_TABLE;
        Log.d("Tag", selectQuery);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor thisCursor = db.rawQuery(selectQuery, null);

        // Loop through all rows and add to list
        if (thisCursor.moveToFirst()) {
            do {
                UserModel user = new UserModel();
                user.userID = thisCursor.getInt(thisCursor.getColumnIndex(KEY_ID));
                user.userCompanyLocation = thisCursor.getString(thisCursor.getColumnIndex(KEY_COMPANY_LOCATION));
                user.userCompanyName = thisCursor.getString(thisCursor.getColumnIndex(KEY_COMPANY_NAME));
                user.userDayCovered = thisCursor.getString(thisCursor.getColumnIndex(KEY_DAYS_COVERED));
                user.userHoursCovered = thisCursor.getString(thisCursor.getColumnIndex(KEY_HOURS_COVERED));

                // Add to User List
                usersArrayList.add(user);
            } while (thisCursor.moveToNext());
        }

        return usersArrayList;
    }









}
