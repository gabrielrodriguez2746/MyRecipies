package com.myrecipes.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.myrecipes.IngredientsIntentService;
import com.myrecipes.R;
import com.myrecipes.data.preference.LastRecipePreference;
import com.myrecipes.fragments.RecipeDetailFragment;
import com.myrecipes.fragments.StepVideoPlayerFragment;
import com.myrecipes.listeners.OnDetailFragmentInteraction;
import com.myrecipes.listeners.OnFragmentInteraction;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class MainActivity extends AppCompatActivity implements HasSupportFragmentInjector,
        OnFragmentInteraction, OnDetailFragmentInteraction {

    @Inject
    LastRecipePreference lastRecipePreference;

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    private NavController navController;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        DataBindingUtil.setContentView(this, R.layout.activity_main);
        navController = Navigation.findNavController(this, R.id.fgNavController);
        NavigationUI.setupActionBarWithNavController(this, navController);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp();
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }

    @Override
    public void onItemClicked(String fragment, String id) {
        int recipeId = Integer.valueOf(id);
        navigateToDetail(recipeId);
    }

    @Override
    public void onStepClicked(String recipeId, String stepId) {
        Bundle data = new Bundle();
        data.putInt(StepVideoPlayerFragment.RECIPE_ID_KEY, Integer.valueOf(recipeId));
        data.putInt(StepVideoPlayerFragment.STEP_ID_KEY, Integer.valueOf(stepId));
        navController.navigate(R.id.destination_player, data);
    }

    private void navigateToDetail(int recipeId) {
        Bundle data = new Bundle();
        data.putInt(RecipeDetailFragment.RECIPE_ID_KEY, recipeId);
        lastRecipePreference.setLastRecipeId(recipeId);
        IngredientsIntentService.startActionUpdateRecipes(this);
        navController.navigate(R.id.action_fragment_detail, data);
    }
}
