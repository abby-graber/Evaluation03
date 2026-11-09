package edu.charlotte.evaluation03.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.charlotte.evaluation03.R;
import edu.charlotte.evaluation03.databinding.FragmentMyCookBookBinding;
import edu.charlotte.evaluation03.models.MealDetail;

public class MyCookBookFragment extends Fragment {
    public MyCookBookFragment() {
        // Required empty public constructor
    }

    FragmentMyCookBookBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMyCookBookBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("My CookBook");
        binding.buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.goBackToSearch();
            }
        });
    }

    CookBookListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mListener = (CookBookListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement CookBookListener");
        }
    }

    public interface CookBookListener {
        void goBackToSearch();
        void gotoMealDetails(MealDetail mealDetail);
    }
}