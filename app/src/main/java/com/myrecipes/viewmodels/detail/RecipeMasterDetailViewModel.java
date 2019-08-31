package com.myrecipes.viewmodels.detail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.myrecipes.data.models.Step;
import com.myrecipes.respositories.RecipeDetailRepository;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class RecipeMasterDetailViewModel extends BaseRecipeDetailViewModel {

    private MutableLiveData<Step> stepLiveData;

    @Inject
    public RecipeMasterDetailViewModel(RecipeDetailRepository detailRepository) {
        super(detailRepository);
        stepLiveData = new MutableLiveData<>();
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

}
