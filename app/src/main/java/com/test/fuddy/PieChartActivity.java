package com.test.fuddy;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PieChartActivity extends AppCompatActivity {

    private static final ArrayList<Food> foods = new ArrayList<>();
    private SQLiteDatabase database;
    private FoodDBHelper dbHelper;
    public SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_chart);
        Intent intent = getIntent();
        String select = intent.getStringExtra("SelectedDate");

        float calorie=0;
        float fat=0;
        float carbohydrate=0;
        float protein = 0;
        dbHelper = new FoodDBHelper(this);
        database = dbHelper.getWritableDatabase();
        getData(select);
        for (int i = 0; i < foods.size(); i++){

            calorie += foods.get(i).getCalorie();
            fat += foods.get(i).getFat();
            carbohydrate += foods.get(i).getCarbohydrate();
            protein += foods.get(i).getProtein();

        }


            PieChart pieChart = findViewById(R.id.piechart);
        ArrayList NoOfEmp = new ArrayList();

        NoOfEmp.add(new PieEntry(protein, "Protein"));
        NoOfEmp.add(new PieEntry(fat, "Fat"));
        NoOfEmp.add(new PieEntry(carbohydrate, "Carbohydrate"));
        NoOfEmp.add(new PieEntry(calorie, "Total calorie"));
        PieDataSet dataSet = new PieDataSet(NoOfEmp, "Consumed calories");
        dataSet.setValueTextSize(15);

        PieData data = new PieData(dataSet);
        pieChart.setData(data);
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieChart.animateXY(2000, 2000);
    }

    private void getData(String select) {

        foods.clear();
        //ArrayList<Food> foodFromDb = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM " + FoodDBHelper.TABLE_NAME + " WHERE " +
                FoodDBHelper.COLUMN_DATE + select, null);
        while (cursor.moveToNext()) {
            Date date = null;
            try {
                date = sdf.parse(cursor.getString(cursor.getColumnIndex(FoodDBHelper.COLUMN_DATE)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            int id = cursor.getInt(cursor.getColumnIndex(FoodDBHelper.COLUMN_ID));
            String mealTime = cursor.getString(cursor.getColumnIndex(FoodDBHelper.COLUMN_MEAL_TIME));
            String meal = cursor.getString(cursor.getColumnIndex(FoodDBHelper.COLUMN_MEAL));
            Double calorie = cursor.getDouble(cursor.getColumnIndex(FoodDBHelper.COLUMN_CALORIE));
            Double fat = cursor.getDouble(cursor.getColumnIndex(FoodDBHelper.COLUMN_FAT));
            Double carbohydrate = cursor.getDouble(cursor.getColumnIndex(FoodDBHelper.COLUMN_CARBOHYDRATE));
            Double protein = cursor.getDouble(cursor.getColumnIndex(FoodDBHelper.COLUMN_PROTEIN));

            Food food = new Food(id, date, mealTime, meal, calorie, fat, carbohydrate, protein);
            foods.add(food);
        }
        cursor.close();
    }
}
