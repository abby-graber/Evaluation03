package edu.charlotte.evaluation03.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import edu.charlotte.evaluation03.R;
import edu.charlotte.evaluation03.databinding.FragmentSearchBinding;
import edu.charlotte.evaluation03.models.Category;
import edu.charlotte.evaluation03.models.Meal;
import edu.charlotte.evaluation03.models.MealDetail;

public class SearchFragment extends Fragment {
    public SearchFragment() {
        // Required empty public constructor
    }

    FragmentSearchBinding binding;
    Category selectedCategory;
    ArrayList<Meal> meals = new ArrayList<>();

    public void setSelectedCategory(Category selectedCategory) {
        this.selectedCategory = selectedCategory;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Search");

        binding.buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedCategory = null;
                binding.textViewCategory.setText("No Category Selected");
                binding.editTextSearch.setText("");
                meals.clear();
                //notify the adapter.
            }
        });

        binding.buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //perform the search based on the entered information.
            }
        });

        binding.buttonSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.gotoSelectCategory();
            }
        });

        binding.buttonCookBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.gotoMyCookBook();
            }
        });

        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@org.jspecify.annotations.NonNull Menu menu, @org.jspecify.annotations.NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.search_menu, menu);
            }

            @Override
            public boolean onMenuItemSelected(@org.jspecify.annotations.NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.action_logout) {
                    mListener.logout();
                    return true;
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }

    SearchListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mListener = (SearchListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement SearchListener");
        }
    }

    public interface SearchListener {
        void gotoSelectCategory();
        void gotoMyCookBook();
        void gotoMealDetails(MealDetail mealDetail);
        void logout();
    }
}