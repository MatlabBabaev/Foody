package com.test.fuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SendReportActivity extends AppCompatActivity {

    private static final ArrayList<Food> foods = new ArrayList<>();
    private SQLiteDatabase database;
    private FoodDBHelper dbHelper;
    public SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_report);
        Intent intent = getIntent();
        String select = intent.getStringExtra("SelectedDate");
        dbHelper = new FoodDBHelper(this);
        database = dbHelper.getWritableDatabase();
        getData(select);
        export(null);
    }

     public void export(View view) {

        StringBuilder data = new StringBuilder();
        data.append("Date, Meal time, Meal content, Total calorie, Fat, Carbohydrate, Protein");
        for (int i = 0; i < foods.size(); i++) {
            data.append(String.format("\n%tD,%s,%s,%f,%f,%f,%f",
                    foods.get(i).getDate(),
                    foods.get(i).getMealTime(),
                    foods.get(i).getMeal().replaceAll(","," ").replaceAll("[\n\r]", ""),
                    foods.get(i).getCalorie(),
                    foods.get(i).getFat(),
                    foods.get(i).getCarbohydrate(),
                    foods.get(i).getProtein()));//    "\n" + String.valueOf(i) + "," + String.valueOf(i * i));
        }
        try {

            FileOutputStream out = openFileOutput("data.csv", Context.MODE_PRIVATE);
            out.write((data.toString()).getBytes());
            out.close();

            Context context = getApplicationContext();
            File filelLocation = new File(getFilesDir(), "data.csv");
            Uri path = FileProvider.getUriForFile(context, "com.test.fileprovider", filelLocation);
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/csv");
            intent.putExtra(Intent.EXTRA_SUBJECT, "Data");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.putExtra(Intent.EXTRA_STREAM, path);
            startActivity(Intent.createChooser(intent, "send mail"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getData(String select) {

        foods.clear();
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

    public void onClickBack(View view) {
        startActivity(new Intent(this, ChartActivity.class));
    }
}
