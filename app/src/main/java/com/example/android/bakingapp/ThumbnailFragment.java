package com.example.android.bakingapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class ThumbnailFragment extends Fragment {

    private static final String THUMBNAIL_KEY = "thumbnail_key";
private String mThumbnailUlrString;
    private Unbinder mUnbinder;
    @BindView(R.id.thumbnail)
    ImageView mThumbnail;

    public void setThumbnailUlrString(String thumbnailUlrString) {
       mThumbnailUlrString = thumbnailUlrString;
    }

    public ThumbnailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_thumbnail, container, false);
        mUnbinder = ButterKnife.bind(this, rootView);
        if(savedInstanceState != null){
            mThumbnailUlrString = savedInstanceState.getString(THUMBNAIL_KEY);
        }

        //check ulr string is not null or not empty before trying to fetch thumbnail
        if ((mThumbnailUlrString!=null)&&(!mThumbnailUlrString.equals(""))){
            //If picasso fails to fetch thumbnail show a cake
            Picasso.with(getActivity())
                    .load(mThumbnailUlrString)
                    .placeholder(R.drawable.cake)
                    .error(R.drawable.cake)
                    .into(mThumbnail);
        }
        return rootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle currentState) {
        currentState.putString(THUMBNAIL_KEY, mThumbnailUlrString);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
