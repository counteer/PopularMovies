package com.zflabs.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class MovieData {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;
    private String title;
    private String releaseDate;
    private String poster;
    private double voteAverage;
    private String synopsis;
    private String video;
    private String review;

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public int getOuterId() {
        return outerId;
    }

    public void setOuterId(int outerId) {
        this.outerId = outerId;
    }

    private int outerId;

    public MovieData(int outerId, String title, String releaseDate, String poster, double voteAverage, String synopsis) {
        this.outerId = outerId;
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

    public String toJSON() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("_id", id);
            jsonObject.put("title", title);
            jsonObject.put("release_date", releaseDate);
            jsonObject.put("vote_average", String.valueOf(voteAverage));
            jsonObject.put("overview", synopsis);
            jsonObject.put("poster_path", poster);
            jsonObject.put("video", video);
            jsonObject.put("review", review);
            jsonObject.put("id", outerId);
            return jsonObject.toString();
        } catch (JSONException e) {
            return "";
        }
    }
}
