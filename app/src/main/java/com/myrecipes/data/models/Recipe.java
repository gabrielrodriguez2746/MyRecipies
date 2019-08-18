package com.myrecipes.data.models;

import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.List;
import java.util.Objects;

@Entity(tableName = "recipe")
public class Recipe {

    @PrimaryKey
    private int id;

    private String name;

    @Nullable
    private String image;

    @Ignore
    private List<Ingredient> ingredients;

    @Ignore
    private List<Step> steps;

    public Recipe(int id, String name, @Nullable String image) {
        this.id = id;
        this.name = name;
        this.image = image;
    }

    public Recipe(int id, String name, @Nullable String image, List<Ingredient> ingredients, List<Step> steps) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.ingredients = ingredients;
        this.steps = steps;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Nullable
    public String getImage() {
        return image;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public List<Step> getSteps() {
        return steps;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", ingredients=" + ingredients +
                ", steps=" + steps +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recipe recipe = (Recipe) o;
        return id == recipe.id &&
                Objects.equals(name, recipe.name) &&
                Objects.equals(image, recipe.image) &&
                Objects.equals(ingredients, recipe.ingredients) &&
                Objects.equals(steps, recipe.steps);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, image, ingredients, steps);
    }
}
