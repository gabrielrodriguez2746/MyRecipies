package com.myrecipes.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.myrecipes.R;
import com.myrecipes.data.models.Recipe;
import com.myrecipes.databinding.FragmentRecipesListBinding;
import com.myrecipes.viewmodels.main.RecipesListViewModel;

import java.util.List;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import timber.log.Timber;

public class RecipesListFragment extends Fragment {

    private FragmentRecipesListBinding binding;
    private RecipesListViewModel viewModel;

    @Inject
    ViewModelProvider.Factory factory;

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recipes_list, container, false);
        }
        return binding != null ? binding.getRoot() : super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (viewModel == null) {
            viewModel = ViewModelProviders.of(this, factory).get(RecipesListViewModel.class);
            getViewLifecycleOwner().getLifecycle().addObserver(viewModel);
        }
        viewModel.getItemsLiveData().observe(getViewLifecycleOwner(), this::processItems);
    }

    private void processItems(List<Recipe> recipes) {
        Timber.d("recipes at fragment :: %s", recipes.toString());
    }
}
