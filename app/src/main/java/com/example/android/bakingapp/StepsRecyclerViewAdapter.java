package com.example.android.bakingapp;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.data.Step;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepsRecyclerViewAdapter extends RecyclerView.Adapter<StepsRecyclerViewAdapter.StepsViewHolder> {

    private List<Step> mDataSource;
    private final StepsRecyclerViewAdapter.ListItemClickListener mListItemClickListener;
    private int mRowSelected;
    private boolean mIsRowSelected;
    private boolean mIsTwoPane;

    StepsRecyclerViewAdapter(List<Step> dataSource,
                             StepsRecyclerViewAdapter.ListItemClickListener listItemClickListener,
                             int rowSelected, boolean isSelected, boolean isTwoPane) {
        mDataSource = dataSource;
        mListItemClickListener = listItemClickListener;
        mRowSelected = rowSelected;
        mIsRowSelected = isSelected;
        mIsTwoPane = isTwoPane;
    }

    @Override
    public @NonNull
    StepsRecyclerViewAdapter.StepsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.step_item_view, parent, false);
        return new StepsRecyclerViewAdapter.StepsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull StepsRecyclerViewAdapter.StepsViewHolder holder, int position) {
        holder.bind(position);
        if ((mIsRowSelected) && (mIsTwoPane)) {
            if (mRowSelected == position) {
                holder.mViewHolderItem.setBackgroundResource(R.color.colorPrimaryLight);
            } else {
                holder.mViewHolderItem.setBackgroundResource(R.color.colorBackgroundWhite);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (mDataSource == null) {
            return 0;
        }
        return mDataSource.size();
    }

    public interface ListItemClickListener {
        void onListItemClick(Step step, int rowSelected);
    }

    class StepsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.step_item_tv)
        TextView mStepTextView;
        @BindView(R.id.step_item_id_tv)
        TextView mStepIdTextView;
        @BindView(R.id.step_item_vh)
        ConstraintLayout mViewHolderItem;

        StepsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        void bind(int listIndex) {
            Step step = mDataSource.get(listIndex);
            mStepTextView.setText(step.getShortDescription());
            String id = Integer.toString(step.getId());
            if (id.equals("0")) {
                id = "";
            }
            mStepIdTextView.setText(id);
        }

        @Override
        public void onClick(View view) {
            mRowSelected = getAdapterPosition();
            Step step = mDataSource.get(mRowSelected);
            mListItemClickListener.onListItemClick(step, mRowSelected);
            mIsRowSelected = true;
            System.out.println(mRowSelected);
            notifyDataSetChanged();

        }
    }
}
