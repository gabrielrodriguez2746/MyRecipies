package com.myrecipes.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.myrecipes.R;
import com.myrecipes.adapters.RecipesListAdapter;
import com.myrecipes.data.models.Recipe;
import com.myrecipes.databinding.FragmentRecipesListBinding;
import com.myrecipes.decorators.VerticalSpaceItemDecoration;
import com.myrecipes.listeners.OnFragmentInteraction;
import com.myrecipes.models.RecyclerViewConfiguration;
import com.myrecipes.viewmodels.main.RecipesListViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import timber.log.Timber;

public class RecipesListFragment extends Fragment implements RecipesListAdapter.OnRecipeClicked {

    private FragmentRecipesListBinding binding;
    private RecipesListAdapter adapter;
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
            DisplayMetrics displayMetrics = getDisplayMetrics();
            adapter = new RecipesListAdapter(displayMetrics, this);
            int rowsNumber = Objects.requireNonNull(getContext()).getResources().getInteger(R.integer.app_adapter_rows);
            RecyclerView.LayoutManager layoutManager;
            if (rowsNumber > 1) {
                layoutManager = new LinearLayoutManager(getContext());
            } else {
                layoutManager = new GridLayoutManager(getContext(), rowsNumber);
            }
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recipes_list, container, false);
            binding.setRecyclerConfiguration(new RecyclerViewConfiguration(layoutManager, adapter,
                    new VerticalSpaceItemDecoration((getResources().getDimensionPixelSize(R.dimen.space_small)))));
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

    @NotNull
    private DisplayMetrics getDisplayMetrics() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        Objects.requireNonNull(getActivity()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics;
    }

    private void processItems(List<Recipe> recipes) {
        Timber.d("recipes at fragment :: %s", recipes.toString());
        adapter.submitList(recipes);
    }

    @Override
    public void onRecipeClicked(int recipeId) {
        ((OnFragmentInteraction) Objects.requireNonNull(getActivity()))
                .onItemClicked(viewModel.getClass().getSimpleName(), String.valueOf(recipeId));
    }
}
