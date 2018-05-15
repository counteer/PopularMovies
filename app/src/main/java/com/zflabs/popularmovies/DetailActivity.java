package com.zflabs.popularmovies;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.zflabs.popularmovies.data.CachedMovieContract;
import com.zflabs.popularmovies.data.CachedMovieContract.CachedMovieEntry;
import com.zflabs.popularmovies.util.MovieDBJsonUtils;
import com.zflabs.popularmovies.util.NetworkUtils;

public class DetailActivity extends AppCompatActivity implements TrailerAdapter.TrailerAdapterClickHandler {

    private ImageView imageView;
    private TextView synopsisTV;
    private TextView titleTV;
    private TextView releaseDateTV;
    private TextView ratingTV;
    private TextView favorite;
    private RecyclerView trailerView;
    private TextView review;

    private MovieData movieData;
    private TrailerAdapter trailerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intentThatStartedThisActivity = getIntent();
        trailerView = (RecyclerView) findViewById(R.id.rv_trailers);
        trailerView.setLayoutManager(new GridLayoutManager(this, 1));
        imageView = (ImageView) findViewById(R.id.detail_image);
        synopsisTV = (TextView) findViewById(R.id.synopsis);
        titleTV = (TextView) findViewById(R.id.movie_title);
        releaseDateTV = (TextView) findViewById(R.id.movie_release_date);
        ratingTV = (TextView) findViewById(R.id.movie_rating);
        favorite = (TextView) findViewById(R.id.add_favorite);
        review = (TextView) findViewById(R.id.reviews_view);
        if (intentThatStartedThisActivity != null && intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)) {
            String movie = intentThatStartedThisActivity.getStringExtra(Intent.EXTRA_TEXT);
            MovieData movieData = MovieDBJsonUtils.extractMovieDataFromString(movie);
            this.movieData = movieData;
            titleTV.setText(movieData.getTitle());
            synopsisTV.setText(movieData.getSynopsis());
            releaseDateTV.setText(movieData.getReleaseDate());
            ratingTV.setText(Double.toString(movieData.getVoteAverage()));
            if (isFavorite()) {
                favorite.setText("-");
            } else {
                favorite.setText("+");
            }
            review.setText(movieData.getReview());
            String path = NetworkUtils.buildImageUrl(movieData.getPoster());
            Picasso.with(this).load(path).into(imageView);
            trailerAdapter = new TrailerAdapter(this);

            trailerView.setAdapter(trailerAdapter);
            String[] trailers = MovieDBJsonUtils.convertStringToTrailerArray(movieData.getVideo());
            trailerAdapter.setTrailerData(trailers);
        }
    }

    public void onClick(View textView) {
        if (textView.getId() == favorite.getId()) {
            if (isFavorite()) {
                removeFromFavorite();
                Toast.makeText(this, R.string.remove_from_favorites, Toast.LENGTH_SHORT).show();
                favorite.setText("+");
            } else {
                Toast.makeText(this, R.string.add_to_favorites, Toast.LENGTH_SHORT).show();
                addToFavorite();
                favorite.setText("-");
            }
        }
    }

    private void addToFavorite() {
        ContentValues initialValues = new ContentValues();
        initialValues.put(CachedMovieEntry.COLUMN_MOVIE_TITLE, movieData.getTitle());
        initialValues.put(CachedMovieEntry.COLUMN_MOVIE_POSTER, movieData.getPoster());
        initialValues.put(CachedMovieEntry.COLUMN_RELEASE_DATE, movieData.getReleaseDate());
        initialValues.put(CachedMovieEntry.COLUMN_SYNOPSIS, movieData.getSynopsis());
        initialValues.put(CachedMovieEntry.COLUMN_VOTE_AVERAGE, movieData.getVoteAverage());
        initialValues.put(CachedMovieEntry.COLUMN_OUTER_ID, movieData.getOuterId());
        initialValues.put(CachedMovieEntry.COLUMN_TRAILER, movieData.getVideo());
        initialValues.put(CachedMovieEntry.COLUMN_REVIEW, movieData.getReview());
        getContentResolver().insert(CachedMovieContract.BASE_CONTENT_URI, initialValues);
    }

    private boolean removeFromFavorite() {
        int deleted = getContentResolver().delete(CachedMovieContract.BASE_CONTENT_URI, "_id = " + movieData.getId(), null);
        return deleted > 0;
    }

    private boolean isFavorite() {
        Cursor query = getContentResolver().query(CachedMovieContract.BASE_CONTENT_URI, new String[]{CachedMovieEntry.COLUMN_MOVIE_ID}, "outerId=" + movieData.getOuterId(), null, null);
        if (query.getCount() < 1) {
            return false;
        }
        query.moveToNext();
        movieData.setId(query.getInt(0));
        return true;
    }

    @Override
    public void onClick(String trailerId) {
        Context context = getApplicationContext();
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + trailerId));
        try {
            context.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            appIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://www.youtube.com/watch?v=" + trailerId));
            context.startActivity(appIntent);
        }
    }
}
