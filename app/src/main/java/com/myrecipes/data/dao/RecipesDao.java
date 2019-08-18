package com.myrecipes.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.myrecipes.data.models.Recipe;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

@Dao
public abstract class RecipesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Completable insert(List<Recipe> recipes);

    @Query("SELECT * FROM recipe WHERE id = :recipeId")
    public abstract Maybe<Recipe> getRecipeById(int recipeId);

    @Query("SELECT * FROM recipe")
    public abstract Single<List<Recipe>> getRecipes();

}