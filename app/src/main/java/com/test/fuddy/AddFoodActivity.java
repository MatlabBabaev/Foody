package com.test.fuddy;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kd.dynamic.calendar.generator.ImageGenerator;
import com.test.beans.RequestBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AddFoodActivity extends AppCompatActivity {

    private final String EDEMAM_URL = "https://api.edamam.com/api/nutrition-details?app_id=48f1157e&app_key=4fa70c35958cac026e3e5e45a6fd9355";
    private Spinner spinnerMealTime;
    private EditText editTextMealContent;
    private EditText textViewCalorie;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    private TextView textViewFat, buttonGetCalorie;
    private TextView textViewCarbohydrate;
    private TextView textViewProtein;
    private Button buttonSave;

    private FoodDBHelper dbHelper;
    private SQLiteDatabase database;

    EditText mDateEdittext;
    Calendar mCurrentDate;
    ImageView imageGen;

    Bitmap mGeneratedicon;
    ImageGenerator mImageGenerator;
    ImageView mDisplayGeneratedImage;

    double calorie = 0;
    double fat = 0;
    double carbohydrate = 0;
    double protein = 0;

    String title = "Fresh food";
    String prep = "1. some recipe";
    String yield = "About 15 servings";
    String[] ingr;

    private String action;
    private ArrayList<String> parcel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);

        Intent intent = getIntent();
        action = intent.getStringExtra("action");
        parcel = intent.getStringArrayListExtra("parcel");
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.hide();

        spinnerMealTime = findViewById(R.id.spinnerMealTime);
        editTextMealContent = findViewById(R.id.editTextMealContent);
        textViewCalorie = findViewById(R.id.textViewCalorie);
        textViewFat = findViewById(R.id.editTextFat);
        textViewCarbohydrate = findViewById(R.id.editTextCarbohydrate);
        textViewProtein = findViewById(R.id.editTextProtein);
        buttonSave = findViewById(R.id.buttonSaveFood);

        dbHelper = new FoodDBHelper(this);
        database = dbHelper.getWritableDatabase();
        mImageGenerator = new ImageGenerator(this);
        mDateEdittext = findViewById(R.id.editTextDate);
        mDisplayGeneratedImage = (ImageView) findViewById(R.id.imageGen);

        mImageGenerator.setIconSize(22, 22);
        mImageGenerator.setDateSize(10);
        mImageGenerator.setMonthSize(4);
        mImageGenerator.setDatePosition(18);
        mImageGenerator.setMonthPosition(7);
        mImageGenerator.setDateColor(Color.parseColor("#000000"));
        mImageGenerator.setMonthColor(Color.WHITE);
        mCurrentDate = Calendar.getInstance();
        mImageGenerator.setStorageToSDCard(true);
        imageGen = findViewById(R.id.imageGen);

        if (action.equals("edit")) {
            mDateEdittext.setText(parcel.get(1));
            spinnerMealTime.setSelection(0);
            editTextMealContent.setText(parcel.get(3));
            textViewCalorie.setText(parcel.get(4));
            textViewFat.setText(parcel.get(5));
            textViewCarbohydrate.setText(parcel.get(6));
            textViewProtein.setText(parcel.get(7));
            buttonSave.setText("UPDATE RECORD");

            try {
                mCurrentDate.setTime(sdf.parse(parcel.get(1)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            mGeneratedicon = mImageGenerator.generateDateImage(mCurrentDate, R.drawable.ccal);
        } else {
            mCurrentDate.setTime(new Date());
            mDateEdittext.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            mGeneratedicon = mImageGenerator.generateDateImage(mCurrentDate, R.drawable.ccal);
        }
        mDisplayGeneratedImage.setImageBitmap(mGeneratedicon);


        //Select date event listener
        imageGen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mCurrentDate = Calendar.getInstance();
                int year = mCurrentDate.get(Calendar.YEAR);
                int month = mCurrentDate.get(Calendar.MONTH);
                int day = mCurrentDate.get(Calendar.DAY_OF_MONTH);

                final DatePickerDialog mDatePicker = new DatePickerDialog(AddFoodActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay) {
                        int month = selectedMonth + 1;
                        if (month < 10 && selectedDay < 10)
                            mDateEdittext.setText(selectedYear + "-0" + month + "-0" + selectedDay);
                        else if (month < 10)
                            mDateEdittext.setText(selectedYear + "-0" + month + "-" + selectedDay);
                        else if (selectedDay < 10)
                            mDateEdittext.setText(selectedYear + "-" + month + "-0" + selectedDay);
                        else
                            mDateEdittext.setText(selectedYear + "-" + month + "-" + selectedDay);
                        mCurrentDate.set(selectedYear, selectedMonth, selectedDay);
                        mGeneratedicon = mImageGenerator.generateDateImage(mCurrentDate, R.drawable.ccal);
                        mDisplayGeneratedImage.setImageBitmap(mGeneratedicon);
                    }
                }, year, month, day);
                mDatePicker.show();

            }
        });
    }

    public void onClickSaveFood(View view) {
        String day = mDateEdittext.getText().toString();
        String mealTime = spinnerMealTime.getSelectedItem().toString();
        String mealContent = editTextMealContent.getText().toString();
        double calorie = Double.parseDouble(textViewCalorie.getText().toString());
        double fat = Double.parseDouble(textViewFat.getText().toString());
        double carbohydrate = Double.parseDouble(textViewCarbohydrate.getText().toString());
        double protein = Double.parseDouble(textViewProtein.getText().toString());

        if (action.equals("add")) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(FoodDBHelper.COLUMN_DATE, day);
            contentValues.put(FoodDBHelper.COLUMN_MEAL_TIME, mealTime);
            contentValues.put(FoodDBHelper.COLUMN_MEAL, mealContent);
            contentValues.put(FoodDBHelper.COLUMN_CALORIE, calorie);
            contentValues.put(FoodDBHelper.COLUMN_FAT, fat);
            contentValues.put(FoodDBHelper.COLUMN_CARBOHYDRATE, carbohydrate);
            contentValues.put(FoodDBHelper.COLUMN_PROTEIN, protein);

            database.insert(FoodDBHelper.TABLE_NAME, null, contentValues);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            Toast.makeText(this, "Record added!!!", Toast.LENGTH_SHORT).show();
        } else if (action.equals("edit")) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(FoodDBHelper.COLUMN_DATE, day);
            contentValues.put(FoodDBHelper.COLUMN_MEAL_TIME, mealTime);
            contentValues.put(FoodDBHelper.COLUMN_MEAL, mealContent);
            contentValues.put(FoodDBHelper.COLUMN_CALORIE, calorie);
            contentValues.put(FoodDBHelper.COLUMN_FAT, fat);
            contentValues.put(FoodDBHelper.COLUMN_CARBOHYDRATE, carbohydrate);
            contentValues.put(FoodDBHelper.COLUMN_PROTEIN, protein);

            String[] args = new String[]{parcel.get(0)};
            ;
            database.update(FoodDBHelper.TABLE_NAME, contentValues, FoodDBHelper.COLUMN_ID + " = ?", args);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            Toast.makeText(this, "Record updated!!!", Toast.LENGTH_SHORT).show();

        }
    }

    public void buttonGetCalorieClick(View view) {
        ingr = editTextMealContent.getText().toString().trim().split(";");
        JSONTask task = new JSONTask();
        String url = String.format(EDEMAM_URL);
        task.execute(url);
    }

    public void onClickCancel(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }

    public class JSONTask extends AsyncTask<String, Void, String> {

        final ProgressDialog dialog = new ProgressDialog(AddFoodActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Calculating...");
            dialog.show();
        }



        @Override
        protected void onPostExecute(String s) {


            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                calorie = jsonObject.getDouble("calories");
                fat = jsonObject.getJSONObject("totalNutrientsKCal").getJSONObject("FAT_KCAL").getDouble("quantity");
                carbohydrate = jsonObject.getJSONObject("totalNutrientsKCal").getJSONObject("CHOCDF_KCAL").getDouble("quantity");
                protein = jsonObject.getJSONObject("totalNutrientsKCal").getJSONObject("PROCNT_KCAL").getDouble("quantity");

                textViewCalorie.setText(Double.toString(calorie));
                textViewFat.setText(Double.toString(fat));
                textViewCarbohydrate.setText(Double.toString(carbohydrate));
                textViewProtein.setText(Double.toString(protein));

                Toast.makeText(AddFoodActivity.this, "Updated", Toast.LENGTH_SHORT).show();

            } catch (JSONException e) {
                e.printStackTrace();
                return;
            } catch (Exception e) {
                return;
            }
            dialog.dismiss();

        }

        @Override
        protected String doInBackground(String... strings) {
            title = "Fresh food";
            prep = "1. some recipe";
            yield = "About 15 servings";

            HttpURLConnection conn = null;
            try {

                RequestBean requestBean = null;
                String url_ = EDEMAM_URL;
                requestBean = new RequestBean(url_, title, prep, yield, ingr);
                URL url = new URL(requestBean.getUrl());
                conn = (HttpURLConnection) url.openConnection();

                conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                conn.setRequestMethod("POST");

                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.connect();
                String temp = new Gson().toJson(requestBean);
                conn.getOutputStream().write(temp.getBytes());


                InputStreamReader iStream = new InputStreamReader(conn.getInputStream());
                BufferedReader inFor = new BufferedReader(iStream);

                StringBuilder result = new StringBuilder();
                String line = inFor.readLine();
                while (line != null) {
                    result.append(line);
                    line = inFor.readLine();
                }
                inFor.close();
                Log.i("myREsult", result.toString());
                return result.toString();


            } catch (MalformedURLException ex) {
                ex.printStackTrace();
                return null;

            } catch (IOException ex) {
                ex.printStackTrace();
                return null;

            } catch (Exception e) {
                return null;
            } finally {
                if (conn != null) {
                    conn.disconnect();

                }
            }
        }
    }
}
