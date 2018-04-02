package com.zflabs.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zflabs.popularmovies.util.MovieDBJsonUtils;
import com.zflabs.popularmovies.util.NetworkUtils;

public class DetailActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView synopsisTV;
    private TextView titleTV;
    private TextView releaseDateTV;
    private TextView ratingTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intentThatStartedThisActivity = getIntent();

        imageView = (ImageView) findViewById(R.id.detail_image);
        synopsisTV  = (TextView) findViewById(R.id.synopsis);
        titleTV  =(TextView) findViewById(R.id.movie_title);
        releaseDateTV = (TextView) findViewById(R.id.movie_release_date);
        ratingTV = (TextView) findViewById(R.id.movie_rating);
        if (intentThatStartedThisActivity != null && intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)) {
            String movie = intentThatStartedThisActivity.getStringExtra(Intent.EXTRA_TEXT);
            MovieData movieData = MovieDBJsonUtils.extractMovieDataFromString(movie);
            titleTV.setText(movieData.getTitle());
            synopsisTV.setText(movieData.getSynopsis());
            releaseDateTV.setText(movieData.getReleaseDate());
            ratingTV.setText(Double.toString(movieData.getVoteAverage()));
            String path = NetworkUtils.buildImageUrl(movieData.getPoster());
            Picasso.with(this).load(path).into(imageView);
        }
    }

}
