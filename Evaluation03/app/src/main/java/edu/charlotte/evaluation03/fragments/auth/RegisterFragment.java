package edu.charlotte.evaluation03.fragments.auth;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import edu.charlotte.evaluation03.R;
import edu.charlotte.evaluation03.databinding.FragmentRegisterBinding;

public class RegisterFragment extends Fragment {
    public RegisterFragment() {
        // Required empty public constructor
    }

    FragmentRegisterBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Register");
        binding.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.gotoLoginScreen();
            }
        });

        binding.buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.editTextEmailAddress.getText().toString();
                String password = binding.editTextPassword.getText().toString();
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(getActivity(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else {
                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                mListener.gotoSearchScreen();
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(getActivity(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }
        });
    }

    RegisterListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mListener = (RegisterListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement RegisterListener");
        }
    }

    public interface RegisterListener {
        void gotoSearchScreen();
        void gotoLoginScreen();
    }
}