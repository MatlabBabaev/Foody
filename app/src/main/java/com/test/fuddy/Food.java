package com.test.fuddy;


import java.util.Date;

public class Food {

    private int id;
    private Date date;
    private String mealTime;
    private String meal;
    private double fat;
    private double carbohydrate;
    private double protein;
    private double calorie;

    public Food(int id, Date date, String mealTime,  String meal, double calorie, double fat, double carbohydrate, double protein) {
        this.id = id;
        this.calorie = calorie;
        this.meal = meal;
        this.date=date;
        this.mealTime = mealTime;
        this.fat=fat;
        this.carbohydrate=carbohydrate;
        this.protein=protein;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getCalorie() {
        return calorie;
    }

    public void setCalorie(double calorie) {
        this.calorie = calorie;
    }

    public String getMeal() {
        return meal;
    }

    public void setMeal(String meal) {
        this.meal = meal;
    }

    public String getMealTime() {
        return mealTime;
    }

    public void setMealTime(String mealTime) {
        this.mealTime = mealTime;
    }

    public static String getLunchTimeAsString(int position) {
        switch (position) {
            case 1:
                return "Breakfast";
            case 2:
                return "Lunch";
            default:
                return "Supper";
        }
    }

    public double getFat() {
        return fat;
    }

    public double getCarbohydrate() {
        return carbohydrate;
    }

    public double getProtein() {
        return protein;
    }
}
