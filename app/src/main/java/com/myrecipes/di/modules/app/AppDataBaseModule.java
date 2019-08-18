package com.myrecipes.di.modules.app;

import android.content.Context;

import androidx.room.Room;

import com.myrecipes.data.dao.IngredientsDao;
import com.myrecipes.data.dao.RecipesDao;
import com.myrecipes.data.dao.StepsDao;
import com.myrecipes.data.database.RecipesDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppDataBaseModule {

    @Provides
    @Singleton
    public RecipesDatabase provideRecipesDataBase(Context context) {
        return Room.databaseBuilder(context, RecipesDatabase.class, "Recipes.db")
                .fallbackToDestructiveMigration().build();
    }

    // Gabriel Improve also this, should not be singleton
    @Provides
    @Singleton
    public RecipesDao provideRecipesDao(RecipesDatabase database) {
        return database.recipesDao();
    }

    // Gabriel Improve also this, should not be singleton
    @Provides
    @Singleton
    public IngredientsDao provideIngredientsDao(RecipesDatabase database) {
        return database.ingredientsDao();
    }

    // Gabriel Improve also this, should not be singleton
    @Provides
    @Singleton
    public StepsDao provideStepsDao(RecipesDatabase database) {
        return database.stepsDao();
    }

}