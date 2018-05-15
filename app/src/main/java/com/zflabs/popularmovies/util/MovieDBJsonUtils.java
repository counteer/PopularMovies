package com.zflabs.popularmovies.util;

import android.content.ContentValues;
import android.content.Context;

import com.zflabs.popularmovies.MovieData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.StringJoiner;

public final class MovieDBJsonUtils {

    private static final String RESULT_LIST = "results";

    private static final String NOT_AVAILABLE = "N/A";

    public static MovieData[] getSimpleMovieStringsFromJson(Context context, String forecastJsonStr)
            throws JSONException {

        MovieData[] movieDataCollection;

        JSONObject moviesJson = new JSONObject(forecastJsonStr);

        JSONArray moviesArray = moviesJson.getJSONArray(RESULT_LIST);

        movieDataCollection = new MovieData[moviesArray.length()];

        for (int i = 0; i < moviesArray.length(); i++) {
            JSONObject actualMovie = moviesArray.getJSONObject(i);

            MovieData movieData = extractMovieDataFromJson(actualMovie);
            movieDataCollection[i] = movieData;
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
        int outerId = actualMovie.optInt("id", -1);
        int id = actualMovie.optInt("_id", -1);
        String video = actualMovie.optString("video", "");
        String review = actualMovie.optString("review", "");

        MovieData movieData = new MovieData(outerId, title, releaseDate, poster, voteAvarage, synopsis);
        movieData.setId(id);
        movieData.setVideo(video);
        movieData.setReview(review);
        return movieData;
    }

    public static String getVideoUrlFromTrailerJson(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        JSONArray result = jsonObject.getJSONArray(RESULT_LIST);
        String[] videos = new String[result.length()];
        for (int i = 0; i < result.length(); ++i) {
            JSONObject jsonObject1 = result.getJSONObject(i);
            videos[i] = jsonObject1.getString("key");
        }
        return convertTrailerArrayToString(videos);
    }

    private static String convertTrailerArrayToString(String[] videos) {
        StringBuilder result = new StringBuilder();
        for(String video :videos ){
            result.append(video).append(",");
        }
        result.setLength(result.length()-1);
        return result.toString();
    }

    public static String[] convertStringToTrailerArray(String trailers){
        return trailers.split(",");
    }

    public static String getReviewFromReviewJson(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        JSONArray result = jsonObject.getJSONArray(RESULT_LIST);
        String result2 = "";
        for (int i = 0; i < result.length(); ++i) {
            JSONObject jsonObject1 = result.getJSONObject(i);
            String author = jsonObject1.getString("author");
            String content = jsonObject1.getString("content");
            result2 = author + ": \n" + content + "\n";
        }
        return result2;
    }
}