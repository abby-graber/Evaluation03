package edu.charlotte.evaluation03.models;

import java.io.Serializable;

public class Ingredient implements Serializable {
    public String name, measure;

    public Ingredient() {}

    public Ingredient(String name, String measure) {
        this.name = name;
        this.measure = measure;
    }
}