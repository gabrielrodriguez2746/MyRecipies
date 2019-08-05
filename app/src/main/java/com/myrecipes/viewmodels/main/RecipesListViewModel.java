package com.myrecipes.viewmodels.main;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ViewModel;

import com.myrecipes.data.models.Recipe;
import com.myrecipes.respositories.RecipesRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

public class RecipesListViewModel extends ViewModel implements LifecycleObserver {

    private CompositeDisposable compositeDisposable;
    private MutableLiveData<List<Recipe>> itemsLiveData;

    private RecipesRepository repository;

    @Inject
    public RecipesListViewModel(RecipesRepository repository) {
        this.repository = repository;
        compositeDisposable = new CompositeDisposable();
        itemsLiveData = new MutableLiveData<>();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    void onCreate() {
        compositeDisposable.add(
                repository.getRecipes()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                recipes -> itemsLiveData.postValue(recipes), // Gabriel Improve this
                                Throwable::printStackTrace) // Gabriel Notify
        );
    }

    public LiveData<List<Recipe>> getItemsLiveData() {
        return itemsLiveData;
    }
}
