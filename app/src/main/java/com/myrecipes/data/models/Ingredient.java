package com.myrecipes.data.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = "ingredient")
public class Ingredient {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "recipe_id")
    private int recipeId;

    private Double quantity;

    private String unit;

    private String name;

    public Ingredient(int recipeId, Double quantity, String unit, String name) {
        this.recipeId = recipeId;
        this.quantity = quantity;
        this.unit = unit;
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public Double getQuantity() {
        return quantity;
    }

    public String getUnit() {
        return unit;
    }

    public String getName() {
        return name;
    }

    public int getRecipeId() {
        return recipeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ingredient that = (Ingredient) o;
        return Objects.equals(quantity, that.quantity) &&
                Objects.equals(unit, that.unit) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(quantity, unit, name);
    }
}
