package com.myrecipes.respositories;

import com.myrecipes.data.dao.IngredientsDao;
import com.myrecipes.data.dao.RecipesDao;
import com.myrecipes.data.dao.StepsDao;
import com.myrecipes.data.models.Ingredient;
import com.myrecipes.data.models.Recipe;
import com.myrecipes.data.models.RecipesWrapper;
import com.myrecipes.data.models.Step;
import com.myrecipes.rest.RecipesService;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
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
                .flatMap(recipes -> saveRecipes(recipes).andThen(Single.just(recipes)));

    }

    private Completable saveRecipes(List<Recipe> recipes) {
        return recipesDao.insert(recipes).subscribeOn(Schedulers.io()).andThen(
                Single.fromCallable(() -> {
                    ArrayList<Ingredient> ingredients = new ArrayList<>();
                    ArrayList<Step> steps = new ArrayList<>();
                    for (Recipe recipe : recipes) {
                        ingredients.addAll(recipe.getIngredients());
                        steps.addAll(recipe.getSteps());
                    }
                    return new IngredientsAndSteps(ingredients, steps);
                }).flatMapCompletable(ingredientsAndSteps -> ingredientsDao.insert(ingredientsAndSteps.ingredients)
                        .subscribeOn(Schedulers.io())
                        .andThen(stepsDao.insert(ingredientsAndSteps.steps)))
        );
    }

    private class IngredientsAndSteps {

        private IngredientsAndSteps(List<Ingredient> ingredients, List<Step> steps) {
            this.ingredients = ingredients;
            this.steps = steps;
        }

        private List<Ingredient> ingredients;
        private List<Step> steps;
    }
}
