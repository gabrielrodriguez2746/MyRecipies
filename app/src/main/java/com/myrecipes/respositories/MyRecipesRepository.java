package com.myrecipes.respositories;

import com.myrecipes.data.models.Recipe;
import com.myrecipes.data.models.RecipesWrapper;
import com.myrecipes.rest.RecipesService;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class MyRecipesRepository implements RecipesRepository {

    private RecipesService service;

    @Inject
    public MyRecipesRepository(RecipesService service) {
        this.service = service;
    }

    @Override
    public Single<List<Recipe>> getRecipes() {
        return service.getRecipes()
                .subscribeOn(Schedulers.io())
                .map(RecipesWrapper::getRecipes);
    }
}
