package andrefch.udacity.com.br.popularmovies.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import andrefch.udacity.com.br.popularmovies.BuildConfig;
import andrefch.udacity.com.br.popularmovies.utilities.DateUtils;

/**
 * Author: andrech
 * Date: 19/12/17
 */

public class Movie implements Parcelable {

    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

    private long mId;
    private String mTitle;
    private String mOriginalTitle;
    private String mOverview;
    private long mVoteCount;
    private float mVoteAverage;
    private float mPopularity;
    private String mPosterPath;
    private Date mReleaseDate;

    public Movie() {
        super();
        mId = 0L;
        mVoteCount = 0L;
        mVoteAverage = 0f;
        mPopularity = 0f;
    }

    public Movie(JSONObject json) throws JSONException {
        this();
        if (json != null) {
            mId = json.getLong("id");
            mTitle = json.getString("title");
            mOriginalTitle = json.getString("original_title");
            mOverview = json.getString("overview");
            mVoteCount = json.getLong("vote_count");
            mVoteAverage = (float) json.getDouble("vote_average");
            mPopularity = (float) json.getDouble("popularity");
            mPosterPath = json.getString("poster_path");
            mReleaseDate = DateUtils.stringToDate(DEFAULT_DATE_FORMAT,
                    json.getString("release_date"));
        }
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getOriginalTitle() {
        return mOriginalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        mOriginalTitle = originalTitle;
    }

    public String getOverview() {
        return mOverview;
    }

    public void setOverview(String overview) {
        mOverview = overview;
    }

    public long getVoteCount() {
        return mVoteCount;
    }

    public void setVoteCount(long voteCount) {
        mVoteCount = voteCount;
    }

    public float getVoteAverage() {
        return mVoteAverage;
    }

    public void setVoteAverage(float voteAverage) {
        mVoteAverage = voteAverage;
    }

    public float getPopularity() {
        return mPopularity;
    }

    public void setPopularity(float popularity) {
        mPopularity = popularity;
    }

    public String getPosterPath() {
        return mPosterPath;
    }

    public void setPosterPath(String posterPath) {
        mPosterPath = posterPath;
    }

    public Date getReleaseDate() {
        return mReleaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        mReleaseDate = releaseDate;
    }

    public String getCompletePosterURL() {
        return Uri.parse(BuildConfig.SERVER_IMAGE_BASE_URL)
                .buildUpon()
                .appendEncodedPath(mPosterPath)
                .build()
                .toString();
    }

    //region Parcelable Methods
    private Movie(Parcel parcel) {
        this();
        if (parcel != null) {
            mId = parcel.readLong();
            mTitle = parcel.readString();
            mOriginalTitle = parcel.readString();
            mOverview = parcel.readString();
            mVoteCount = parcel.readLong();
            mVoteAverage = parcel.readFloat();
            mPopularity = parcel.readFloat();
            mPosterPath = parcel.readString();
            long releaseDate = parcel.readLong();
            if (releaseDate >= 0) {
                mReleaseDate = new Date(releaseDate);
            }
        }
    }

    @Override
    public int describeContents() {
        return Parcelable.CONTENTS_FILE_DESCRIPTOR;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeLong(mId);
        parcel.writeString(mTitle);
        parcel.writeString(mOriginalTitle);
        parcel.writeString(mOverview);
        parcel.writeLong(mVoteCount);
        parcel.writeFloat(mVoteAverage);
        parcel.writeFloat(mPopularity);
        parcel.writeString(mPosterPath);
        parcel.writeLong(mReleaseDate != null ? mReleaseDate.getTime() : -1);
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
    //endregion
}
