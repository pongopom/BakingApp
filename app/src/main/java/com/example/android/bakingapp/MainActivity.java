package com.example.android.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.android.bakingapp.data.Recipe;

import com.example.android.bakingapp.utils.EspressoTestingIdlingResource;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;



public class MainActivity extends AppCompatActivity implements BakeRecyclerViewAdapter.ListItemClickListener {

    @BindView(R.id.error_tv)
    TextView mErrorTextView;
    @BindView(R.id.loading_pb)
    ProgressBar mLoadingBar;
    @BindView(R.id.bake_rv)
    RecyclerView mBakeRecyclerView;
    @BindString(R.string.recipe_url)
    String mRecipeURL;
    @BindString(R.string.network_error)
    String mNetworkError;
    @BindString(R.string.no_network)
    String mNoNetwork;

    private static ArrayList<Recipe> mDataSource;
    private BakeRecyclerViewAdapter mBakeRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mDataSource = new ArrayList<>();
        if (findViewById(R.id.tablet_layout) != null) {
            //Grid layout on tablet
            mBakeRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        } else {
            //linear layout phone
            mBakeRecyclerView.addItemDecoration(new DividerItemDecoration(this,
                    DividerItemDecoration.VERTICAL));
            mBakeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
        mBakeRecyclerViewAdapter = new BakeRecyclerViewAdapter(mDataSource, this);
        mBakeRecyclerView.setHasFixedSize(true);
        mBakeRecyclerView.setAdapter(mBakeRecyclerViewAdapter);
        if(isConnected()){
            fetchRecipeData();
        }else{
        mErrorTextView.setVisibility(View.VISIBLE);
        mErrorTextView.setText(mNoNetwork);}
    }

    private void fetchRecipeData() {
        mLoadingBar.setVisibility(View.VISIBLE);
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        EspressoTestingIdlingResource.increment();

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(
                Request.Method.GET,
                mRecipeURL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        ArrayList<Recipe> recipes = new ArrayList<>();
                        try {
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject recipeObject = array.getJSONObject(i);
                                Gson gson = new GsonBuilder().create();
                                Recipe r = gson.fromJson(String.valueOf(recipeObject), Recipe.class);
                                recipes.add(r);
                            }
                            mLoadingBar.setVisibility(View.INVISIBLE);
                            mDataSource = recipes;
                            mBakeRecyclerViewAdapter.setDataSource(mDataSource);
                            EspressoTestingIdlingResource.decrement();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mLoadingBar.setVisibility(View.INVISIBLE);
                        mErrorTextView.setVisibility(View.VISIBLE);
                    }
                }
        );
        requestQueue.add(jsonObjectRequest);
    }

    protected boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager!=null){
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
        return  (netInfo != null && netInfo.isConnectedOrConnecting()) ;}
        return true;
    }

    @Override
    public void onListItemClick(Recipe recipe) {
        Intent detailIntent = new Intent(this, RecipeActivity.class);
        detailIntent.putExtra(RecipeActivity.RECIPE_KEY, recipe);
        startActivity(detailIntent);
    }
}
