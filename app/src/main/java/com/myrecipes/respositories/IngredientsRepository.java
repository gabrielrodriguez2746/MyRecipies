package com.myrecipes.respositories;

import com.myrecipes.data.models.Recipe;

import io.reactivex.Single;

public interface IngredientsRepository {

    Single<Recipe> getLastRecipeIngredients();
}
