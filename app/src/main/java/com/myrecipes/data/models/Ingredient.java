package com.myrecipes.data.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@Entity(tableName = "ingredient")
public class Ingredient {

    @ColumnInfo(name = "recipe_id")
    private int recipeId;

    private Double quantity;

    private String unit;

    @NotNull
    private String name;

    @PrimaryKey
    private int id;

    private int index;

    public Ingredient(int index, int recipeId, Double quantity, String unit, String name) {
        this.index = index;
        this.recipeId = recipeId;
        this.quantity = quantity;
        this.unit = unit;
        this.name = name;
        this.id = recipeId + name.hashCode();
    }

    public int getIndex() {
        return index;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ingredient)) return false;
        Ingredient that = (Ingredient) o;
        return recipeId == that.recipeId &&
                id == that.id &&
                quantity.equals(that.quantity) &&
                unit.equals(that.unit) &&
                name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recipeId, quantity, unit, name, id);
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "recipeId=" + recipeId +
                ", quantity=" + quantity +
                ", unit='" + unit + '\'' +
                ", name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}
