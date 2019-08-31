package com.myrecipes.viewmodels.detail;

import com.myrecipes.respositories.RecipeDetailRepository;

import javax.inject.Inject;

public class RecipeDetailViewModel extends BaseRecipeDetailViewModel {

    @Inject
    public RecipeDetailViewModel(RecipeDetailRepository detailRepository) {
        super(detailRepository);
    }

}
