package hr.fer.caloriecounter.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import hr.fer.caloriecounter.model.Food;

public class CalorieCounterDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "CalorieCounter.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "food";
    private static final String COLUMN_NAME_ID = "id";
    private static final String COLUMN_NAME_NAME = "name";
    private static final String COLUMN_NAME_CALORIES = "calories";
    private static final String COLUMN_NAME_FAT = "fat";
    private static final String COLUMN_NAME_PROTEIN = "protein";
    private static final String COLUMN_NAME_CARBS = "carbs";
    private static final String COLUMN_NAME_VITAMINA = "vitamin_a";
    private static final String COLUMN_NAME_VITAMINC = "vitamin_c";
    private static final String COLUMN_NAME_IODINE = "idoine";
    private static final String COLUMN_NAME_SALT = "salt";
    private static final String COLUMN_NAME_CALCIUM = "calcium";
    private static final String COLUMN_NAME_IRON = "iron";

    public CalorieCounterDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_NAME_ID + " LONG PRIMARY KEY," +
                COLUMN_NAME_NAME + " TEXT," +
                COLUMN_NAME_CALORIES + " LONG," +
                COLUMN_NAME_FAT + " FLOAT," +
                COLUMN_NAME_PROTEIN + " FLOAT," +
                COLUMN_NAME_CARBS + " FLOAT," +
                COLUMN_NAME_VITAMINA + " FLOAT," +
                COLUMN_NAME_VITAMINC + " FLOAT," +
                COLUMN_NAME_IODINE + " FLOAT," +
                COLUMN_NAME_SALT + " FLOAT," +
                COLUMN_NAME_CALCIUM + " FLOAT," +
                COLUMN_NAME_IRON + " FLOAT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insert(Food food) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_ID, food.getId());
        values.put(COLUMN_NAME_NAME, food.getName());
        values.put(COLUMN_NAME_CALORIES, food.getCalories());
        values.put(COLUMN_NAME_FAT, food.getFat());
        values.put(COLUMN_NAME_PROTEIN, food.getProteins());
        values.put(COLUMN_NAME_CARBS, food.getCarbohydrates());
        values.put(COLUMN_NAME_VITAMINA, food.getVitaminA());
        values.put(COLUMN_NAME_VITAMINC, food.getVitaminC());
        values.put(COLUMN_NAME_IODINE, food.getCholesterol());
        values.put(COLUMN_NAME_SALT, food.getSalt());
        values.put(COLUMN_NAME_CALCIUM, food.getCalcium());
        values.put(COLUMN_NAME_IRON, food.getIron());

        long result = db.insert(TABLE_NAME, null, values);
    }

    public Cursor fetchEntries() {
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }

        return cursor;
    }

    public Cursor fetchEntry(String name) {
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_NAME_NAME + " = '" + name + "'";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }

        return cursor;
    }

    public void deleteEntry(String entryId) {
        SQLiteDatabase db = this.getWritableDatabase();

        long result = db.delete(TABLE_NAME, "id=?", new String[]{entryId});
    }
}
