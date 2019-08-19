package com.myrecipes.respositories;

import com.myrecipes.data.models.Ingredient;
import com.myrecipes.data.models.Recipe;
import com.myrecipes.data.models.Step;

import java.util.List;

import io.reactivex.Single;

public interface RecipeDetailRepository {

    Single<Recipe> getRecipeById(int id);

    Single<List<Step>> getStepsByRecipeId(int id);

    Single<List<Ingredient>> getIngredientsByRecipeId(int id);
}
