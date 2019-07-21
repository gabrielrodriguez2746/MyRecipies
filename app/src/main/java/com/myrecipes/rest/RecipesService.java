package com.myrecipes.rest;

import com.myrecipes.data.models.RecipesWrapper;

import io.reactivex.Single;
import retrofit2.http.GET;

public interface RecipesService {

    @GET("topher/2017/May/59121517_baking/baking.json")
    Single<RecipesWrapper> getRecipes();
}
