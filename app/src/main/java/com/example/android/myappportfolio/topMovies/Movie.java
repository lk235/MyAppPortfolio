package com.example.android.myappportfolio.topMovies;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Time;

/**
 * Created by lk235 on 2017/3/14.
 */

public class Movie implements Parcelable {


    private int id;





    private String categroy;
    private String imageUrl;

    private String title;

    private String release_date;

    private String vote;
    private String overview;
    private String runtime;


    private String trailer;

    private String colledted;

    public Movie(){

    }

    public String getCategroy() {
        return categroy;
    }

    public void setCategroy(String categroy) {
        this.categroy = categroy;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getVote() {
        return vote;
    }

    public void setVote(String vote) {
        this.vote = vote;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public String getTrailer() {
        return trailer;
    }

    public void setTrailer(String trailer) {
        this.trailer = trailer;
    }

    public String getColledted() {
        return colledted;
    }

    public void setColledted(String colledted) {
        this.colledted = colledted;
    }


    public void writeToParcel(Parcel out, int flags){
        out.writeInt(id);
        out.writeString(imageUrl);
        out.writeString(title);
        out.writeString(release_date);
        out.writeString(vote);
        out.writeString(overview);

    }

    public int describeContents(){
       return 0;
    }

    public static final Parcelable.Creator<Movie> CREATOR =
            new Parcelable.Creator<Movie>(){
                public Movie createFromParcel(Parcel in){
                    return new Movie(in);
                }

                public Movie[] newArray(int size){
                    return new Movie[size];
                }
            };

            public Movie(Parcel in){
                this.id = in.readInt();
                this.imageUrl = in.readString();
                this.title = in.readString();
                this.release_date = in.readString();
                this.vote = in.readString();
                this.overview = in.readString();

            }






}
