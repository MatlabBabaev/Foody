package com.test.fuddy;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class FoodDBHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "food.db";
    public static final int DB_VERSION = 2;

    public static final String TABLE_NAME = "food";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_MEAL_TIME = "mealTime";
    public static final String COLUMN_MEAL = "meal";
    public static final String COLUMN_CALORIE = "calorie";
    public static final String COLUMN_FAT = "fat";
    public static final String COLUMN_CARBOHYDRATE = "carbohydrate";
    public static final String COLUMN_PROTEIN = "protein";

    public static final String TYPE_TEXT="TEXT";
    private static final String TYPE_INTEGER="INTEGER";
    private static final String TYPE_DOUBLE="REAL";
    private static final String TYPE_DATE="DATE";
    private static final String TYPE_TIME="TIME";

    public static final String CREATE_COMMAND="CREATE TABLE IF NOT EXISTS "  + TABLE_NAME+
            " (" +COLUMN_ID + " " +TYPE_INTEGER + " " + "PRIMARY KEY AUTOINCREMENT, " + COLUMN_DATE+
            " " + TYPE_DATE + ", " + COLUMN_TIME + " " + TYPE_TIME + ", " +COLUMN_MEAL_TIME+
            " " + TYPE_TEXT + ", " + COLUMN_MEAL + " " + TYPE_TEXT + ", " + COLUMN_CALORIE +
            " " + TYPE_DOUBLE + ", "+ COLUMN_FAT + " "+ TYPE_DOUBLE + ", "+ COLUMN_CARBOHYDRATE +
            " "+ TYPE_DOUBLE + ", "+ COLUMN_PROTEIN + " "+ TYPE_DOUBLE + ")";
    public  static final String DROP_COMMAND = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public FoodDBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_COMMAND);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(DROP_COMMAND);
        onCreate(db);
    }
}
