package edu.charlotte.evaluation03.models;

import org.json.JSONObject;
import java.io.Serializable;
import java.util.ArrayList;

public class MealDetail implements Serializable {
    public String idMeal, strMeal, strCategory, strArea, strMealThumb, strInstructions;
    public ArrayList<Ingredient> ingredients = new ArrayList<>();

    public MealDetail() {}

    private static String get(JSONObject json, String key) {
        // The API returns null (not "") for unused fields
        return json.isNull(key) ? "" : json.optString(key).trim();
    }

    public MealDetail(JSONObject json) {
        idMeal = get(json, "idMeal");
        strMeal = get(json, "strMeal");
        strCategory = get(json, "strCategory");
        strArea = get(json, "strArea");
        strMealThumb = get(json, "strMealThumb");
        strInstructions = get(json, "strInstructions");

        // pair strIngredient1-20 with strMeasure1-20, skip empty ingredients
        for (int i = 1; i <= 20; i++) {
            String name = get(json, "strIngredient" + i);
            String measure = get(json, "strMeasure" + i);
            if (!name.isEmpty()) {
                ingredients.add(new Ingredient(name, measure));
            }
        }
    }
}