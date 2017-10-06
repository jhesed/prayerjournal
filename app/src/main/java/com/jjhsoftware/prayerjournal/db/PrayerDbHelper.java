package com.jjhsoftware.prayerjournal.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by jhesed on 7/29/2017.
 */


public class PrayerDbHelper extends SQLiteOpenHelper {

    final String TAG = "PrayerDBHelper";

    public PrayerDbHelper(Context context) {
        super(context, PrayerContract.DB_NAME, null, PrayerContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createTable = "CREATE TABLE " + PrayerContract.PrayerEntry.TABLE + " ( " +
                PrayerContract.PrayerEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PrayerContract.PrayerEntry.COL_TITLE + " TEXT NOT NULL, " +
                PrayerContract.PrayerEntry.COL_CONTENT + " TEXT NOT NULL, " +
                PrayerContract.PrayerEntry.COL_DAY + " INTEGER DEFAULT NULL, " +
                PrayerContract.PrayerEntry.COL_IS_REMINDER_ENABLED + " INTEGER DEFAULT NULL, " +
                PrayerContract.PrayerEntry.COL_REMINDER_TIME + " TIMESTAMP DEFAULT NULL, " +
                PrayerContract.PrayerEntry.COL_IS_DONE + " INTEGER DEFAULT 0, " +
                PrayerContract.PrayerEntry.COL_IS_ANSWERED + " INTEGER DEFAULT 0, " +
                PrayerContract.PrayerEntry.COL_DATE_CREATED + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                PrayerContract.PrayerEntry.COL_DATE_MODIFIED + " TIMESTAMP DEFAULT NULL, " +
                PrayerContract.PrayerEntry.COL_DATE_LAST_SYNC + " TIMESTAMP DEFAULT NULL, " +
                PrayerContract.PrayerEntry.COL_IS_SYNCED + " INTEGER DEFAULT NULL); ";

        Log.d(TAG, createTable);
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + PrayerContract.PrayerEntry.TABLE);
        onCreate(db);
    }

    public Cursor selectAll(int isDone, int day, int isAnswered) {

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + PrayerContract.PrayerEntry._ID + ", " +
                PrayerContract.PrayerEntry.COL_TITLE +
                " FROM " + PrayerContract.PrayerEntry.TABLE + " WHERE " +
                PrayerContract.PrayerEntry.COL_IS_DONE + " = " + isDone + " AND " +
                PrayerContract.PrayerEntry.COL_DAY + " = " + day + " AND " +
                PrayerContract.PrayerEntry.COL_IS_ANSWERED + " = " + isAnswered + ";";

        return db.rawQuery(query, null);
    }

    public Cursor select(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + PrayerContract.PrayerEntry.COL_TITLE +
                ", " + PrayerContract.PrayerEntry.COL_CONTENT +
                ", " + PrayerContract.PrayerEntry.COL_IS_ANSWERED +
                " FROM " + PrayerContract.PrayerEntry.TABLE +
                " WHERE " + PrayerContract.PrayerEntry._ID + " = " + id;

        return db.rawQuery(query, null);
    }

    public void update(int id, CharSequence title, CharSequence content) {

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "UPDATE " + PrayerContract.PrayerEntry.TABLE +
                " SET " + PrayerContract.PrayerEntry.COL_TITLE + "=\"" + title + "\", " +
                PrayerContract.PrayerEntry.COL_CONTENT + "=\"" + content + "\", " +
                PrayerContract.PrayerEntry.COL_IS_ANSWERED + "=\"" + title + "\" " +
                " WHERE " + PrayerContract.PrayerEntry._ID + "=" + id;
        Cursor cursor = db.rawQuery(query, null);
        cursor.close();
    }

    protected void delete() {}
}
