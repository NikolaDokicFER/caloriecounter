package hr.fer.caloriecounter.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import hr.fer.caloriecounter.model.Food;

public class CalorieCounterPlanDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "CalorieCounterPlan.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "plans";
    private static final String COLUMN_NAME_ID = "id";
    private static final String COLUMN_NAME_DAY = "day";
    private static final String COLUMN_NAME_TYPE = "type";
    private static final String COLUMN_NAME_FOOD = "food";

    public CalorieCounterPlanDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_NAME_DAY + " TEXT," +
                COLUMN_NAME_TYPE + " TEXT," +
                COLUMN_NAME_FOOD + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insert(String day, String food, String type) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_DAY, day);
        values.put(COLUMN_NAME_FOOD, food);
        values.put(COLUMN_NAME_TYPE, type);

        long result = db.insert(TABLE_NAME, null, values);
    }

    public Cursor fetchEntries(String day, String type) {
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE day = '" +  day + "' AND type = '" + type + "'";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }

        return cursor;
    }

    public void deleteEntry(String food, String day, String type) {
        SQLiteDatabase db = this.getWritableDatabase();

        long result = db.delete(TABLE_NAME, "food=? and day=? and type=?", new String[]{food, day, type});
    }
}
