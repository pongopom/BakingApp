package com.example.android.bakingapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.bakingapp.data.Step;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RecipeDetailActivity extends AppCompatActivity {
    //keys used by intent
    public static final String STEP_KEY = "step_key";
    public static final String STEPS_KEY = "steps_key";
    //keys used for saved instance state
    private static final String STEPS_LIST_KEY = "steps_list_key";
    private static final String STEP_ID_KEY = "step_id_key";
    //fragment tags
    private static final String INSTRUCTION_TAG = "instruction_tag";
    private static final String VIDEO_TAG = "video_tag";

    private ArrayList<Step> mSteps;
    // view bindings
    private int mStepId;
    @BindView(R.id.step_count_tv)
    TextView mStepTextView;
    @BindView(R.id.prev_button)
    Button mPrevButton;
    @BindView(R.id.next_button)
    Button mNextButton;
    @BindView(R.id.nav_bar)
    CardView mNavBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        ButterKnife.bind(this);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

            mNavBar.setVisibility(View.VISIBLE);
        }
        if (savedInstanceState == null) {
            Intent intent = getIntent();
            Step step = intent.getParcelableExtra(STEP_KEY);
            mSteps = intent.getParcelableArrayListExtra(STEPS_KEY);
            if (step != null) {

                loadFragments(step);
            }
        } else {
            mSteps = savedInstanceState.getParcelableArrayList(STEPS_LIST_KEY);
            mStepId = savedInstanceState.getInt(STEP_ID_KEY);
            setUpStepNav();
        }
    }

    @OnClick(R.id.prev_button)
    void prevClicked() {
        Step step = mSteps.get(mStepId - 1);
        loadFragments(step);
    }

    @OnClick(R.id.next_button)
    void nextClicked() {
        Step step = mSteps.get(mStepId + 1);
        loadFragments(step);
    }

    //to navigate between steps in portrait on phone only
    void setUpStepNav() {
        String stringId = Integer.toString(mStepId);
        if (mStepId == 0) {
            mPrevButton.setVisibility(View.INVISIBLE);
        } else {
            mPrevButton.setVisibility(View.VISIBLE);
        }
        if (mStepId == mSteps.size() - 1) {
            mNextButton.setVisibility(View.INVISIBLE);
        } else {
            mNextButton.setVisibility(View.VISIBLE);
        }
        mStepTextView.setText(stringId);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    void loadFragments(Step step) {
        String URLString = step.getVideoURL();
        String description = step.getDescription();
        mStepId = step.getId();
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment instructionsFragmentOld = fragmentManager.findFragmentByTag(INSTRUCTION_TAG);
        InstructionsFragment instructionsFragment = new InstructionsFragment();
        instructionsFragment.setInstructions(description);
        if (instructionsFragmentOld == null) {
            fragmentManager.beginTransaction()
                    .add(R.id.instruction_layout, instructionsFragment, INSTRUCTION_TAG).commit();
        } else {
            fragmentManager.beginTransaction().setCustomAnimations(android.R.anim.fade_out, android.R.anim.fade_in)
                    .replace(R.id.instruction_layout, instructionsFragment, INSTRUCTION_TAG).commit();
        }
        Fragment videoFragmentOld = fragmentManager.findFragmentByTag(VIDEO_TAG);
        if (!URLString.equals("")) {
            VideoFragment videoFragment = new VideoFragment();
            videoFragment.setVideoUrl(URLString);
            videoFragment.setIsTwoPane(false);
            if (videoFragmentOld == null) {
                fragmentManager.beginTransaction()
                        .add(R.id.video_layout, videoFragment, VIDEO_TAG).commit();
            } else {
                fragmentManager.beginTransaction().setCustomAnimations(android.R.anim.fade_out, android.R.anim.fade_in)
                        .replace(R.id.video_layout, videoFragment, VIDEO_TAG).commit();
            }
        } else {
            if (videoFragmentOld != null) {
                fragmentManager.beginTransaction()
                        .remove(videoFragmentOld).commit();
            }
        }
        setUpStepNav();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STEP_ID_KEY, mStepId);
        outState.putParcelableArrayList(STEPS_LIST_KEY, mSteps);
    }
}
