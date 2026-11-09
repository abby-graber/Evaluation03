package edu.charlotte.evaluation03.models;

import org.json.JSONObject;

public class Meal {
    public String idMeal, strMeal, strMealThumb, strArea;

    public Meal() {}

    public Meal(JSONObject json) {
        idMeal = json.optString("idMeal");
        strMeal = json.optString("strMeal");
        strMealThumb = json.optString("strMealThumb");
        // search.php has strArea, filter.php does not
        strArea = json.isNull("strArea") ? "" : json.optString("strArea");
    }
}