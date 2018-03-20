package com.example.android.bakingapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.bakingapp.data.Step;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class StepsFragment extends Fragment implements StepsRecyclerViewAdapter.ListItemClickListener {

    private static final String STEPS_KEY = "steps_key";
    private static final String ROW_SELECTED_KEY = "row_selected_key";
    private static final String IS_ROW_SELECTED_KEY = "is_row_selected_key";
    private static final String IS_TWO_PANE_KEY = "is_two_pane_key";

    private OnFragmentInteractionListener mListener;
    private Context mContext;
    private ArrayList<Step> mSteps = null;
    private  int mRowSelected = 0;
    private  boolean mIsRowSelected = false;
    private boolean mTwoPane;
    private Unbinder mUnbinder;
    @BindView(R.id.steps_rv)
    RecyclerView mStepsRecyclerView;

    public void setSteps(ArrayList<Step> steps) {
        mSteps = steps;
    }

    public void setTwoPane(boolean twoPane) {
        mTwoPane = twoPane;
    }

    public StepsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_steps, container, false);
        if (savedInstanceState != null){
            mSteps = savedInstanceState.getParcelableArrayList(STEPS_KEY);
        mIsRowSelected = savedInstanceState.getBoolean(IS_ROW_SELECTED_KEY);
        mRowSelected = savedInstanceState.getInt(ROW_SELECTED_KEY);
            mTwoPane = savedInstanceState.getBoolean(IS_TWO_PANE_KEY);
        }
        mUnbinder = ButterKnife.bind(this, rootView);
        mStepsRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
        mStepsRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        StepsRecyclerViewAdapter stepsRecyclerViewAdapter =
                new StepsRecyclerViewAdapter(mSteps, this, mRowSelected, mIsRowSelected, mTwoPane);
        mStepsRecyclerView.setHasFixedSize(true);
        mStepsRecyclerView.setAdapter(stepsRecyclerViewAdapter);
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onListItemClick(Step step, int rowSelected) {
        if (mListener != null) {
            mListener.onStepItemClicked(step);
            mRowSelected = rowSelected;
            mIsRowSelected = true;

        }
    }

    public interface OnFragmentInteractionListener {
        void onStepItemClicked(Step step);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle currentState) {
        currentState.putParcelableArrayList(STEPS_KEY, mSteps);
        currentState.putInt(ROW_SELECTED_KEY, mRowSelected);
        currentState.putBoolean(IS_ROW_SELECTED_KEY, mIsRowSelected);
        currentState.putBoolean(IS_TWO_PANE_KEY, mTwoPane);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
