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
import edu.charlotte.evaluation03.databinding.FragmentCategoriesBinding;
import edu.charlotte.evaluation03.models.Category;

public class CategoriesFragment extends Fragment {
    public CategoriesFragment() {
        // Required empty public constructor
    }

    FragmentCategoriesBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCategoriesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Categories");
        binding.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.cancelCategorySelection();
            }
        });
    }

    CategoriesListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mListener = (CategoriesListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement CategoriesListener");
        }
    }

    public interface CategoriesListener {
        void sendBackSelectedCategory(Category category);
        void cancelCategorySelection();
    }
}