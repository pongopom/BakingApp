package com.example.android.bakingapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class InstructionsFragment extends Fragment {

    private static final String INSTRUCTIONS_LIST_KEY = "instructions_list_key";
    String mInstructions;
    private Unbinder mUnbinder;
    @BindView(R.id.instructions_fragment_tv)
    TextView mInstructionsTextView;

    public void setInstructions(String instructions) {
        mInstructions = instructions;
    }

    public InstructionsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_instructions, container, false);
        mUnbinder = ButterKnife.bind(this, rootView);
        if (savedInstanceState != null) {
            mInstructions = (savedInstanceState.getString(INSTRUCTIONS_LIST_KEY));
        }
        mInstructionsTextView.setText(mInstructions);
        return rootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle currentState) {
        currentState.putString(INSTRUCTIONS_LIST_KEY, mInstructions);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
