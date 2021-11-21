package com.test.fuddy;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LineChartActivity extends AppCompatActivity {

    private static final ArrayList<Food> foods = new ArrayList<>();
    private SQLiteDatabase database;
    private FoodDBHelper dbHelper;
    public SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_chart);
        Intent intent = getIntent();
        String select = intent.getStringExtra("SelectedDate");

        dbHelper = new FoodDBHelper(this);
        database = dbHelper.getWritableDatabase();
        getData(select);

        LineChart ln = findViewById(R.id.barchart);

        ArrayList<Entry> calories = new ArrayList();
        ArrayList<Entry> fats = new ArrayList();
        ArrayList<Entry> carbs = new ArrayList();
        ArrayList<Entry> prots = new ArrayList();

        for (int i = 0; i < foods.size(); i++) {
            calories.add(new Entry(i + 1, new Float(foods.get(i).getCalorie())));
            fats.add(new Entry(i + 1, new Float(foods.get(i).getFat())));
            carbs.add(new Entry(i + 1, new Float(foods.get(i).getCarbohydrate())));
            prots.add(new Entry(i + 1, new Float(foods.get(i).getProtein())));
        }

        LineDataSet dataSetCalor = new LineDataSet(calories, "Calorie");
        LineDataSet dataSetFat = new LineDataSet(fats, "Fat");
        LineDataSet dataSetCarb = new LineDataSet(carbs, "Carbohydrate");
        LineDataSet dataSetProt = new LineDataSet(prots, "Protein");

        LineData data = new LineData(dataSetCalor, dataSetFat, dataSetCarb, dataSetProt);
        ln.setData(data);

        dataSetCalor.setValueTextSize(12);
        dataSetFat.setValueTextSize(12);
        dataSetCarb.setValueTextSize(12);
        dataSetProt.setValueTextSize(12);

        dataSetCalor.setLineWidth(3);
        dataSetCarb.setLineWidth(3);
        dataSetFat.setLineWidth(3);
        dataSetProt.setLineWidth(3);
        dataSetCalor.setColors(Color.BLACK);
        dataSetFat.setColors(Color.BLUE);
        dataSetCarb.setColors(Color.RED);
        dataSetProt.setColors(Color.GREEN);
        ln.animateXY(5000, 5000);
    }

    private void getData(String select) {

        foods.clear();
        //ArrayList<Food> foodFromDb = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT " + FoodDBHelper.COLUMN_DATE +
                ", SUM (" + FoodDBHelper.COLUMN_PROTEIN +
                "), SUM (" + FoodDBHelper.COLUMN_CARBOHYDRATE +
                "), SUM (" + FoodDBHelper.COLUMN_FAT +
                "), SUM (" + FoodDBHelper.COLUMN_CALORIE +
                ") FROM " + FoodDBHelper.TABLE_NAME +
                " WHERE " + FoodDBHelper.COLUMN_DATE + select +
                " GROUP BY " + FoodDBHelper.COLUMN_DATE, null);
        /*Cursor cursor = database.rawQuery("SELECT * FROM " + FoodDBHelper.TABLE_NAME + " WHERE " +
                FoodDBHelper.COLUMN_DATE + select, null);*/
        while (cursor.moveToNext()) {
            Date date = null;
            try {
                date = sdf.parse(cursor.getString(cursor.getColumnIndex(FoodDBHelper.COLUMN_DATE)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            int id = 0;//cursor.getInt(cursor.getColumnIndex(FoodDBHelper.COLUMN_ID));
            String mealTime = "yy";//cursor.getString(cursor.getColumnIndex(FoodDBHelper.COLUMN_MEAL_TIME));
            String meal = "yy";//cursor.getString(cursor.getColumnIndex(FoodDBHelper.COLUMN_MEAL));
            Double calorie = cursor.getDouble(cursor.getColumnIndex("SUM (" + FoodDBHelper.COLUMN_CALORIE + ")"));
            Double fat = cursor.getDouble(cursor.getColumnIndex("SUM (" + FoodDBHelper.COLUMN_FAT + ")"));
            Double carbohydrate = cursor.getDouble(cursor.getColumnIndex("SUM (" + FoodDBHelper.COLUMN_CARBOHYDRATE + ")"));
            Double protein = cursor.getDouble(cursor.getColumnIndex("SUM (" + FoodDBHelper.COLUMN_PROTEIN + ")"));

            Food food = new Food(id, date, mealTime, meal, calorie, fat, carbohydrate, protein);
            foods.add(food);
        }
        cursor.close();
    }
}
