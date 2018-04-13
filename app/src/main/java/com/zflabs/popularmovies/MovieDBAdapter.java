package com.zflabs.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.zflabs.popularmovies.util.NetworkUtils;

public class MovieDBAdapter extends Adapter<MovieDBAdapter.MovieAdapterViewHolder>
        {

    private MovieData[] movieData;

    private final MovieAdapterClickHandler mClickHandler;

    public interface MovieAdapterClickHandler {
        void onClick(MovieData movieData);
    }

    public MovieDBAdapter(MovieAdapterClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        public final ImageView imageView;

        public MovieAdapterViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.poster_image);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            MovieData actualMovieData = movieData[adapterPosition];
            mClickHandler.onClick(actualMovieData);
        }
    }

    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movie_poster_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder movieAdapterViewHolder, int position) {
        Context context = movieAdapterViewHolder.imageView.getContext();
        String path = NetworkUtils.buildImageUrl(movieData[position].getPoster());
        Log.i("ADAPTER", path);
        Picasso.with(context).load(path).into(movieAdapterViewHolder.imageView);
    }

    @Override
    public int getItemCount() {
        if (null == movieData) return 0;
        return movieData.length;
    }

    public void setMovieData(MovieData[] movieData) {
        this.movieData = movieData;
        notifyDataSetChanged();
    }
}