package com.zflabs.popularmovies.util;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String BASE_URL =
            "https://api.themoviedb.org/3/";

    private static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";

    private static final String IMAGE_SIZE_PATH = "w185";
    final static String MOVIE_PATH = "movie";
    final static String TOP_RATED_PATH = "top_rated";
    final static String POPULAR_PATH = "popular";
    final static String API_KEY_PARAM = "api_key";
    final static String LANGUAGE_PARAM = "language";
    final static String DEFAULT_LANGUAGE = "en-US";
    final static String PAGE_PARAM = "page";
    final static String DEFAULT_PAGE = "1";

    public static URL buildUrl(String order, String apiKey) {
        String sortPath;
        if(order.equals("popular")){
            sortPath = POPULAR_PATH;
        } else {
            sortPath = TOP_RATED_PATH;
        }
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(MOVIE_PATH)
                .appendPath(sortPath)
                .appendQueryParameter(API_KEY_PARAM, apiKey)
                .appendQueryParameter(LANGUAGE_PARAM, DEFAULT_LANGUAGE)
                .appendQueryParameter(PAGE_PARAM, DEFAULT_PAGE)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    public static String buildImageUrl(String poster){
        StringBuilder builtUri = new StringBuilder();
        builtUri.append(IMAGE_BASE_URL).append(IMAGE_SIZE_PATH).append(poster);
        return builtUri.toString();
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}