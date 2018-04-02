package com.zflabs.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class MovieData {
    private String title;
    private String releaseDate;
    private String poster;
    private double voteAverage;
    private String synopsis;


    public MovieData(String title, String releaseDate, String poster, double voteAverage, String synopsis) {
        this.title = title;
        this.releaseDate = releaseDate;
        this.poster = poster;
        this.voteAverage = voteAverage;
        this.synopsis = synopsis;
    }


    public void setTitle(String title) {
        this.title = title;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getTitle() {
        return title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getPoster() {
        return poster;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public String toJSON(){
        JSONObject jsonObject= new JSONObject();
        try {
            jsonObject.put("title", title);
            jsonObject.put("release_date", releaseDate);
            jsonObject.put("vote_average", String.valueOf(voteAverage));
            jsonObject.put("overview", synopsis);
            jsonObject.put("poster_path", poster);
            return jsonObject.toString();
        } catch (JSONException e) {
            return "";
        }
    }
}
