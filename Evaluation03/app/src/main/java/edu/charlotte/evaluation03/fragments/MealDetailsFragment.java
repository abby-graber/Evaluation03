package edu.charlotte.evaluation03.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.charlotte.evaluation03.R;
import edu.charlotte.evaluation03.databinding.FragmentMealDetailsBinding;
import edu.charlotte.evaluation03.models.MealDetail;

public class MealDetailsFragment extends Fragment {
    private static final String ARG_PARAM_MEAL_DETAIL = "ARG_PARAM_MEAL_DETAIL";
    private static final String ARG_PARAM2 = "param2";

    private MealDetail mMealDetail;
    public MealDetailsFragment() {
        // Required empty public constructor
    }

    public static MealDetailsFragment newInstance(MealDetail mealDetail) {
        MealDetailsFragment fragment = new MealDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM_MEAL_DETAIL, mealDetail);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mMealDetail = (MealDetail) getArguments().getSerializable(ARG_PARAM_MEAL_DETAIL);
        }
    }

    FragmentMealDetailsBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMealDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Meal Details");
    }
}