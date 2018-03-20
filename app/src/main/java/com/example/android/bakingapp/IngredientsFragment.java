package com.example.android.bakingapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.data.Ingredient;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class IngredientsFragment extends Fragment {

    private static final String INGREDIENTS_LIST_KEY = "ingredients_list_key";
    private List<Ingredient> mIngredients = null;
    private String mIngredientsList;
    private Unbinder mUnbinder;
    @BindView(R.id.ingredient_fragment_tv)
    TextView mIngredientTextView;

    public void setIngredients(List<Ingredient> ingredients) {
        mIngredients = ingredients;
    }

    public IngredientsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_ingredients, container, false);
        mUnbinder = ButterKnife.bind(this, rootView);

        if (savedInstanceState == null) {
            if(mIngredients != null){
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < mIngredients.size(); i++) {
                    Ingredient ingredient = mIngredients.get(i);
                    String name = ingredient.getIngredient();
                    String measure = ingredient.getMeasure();
                    float quantity = ingredient.getQuantity();
                    String quantityString = Float.toString(quantity);
                    String ing = String.format("%s %s %s \n", quantityString, measure, name);
                    sb.append(ing);
                    mIngredientsList = sb.toString();
                }
            }

        } else {
            mIngredientsList = (savedInstanceState.getString(INGREDIENTS_LIST_KEY));
        }
        mIngredientTextView.setText(mIngredientsList);
        return rootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle currentState) {
        currentState.putString(INGREDIENTS_LIST_KEY, mIngredientsList);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
