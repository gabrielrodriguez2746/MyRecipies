package com.myrecipes.data.models;

import java.util.List;

public class RecipesWrapper {

    private List<Recipe> recipes;

    public RecipesWrapper(List<Recipe> recipes) {
        this.recipes = recipes;
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }
}
