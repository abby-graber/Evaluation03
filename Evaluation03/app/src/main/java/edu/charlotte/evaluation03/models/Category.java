package edu.charlotte.evaluation03.models;

import org.json.JSONObject;
import java.io.Serializable;

public class Category implements Serializable {
    public String idCategory, strCategory, strCategoryThumb, strCategoryDescription;

    public Category() {}

    public Category(JSONObject json) {
        idCategory = json.optString("idCategory");
        strCategory = json.optString("strCategory");
        strCategoryThumb = json.optString("strCategoryThumb");
        strCategoryDescription = json.optString("strCategoryDescription");
    }
}