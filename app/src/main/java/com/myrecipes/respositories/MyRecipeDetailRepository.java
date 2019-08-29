package com.myrecipes.respositories;

import com.myrecipes.data.dao.IngredientsDao;
import com.myrecipes.data.dao.RecipesDao;
import com.myrecipes.data.dao.StepsDao;
import com.myrecipes.data.models.Ingredient;
import com.myrecipes.data.models.Recipe;
import com.myrecipes.data.models.Step;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class MyRecipeDetailRepository implements RecipeDetailRepository {

    private RecipesDao recipesDao;
    private IngredientsDao ingredientsDao;
    private StepsDao stepsDao;

    @Inject
    public MyRecipeDetailRepository(RecipesDao recipesDao, IngredientsDao ingredientsDao, StepsDao stepsDao) {
        this.recipesDao = recipesDao;
        this.ingredientsDao = ingredientsDao;
        this.stepsDao = stepsDao;
    }

    @Override
    public Single<Recipe> getRecipeById(int id) {
        return recipesDao.getRecipeById(id)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Single<List<Step>> getStepsByRecipeId(int id) {
        return stepsDao.getStepsByRecipeId(id)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Single<List<Ingredient>> getIngredientsByRecipeId(int id) {
        return ingredientsDao.getIngredientsByRecipeId(id)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Single<Step> getStepByRecipeId(int recipeId, int stepId) {
        return stepsDao.getStepByRecipeId(recipeId, stepId)
                .subscribeOn(Schedulers.io());
    }
}
