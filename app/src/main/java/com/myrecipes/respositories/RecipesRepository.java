package com.myrecipes.respositories;

import com.myrecipes.data.models.Recipe;

import java.util.List;

import io.reactivex.Single;

public interface RecipesRepository {

    Single<List<Recipe>> getRecipes();
}
