package com.myrecipes.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.myrecipes.data.models.Step;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public abstract class StepsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Completable insert(List<Step> steps);

    @Query("SELECT * FROM step WHERE recipe_id = :recipeId")
    public abstract Single<List<Step>> getStepsByRecipeId(int recipeId);

    @Query("SELECT * FROM step WHERE recipe_id = :recipeId AND id = :stepId")
    public abstract Single<Step> getStepByRecipeId(int recipeId, int stepId);

}