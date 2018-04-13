package com.zflabs.popularmovies.util;

import android.content.ContentValues;
import android.content.Context;

import com.zflabs.popularmovies.MovieData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class MovieDBJsonUtils {


    private static final String NOT_AVAILABLE = "N/A";

    public static MovieData[] getSimpleMovieStringsFromJson(Context context, String forecastJsonStr)
            throws JSONException {

        final String RESULT_LIST = "results";

        MovieData[] movieDataCollection = null;

        JSONObject moviesJson = new JSONObject(forecastJsonStr);

        JSONArray moviesArray = moviesJson.getJSONArray(RESULT_LIST);

        movieDataCollection = new MovieData[moviesArray.length()];

        for (int i = 0; i < moviesArray.length(); i++) {
            JSONObject actualMovie = moviesArray.getJSONObject(i);

            MovieData movieData = extractMovieDataFromJson(actualMovie);
            movieDataCollection[i] =  movieData;
        }

        return movieDataCollection;

    }

    public static MovieData extractMovieDataFromString(String movie) {
        try {
            return extractMovieDataFromJson(new JSONObject(movie));
        } catch (JSONException e) {
            return null;
        }
    }

    public static MovieData extractMovieDataFromJson(JSONObject actualMovie) throws
            JSONException {
        final String POSTER = "poster_path";
        double voteAvarage = actualMovie.optDouble("vote_average", 0.0);
        String title = actualMovie.optString("title", NOT_AVAILABLE);
        String releaseDate = actualMovie.optString("release_date", NOT_AVAILABLE);
        String synopsis = actualMovie.optString("overview", NOT_AVAILABLE);
        String poster = actualMovie.getString(POSTER);


        MovieData movieData = new MovieData(title, releaseDate, poster, voteAvarage, synopsis);
        return movieData;
    }
}