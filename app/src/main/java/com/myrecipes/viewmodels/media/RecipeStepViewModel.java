package com.myrecipes.viewmodels.media;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.myrecipes.data.models.Step;
import com.myrecipes.respositories.RecipeDetailRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

public class RecipeStepViewModel extends ViewModel {

    private MutableLiveData<Step> stepLiveData;
    private MutableLiveData<List<Step>> stepsLiveData;
    private RecipeDetailRepository detailRepository;
    private CompositeDisposable compositeDisposable;

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }

    @Inject
    public RecipeStepViewModel(RecipeDetailRepository detailRepository) {
        this.detailRepository = detailRepository;
        compositeDisposable = new CompositeDisposable();
        stepLiveData = new MutableLiveData<>();
        stepsLiveData = new MutableLiveData<>();
    }

    public void getStepsByRecipeId(int id) {
        compositeDisposable.add(detailRepository.getStepsByRecipeId(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        steps -> stepsLiveData.postValue(steps), // Gabriel Improve this
                        Throwable::printStackTrace) // Gabriel Notify
        );
    }

    public void getRecipeStepInformation(int recipeId, int stepId) {
        compositeDisposable.add(detailRepository.getStepByRecipeId(recipeId, stepId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        step -> stepLiveData.postValue(step), // Gabriel Improve this
                        Throwable::printStackTrace) // Gabriel Notify
        );
    }

    public LiveData<Step> getStepLiveData() {
        return stepLiveData;
    }

    public LiveData<List<Step>> getStepsLiveData() {
        return stepsLiveData;
    }
}
