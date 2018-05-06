package com.zflabs.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.zflabs.popularmovies.data.CachedMovieContract.CachedMovieEntry;

public class CachedMovieDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "cachedMovie.db";

    public static final int DATABASE_VERSION = 2;

    public CachedMovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String CREATE_TABLE = "CREATE TABLE "+
                CachedMovieEntry.TABLE_NAME + " (" +
                "_id" + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                CachedMovieEntry.COLUMN_MOVIE_TITLE + " TEXT NOT NULL, " +
                CachedMovieEntry.COLUMN_MOVIE_POSTER + " TEXT NOT NULL, " +
                CachedMovieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                CachedMovieEntry.COLUMN_VOTE_AVERAGE + " REAL NOT NULL, " +
                CachedMovieEntry.COLUMN_SYNOPSIS + " TEXT NOT NULL, " +
                CachedMovieEntry.COLUMN_OUTER_ID + " INT NOT NULL, " +
                CachedMovieEntry.COLUMN_TRAILER + " TEXT, " +
                CachedMovieEntry.COLUMN_REVIEW + " TEXT " +
                ");";
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CachedMovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
