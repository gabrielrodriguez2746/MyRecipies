package com.myrecipes.respositories;

import com.myrecipes.data.dao.IngredientsDao;
import com.myrecipes.data.dao.RecipesDao;
import com.myrecipes.data.dao.StepsDao;
import com.myrecipes.data.models.Recipe;
import com.myrecipes.data.models.RecipesWrapper;
import com.myrecipes.rest.RecipesService;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class MyRecipesRepository implements RecipesRepository {

    private RecipesService service;
    private RecipesDao recipesDao;
    private IngredientsDao ingredientsDao;
    private StepsDao stepsDao;

    @Inject
    public MyRecipesRepository(RecipesService service, RecipesDao recipesDao,
                               IngredientsDao ingredientsDao, StepsDao stepsDao) {
        this.service = service;
        this.recipesDao = recipesDao;
        this.ingredientsDao = ingredientsDao;
        this.stepsDao = stepsDao;
    }

    @Override
    public Single<List<Recipe>> getRecipes() {
        return service.getRecipes()
                .subscribeOn(Schedulers.io())
                .map(RecipesWrapper::getRecipes)
                .doOnSuccess(recipes -> {
                            recipesDao.insert(recipes);
                            for (Recipe recipe : recipes) {
                                ingredientsDao.insert(recipe.getIngredients());
                                stepsDao.insert(recipe.getSteps());
                            }
                        }

                );
    }
}
