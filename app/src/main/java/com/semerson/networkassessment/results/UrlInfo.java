package com.semerson.networkassessment.results;

import android.os.Parcel;
import android.os.Parcelable;

public class UrlInfo implements Parcelable{
    private String url;
    private String description;

    public UrlInfo(){
        url = "";
        description = "";
    }

    public void setUrlInfo(String url, String description){
        this.url = url;
        this.description = description;
    }

    public String getUrl(){
        return url;
    }

    public String getDescription(){
        return description;
    }

    public UrlInfo(Parcel in){
        url = in.readString();
        description = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url);
        dest.writeString(description);
    }

    public static final Parcelable.Creator<UrlInfo> CREATOR= new Parcelable.Creator<UrlInfo>() {

        @Override
        public UrlInfo createFromParcel(Parcel source) {
            return new UrlInfo(source);  //using parcelable constructor
        }

        @Override
        public UrlInfo[] newArray(int size) {
            return new UrlInfo[size];
        }
    };
}
