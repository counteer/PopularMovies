package com.zflabs.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.zflabs.popularmovies.util.MovieDBJsonUtils;
import com.zflabs.popularmovies.util.NetworkUtils;

import java.net.URL;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<MovieData[]>,
        MovieDBAdapter.MovieAdapterClickHandler, SharedPreferences.OnSharedPreferenceChangeListener,
        Preference.OnPreferenceChangeListener{

    private static final int MOVIE_LIST_LOADER_ID = 213;

    private MovieDBAdapter movieDBAdapter;

    private RecyclerView recyclerView;

    private TextView mErrorMessageDisplay;

    private ProgressBar mLoadingIndicator;

    private static String API_KEY;

    private static boolean PREFERENCES_HAVE_BEEN_UPDATED = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        API_KEY = BuildConfig.ApiKey;
        int loaderId = MOVIE_LIST_LOADER_ID;
        recyclerView = (RecyclerView) findViewById(R.id.rvMovies);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        movieDBAdapter = new MovieDBAdapter(this);
        recyclerView.setAdapter(movieDBAdapter);
        LoaderManager.LoaderCallbacks<MovieData[]> callback = MainActivity.this;
        Bundle bundleForLoader = null;
        getSupportLoaderManager().initLoader(loaderId, bundleForLoader, callback);
        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);

    }
    @Override
    protected void onStart() {
        super.onStart();
        if (PREFERENCES_HAVE_BEEN_UPDATED) {
            getSupportLoaderManager().restartLoader(MOVIE_LIST_LOADER_ID, null, this);
            PREFERENCES_HAVE_BEEN_UPDATED = false;
        }
    }
    @Override
    public Loader<MovieData[]> onCreateLoader(int id, final Bundle loaderArgs) {

        return new AsyncTaskLoader<MovieData[]>(this) {

            MovieData[] movieData = null;

            @Override
            protected void onStartLoading() {
                if (movieData != null) {
                    deliverResult(movieData);
                } else {
                    mLoadingIndicator.setVisibility(View.VISIBLE);
                    forceLoad();
                }
            }

            @Override
            public MovieData[] loadInBackground() {
                String orderCriteria;
                String order = getOrder();
                String orderDebug = getString(R.string.pref_sort_order_popular_value);
                if(order.equals(orderDebug)){
                    orderCriteria = "popular";
                } else {
                    orderCriteria = "toprated";
                }
                URL movieRequestURL = NetworkUtils.buildUrl(orderCriteria, API_KEY);

                try {
                    String jsonMovieResponse = NetworkUtils
                            .getResponseFromHttpUrl(movieRequestURL);

                    MovieData[] simpleJsonMovieData = MovieDBJsonUtils
                            .getSimpleMovieStringsFromJson(MainActivity.this, jsonMovieResponse);

                    return simpleJsonMovieData;
                } catch (Exception e) {
                        e.printStackTrace();
                    return null;
                }
            }

            public void deliverResult(MovieData[] data) {
                movieData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<MovieData[]> loader, MovieData[] data) {
        movieDBAdapter.setMovieData(data);
        recyclerView.setVisibility(View.VISIBLE);
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        if (null == data) {
            showErrorMessage();
        } else {
            showResult();
        }
    }

    public void onClick(View textView){
        if(textView.getId()==mErrorMessageDisplay.getId()) {
            getSupportLoaderManager().restartLoader(MOVIE_LIST_LOADER_ID, null, this);
            Toast.makeText(this, R.string.reloading_message, Toast.LENGTH_SHORT).show();
        }
    }

    private void showErrorMessage() {
        recyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    private void showResult() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoaderReset(Loader<MovieData[]> loader) {

    }

    //TODO remove to class
    private String getOrder(){
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(this);
        String keyforOrder = getString(R.string.pref_sort_order_key);
        String defaultOrder = getString(R.string.pref_sort_order_default);
        return prefs.getString(keyforOrder, defaultOrder);
    }


    @Override
    public void onClick(MovieData movieData) {
        String dataToSend = movieData.toJSON();
        Context context = this;
        Class destinationClass = DetailActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        intentToStartDetailActivity.putExtra(Intent.EXTRA_TEXT, dataToSend);
        startActivity(intentToStartDetailActivity);
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int actualMenu = item.getItemId();
        if (actualMenu == R.id.action_settings) {
            Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(startSettingsActivity);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        /* Unregister MainActivity as an OnPreferenceChangedListener to avoid any memory leaks. */
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals(getString(R.string.pref_sort_order_key))){
            String orderValue = sharedPreferences.getString(key, "");
            PREFERENCES_HAVE_BEEN_UPDATED = true;
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        return false;
    }

}
