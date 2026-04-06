
package com.example.water;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "watertracker.db";
    private static final int DB_VERSION = 1;
    private static final String TABLE = "water_intake";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE +
                " (id INTEGER PRIMARY KEY AUTOINCREMENT, amount INTEGER, date TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }

    public void addIntake(int amount, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("amount", amount);
        cv.put("date", date);
        db.insert(TABLE, null, cv);
        db.close();
    }

    public int getTodayTotal(String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT SUM(amount) FROM " + TABLE + " WHERE date=?",
                new String[]{date});
        int total = 0;
        if (cursor.moveToFirst()) total = cursor.getInt(0);
        cursor.close();
        return total;
    }

    public List<String> getAllHistory() {
        List<String> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT date, SUM(amount) FROM " + TABLE +
                        " GROUP BY date ORDER BY date DESC", null);
        while (cursor.moveToNext()) {
            list.add(cursor.getString(0) + " — " + cursor.getInt(1) + " ml");
        }
        cursor.close();
        return list;
    }
}