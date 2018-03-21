package com.example.android.bakingapp;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.android.bakingapp.data.Ingredient;
import com.example.android.bakingapp.data.Recipe;
import com.example.android.bakingapp.data.Step;

import java.util.ArrayList;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeActivity extends AppCompatActivity implements StepsFragment.OnFragmentInteractionListener {

    public static final String RECIPE_KEY = "recipe_key";
    public static final String RECIPE_NAME_KEY = "recipe_name_key";
    private static final String SCROLL_POSITION_KEY = "scroll_position_key";
    private static final String STEPS_LIST_KEY = "steps_list_key";
    private static final String INGREDIENTS_LIST_KEY = "ingredients_list_key";

    //fragment tags
    private static final String INSTRUCTION_TAG = "instruction_tag";
    private static final String VIDEO_TAG = "video_tag";
    private static final String THUMBNAIL_TAG = "thumbnail_tag";

    @BindView(R.id.scroll_view)
    NestedScrollView mScrollView;
    @BindString(R.string.widget_toast)
     String mWidgetToastString;
    private String mRecipeName;
    private boolean mTwoPane;
    private ArrayList<Step> mSteps;
    private ArrayList<Ingredient> mIngredients;
    private int mScrollYPosition = 0;
    private ActionBar mActionBar;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        ButterKnife.bind(this);
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            mActionBar = actionBar;
            actionBar.setDisplayHomeAsUpEnabled(true);

        }
        if (findViewById(R.id.tablet_layout) != null) {
            mTwoPane = true;
        }
        mScrollView.setNestedScrollingEnabled(false);
        saveScrollPosition(savedInstanceState);
        if (savedInstanceState == null) {
            Recipe recipe = getIntent().getParcelableExtra(RECIPE_KEY);

            if (recipe != null) {
                mRecipeName = recipe.getName();
                IngredientsFragment ingredientsFragment = new IngredientsFragment();
                mIngredients = (ArrayList<Ingredient>) recipe.getIngredients();
                ingredientsFragment.setIngredients(recipe.getIngredients());
                StepsFragment stepsFragment = new StepsFragment();
                mSteps = recipe.getSteps();
                stepsFragment.setSteps(mSteps);
                stepsFragment.setTwoPane(mTwoPane);
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .add(R.id.ingredient_layout, ingredientsFragment)
                        .add(R.id.step_layout, stepsFragment)
                        .commit();
            }
        } else {
            mSteps = savedInstanceState.getParcelableArrayList(STEPS_LIST_KEY);
            mIngredients = savedInstanceState.getParcelableArrayList(INGREDIENTS_LIST_KEY);
            mRecipeName = savedInstanceState.getString(RECIPE_NAME_KEY);
        }
        mActionBar.setTitle(mRecipeName);
    }

    private void saveScrollPosition(final Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mScrollYPosition = savedInstanceState.getInt(SCROLL_POSITION_KEY);
        }
        mScrollView.post(new Runnable() {
            public void run() {
                mScrollView.scrollTo(mScrollView.getScrollX(), mScrollYPosition);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.recipe_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.widget_button:
           createWidget();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

   private void createWidget(){

       String  ingredientsList ="";
       StringBuilder sb = new StringBuilder();
       for (int i = 0; i < mIngredients.size(); i++) {
           Ingredient ingredient = mIngredients.get(i);
           String name = ingredient.getIngredient();
           String measure = ingredient.getMeasure();
           float quantity = ingredient.getQuantity();
           String quantityString = Float.toString(quantity);
           String ing = String.format("%s %s %s \n", quantityString, measure, name);
           sb.append(ing);
           ingredientsList = sb.toString();
       }
       AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
       RemoteViews remoteViews = new RemoteViews(this.getPackageName(), R.layout.baking_app_widget);
       ComponentName thisWidget = new ComponentName(this, BakingAppWidget.class);
       remoteViews.setTextViewText(R.id.appwidget_head_text, mRecipeName);
       remoteViews.setTextViewText(R.id.appwidget_text, ingredientsList);
       appWidgetManager.updateAppWidget(thisWidget, remoteViews);
       Toast.makeText(this, mWidgetToastString,
               Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        int ScrollPos = mScrollView.getScrollY();
        outState.putInt(SCROLL_POSITION_KEY, ScrollPos);
        outState.putString(RECIPE_NAME_KEY, mRecipeName);
        outState.putParcelableArrayList(STEPS_LIST_KEY, mSteps);
        outState.putParcelableArrayList(INGREDIENTS_LIST_KEY, mIngredients);
    }

    @Override
    public void onStepItemClicked(Step step) {
        if (mTwoPane) {
            loadFragmentsForDetailPane(step);
        } else {
            Intent recipeDetailIntent = new Intent(this, RecipeDetailActivity.class);
            recipeDetailIntent.putExtra(RecipeDetailActivity.STEPS_KEY, mSteps);
            recipeDetailIntent.putExtra(RecipeDetailActivity.STEP_POSITION_KEY, getStepPos(step));
            startActivity(recipeDetailIntent);
        }
    }

    private int getStepPos(Step step) {
        return mSteps.indexOf(step);
    }

    void loadFragmentsForDetailPane(Step step) {
        String URLString = step.getVideoURL();
        String thumbnailUlrString = step.getThumbnailURL();
        String description = step.getDescription();
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
        Fragment thumbnailFragmentOld = fragmentManager.findFragmentByTag(THUMBNAIL_TAG);
        //Check if we have a video valid url
        if (!URLString.equals("")) {
            VideoFragment videoFragment = new VideoFragment();
            videoFragment.setVideoUrl(URLString);
            videoFragment.setIsTwoPane(mTwoPane);
            //check if thumbnailFragment is showing if so remove
            if (thumbnailFragmentOld != null) {
                fragmentManager.beginTransaction()
                        .remove(thumbnailFragmentOld).commit();
            }
            //If there is no videoFragment add one
            if (videoFragmentOld == null) {
                fragmentManager.beginTransaction()
                        .add(R.id.video_layout, videoFragment, VIDEO_TAG).commit();
            } else {
                //if there is a videoFragment replace it with the new
                fragmentManager.beginTransaction().setCustomAnimations(android.R.anim.fade_out, android.R.anim.fade_in)
                        .replace(R.id.video_layout, videoFragment, VIDEO_TAG).commit();
            }
        } else {
            // no valid video url check and remove any videoFragments
            if (videoFragmentOld != null) {
                fragmentManager.beginTransaction()
                        .remove(videoFragmentOld).commit();
                //no video lets load thumbnail fragment
            }
            ThumbnailFragment thumbnailFragment = new ThumbnailFragment();
             thumbnailFragment.setThumbnailUlrString(thumbnailUlrString);

            //If there is no thumbnailFragment add one
            if (thumbnailFragmentOld == null) {
                fragmentManager.beginTransaction()
                        .add(R.id.video_layout, thumbnailFragment, THUMBNAIL_TAG).commit();
            } else {
                //if there is a thumbnailFragment replace it with the new
                fragmentManager.beginTransaction().setCustomAnimations(android.R.anim.fade_out, android.R.anim.fade_in)
                        .replace(R.id.video_layout, thumbnailFragment, THUMBNAIL_TAG).commit();
            }
        }
    }

}
