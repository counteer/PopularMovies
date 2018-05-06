package com.zflabs.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

import java.util.Arrays;
import java.util.List;

public class CachedMovieContract {

    public static final String AUTHORITY = "com.zflabs.popularmovies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    private static final String PATH_MOVIES = "movies";

    public static final Uri CONTENT_URI =
            BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

    public static final class CachedMovieEntry implements BaseColumns {


        public static final String TABLE_NAME = "cachedMovies";

        public static final String COLUMN_MOVIE_ID = "_id";
        public static final String COLUMN_MOVIE_TITLE = "movieTitle";
        public static final String COLUMN_MOVIE_POSTER = "moviePoster";
        public static final String COLUMN_RELEASE_DATE = "releaseDate";
        public static final String COLUMN_VOTE_AVERAGE = "voteAverage";
        public static final String COLUMN_SYNOPSIS = "synopsis";
        public static final String COLUMN_OUTER_ID = "outerId";
        public static final String COLUMN_TRAILER = "video";
        public static final String COLUMN_REVIEW = "review";
        public static final String[] COLUMN_ARRAY = Arrays.asList(
                COLUMN_MOVIE_ID,
                COLUMN_MOVIE_TITLE,
                COLUMN_MOVIE_POSTER,
                COLUMN_SYNOPSIS,
                COLUMN_RELEASE_DATE,
                COLUMN_VOTE_AVERAGE,
                COLUMN_OUTER_ID,
                COLUMN_TRAILER,
                COLUMN_REVIEW).toArray(new String[6]);
    }
}
