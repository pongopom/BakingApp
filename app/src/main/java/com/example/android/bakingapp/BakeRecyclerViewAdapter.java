package com.example.android.bakingapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.data.Recipe;

import java.util.ArrayList;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by peterpomlett on 06/03/2018.
 * MainActivity List of recipes
 */

    public class BakeRecyclerViewAdapter extends RecyclerView.Adapter<BakeRecyclerViewAdapter.BakeViewHolder> {
    private ArrayList<Recipe> mDataSource;
    private final ListItemClickListener mListItemClickListener;

         BakeRecyclerViewAdapter(ArrayList<Recipe> dataSource, ListItemClickListener listItemClickListener) {
            mDataSource = dataSource;
            mListItemClickListener = listItemClickListener;
        }

     void setDataSource(ArrayList<Recipe>dataSource) {
        mDataSource = dataSource;
        notifyDataSetChanged();
    }

        @Override
        public @NonNull BakeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.bake_item_view, parent, false);
            return new BakeViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull BakeViewHolder holder, int position) {
            holder.bind(position);
        }

        @Override
        public int getItemCount() {
            if (mDataSource == null) {
                return 0;
            }
            return mDataSource.size();
        }

    public interface ListItemClickListener {
        void onListItemClick(Recipe recipe);
    }

         class BakeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
             @BindView(R.id.recipe_tv) TextView mRecipeTextView;
             @BindView(R.id.serves_tv) TextView mServingsTextView;
             @BindString(R.string.serves) String mServes;
             BakeViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
                 itemView.setOnClickListener(this);
            }

             void bind(int listIndex) {
                Recipe  recipe = mDataSource.get(listIndex) ;
                mRecipeTextView.setText(recipe.getName());
                String servingCount = Integer.toString(recipe.getServings());
                String servings = String.format ("%s %s",mServes, servingCount);
                mServingsTextView.setText(servings);
            }

             @Override
             public void onClick(View view) {
                 int position = getAdapterPosition();
                 Recipe recipe = mDataSource.get(position);
                 mListItemClickListener.onListItemClick(recipe);
             }
        }
}
