package com.myrecipes.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.myrecipes.data.models.Ingredient;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public abstract class IngredientsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Completable insert(List<Ingredient> ingredients);

    @Query("SELECT * FROM ingredient WHERE recipe_id = :recipeId ORDER BY `index` ASC")
    public abstract Single<List<Ingredient>> getIngredientsByRecipeId(int recipeId);

}