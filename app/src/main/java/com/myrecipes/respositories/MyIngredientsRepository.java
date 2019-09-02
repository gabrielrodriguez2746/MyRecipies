package com.myrecipes.respositories;

import com.myrecipes.data.dao.IngredientsDao;
import com.myrecipes.data.dao.RecipesDao;
import com.myrecipes.data.models.Recipe;
import com.myrecipes.data.preference.LastRecipePreference;

import java.util.Collections;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class MyIngredientsRepository implements IngredientsRepository {

    private IngredientsDao ingredientsDao;
    private RecipesDao recipesDao;
    private LastRecipePreference preferenceManager;

    @Inject
    public MyIngredientsRepository(IngredientsDao ingredientsDao, RecipesDao recipesDao, LastRecipePreference preferenceManager) {
        this.ingredientsDao = ingredientsDao;
        this.recipesDao = recipesDao;
        this.preferenceManager = preferenceManager;
    }


    @Override
    public Single<Recipe> getLastRecipeIngredients() {
        Timber.d("Calling recipe ingredients");
        int lastSeenRecipeId = preferenceManager.getLastSeenRecipeId();
        if (lastSeenRecipeId != -1) {
            Timber.d("last recipe available :: %s", lastSeenRecipeId);
            return recipesDao.getRecipeById(lastSeenRecipeId)
                    .subscribeOn(Schedulers.io())
                    .flatMap((Function<Recipe, SingleSource<Recipe>>) recipe ->
                            ingredientsDao.getIngredientsByRecipeId(lastSeenRecipeId)
                                    .subscribeOn(Schedulers.io())
                                    .map(ingredients -> {
                                        Timber.d("I am here");
                                        recipe.setIngredients(ingredients);
                                        return recipe;
                                    }));

        } else {
            Timber.d("last recipe not available");
            return Single.just(new Recipe(-1, "", "", Collections.emptyList(), Collections.emptyList()));
        }
    }
}
