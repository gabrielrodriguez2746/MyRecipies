package com.myrecipes.data.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.myrecipes.data.dao.IngredientsDao;
import com.myrecipes.data.dao.RecipesDao;
import com.myrecipes.data.dao.StepsDao;
import com.myrecipes.data.models.Ingredient;
import com.myrecipes.data.models.Recipe;
import com.myrecipes.data.models.Step;

@Database(entities = {Recipe.class, Ingredient.class, Step.class}, version = 1)
public abstract class RecipesDatabase extends RoomDatabase {

    public abstract RecipesDao recipesDao();

    public abstract IngredientsDao ingredientsDao();

    public abstract StepsDao stepsDao();
}