package com.myrecipes.viewmodels.detail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.myrecipes.data.models.Ingredient;
import com.myrecipes.data.models.Recipe;
import com.myrecipes.data.models.Step;
import com.myrecipes.respositories.RecipeDetailRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

public class RecipeDetailViewModel extends ViewModel {

    private MutableLiveData<Recipe> recipeLiveData;
    private MutableLiveData<List<Ingredient>> ingredientsLiveData;
    private MutableLiveData<List<Step>> stepsLiveData;
    private RecipeDetailRepository detailRepository;
    private CompositeDisposable compositeDisposable;

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }

    @Inject
    public RecipeDetailViewModel(RecipeDetailRepository detailRepository) {
        this.detailRepository = detailRepository;
        compositeDisposable = new CompositeDisposable();
        recipeLiveData = new MutableLiveData<>();
        ingredientsLiveData = new MutableLiveData<>();
        stepsLiveData = new MutableLiveData<>();
    }

    public void getRecipeInformationById(int id) {
        compositeDisposable.add(getAllRecipeDataZip(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        movie -> {
                        }, // Gabriel Improve this
                        Throwable::printStackTrace) // Gabriel Notify
        );
    }

    public LiveData<Recipe> getRecipeLiveData() {
        return recipeLiveData;
    }

    public LiveData<List<Ingredient>> getIngredientsLiveData() {
        return ingredientsLiveData;
    }

    public LiveData<List<Step>> getStepsLiveData() {
        return stepsLiveData;
    }

    private Single<List<Step>> getRecipeStepsSingle(int id) {
        return detailRepository.getStepsByRecipeId(id)
                .subscribeOn(AndroidSchedulers.mainThread())
                .doOnSuccess(steps -> stepsLiveData.postValue(steps));
    }

    private Single<List<Ingredient>> getRecipeIngredientsSingle(int id) {
        return detailRepository.getIngredientsByRecipeId(id)
                .subscribeOn(AndroidSchedulers.mainThread())
                .doOnSuccess(ingredients -> ingredientsLiveData.postValue(ingredients));
    }

    private Single<Recipe> getSingleRecipe(int id) {
        return detailRepository.getRecipeById(id)
                .subscribeOn(AndroidSchedulers.mainThread())
                .doOnSuccess(recipe -> {
                    recipeLiveData.postValue(recipe);
                });
    }

    private Single<Recipe> getAllRecipeDataZip(int movieId) {
        return Single.zip(
                getRecipeStepsSingle(movieId),
                getRecipeIngredientsSingle(movieId),
                getSingleRecipe(movieId), (steps, ingredients, recipe) -> {
                    recipe.setIngredients(ingredients);
                    recipe.setSteps(steps);
                    return recipe;
                });
    }
}
