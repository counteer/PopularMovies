package com.zflabs.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by zoli on 2018.04.16..
 */

public class CachedMovieContentProvider extends ContentProvider {

    private CachedMovieDbHelper dbHelper;

    public static final int MOVIES = 100;

    public static UriMatcher buildUriMatcher(){
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI("com.zflabs.popularmovies", null, MOVIES);
        return uriMatcher;
    }
    private static final UriMatcher sUriMatcher = buildUriMatcher();


    @Override
    public boolean onCreate() {
        Context context = getContext();
        dbHelper = new CachedMovieDbHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);
        Uri returnUri;
        Cursor result;
        switch (match) {
            case MOVIES:
                result = db.query(CachedMovieContract.CachedMovieEntry.TABLE_NAME,
                        CachedMovieContract.CachedMovieEntry.COLUMN_ARRAY,
                        s,
                        null,
                        null,
                        null,
                        CachedMovieContract.CachedMovieEntry.COLUMN_MOVIE_TITLE
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        return result;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case MOVIES:
                long id = db.insert(CachedMovieContract.CachedMovieEntry.TABLE_NAME, null, contentValues);
                if ( id > 0 ) {
                    returnUri = ContentUris.withAppendedId(CachedMovieContract.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        db.close();
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int deleted;
        switch (match) {
            case MOVIES:
                deleted = db.delete(CachedMovieContract.CachedMovieEntry.TABLE_NAME, s, null);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        return deleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
