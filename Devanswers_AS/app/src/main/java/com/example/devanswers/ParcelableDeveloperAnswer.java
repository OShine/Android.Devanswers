package com.example.devanswers;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by O'Shine on 10.04.2015.
 */
public class ParcelableDeveloperAnswer implements Parcelable {

    private String suffix;
    private String text;

    public ParcelableDeveloperAnswer(Parcel source) {
        text = source.readString();
        suffix = source.readString();
    }

    public ParcelableDeveloperAnswer(String text, String suffix) {
        this.text = text;
        this.suffix = suffix;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(text);
        dest.writeString(suffix);
    }

    public static final Parcelable.Creator<ParcelableDeveloperAnswer> CREATOR = new Creator<ParcelableDeveloperAnswer>() {

        @Override
        public ParcelableDeveloperAnswer createFromParcel(Parcel source) {
            return new ParcelableDeveloperAnswer(source);
        }

        @Override
        public ParcelableDeveloperAnswer[] newArray(int size) {
            return new ParcelableDeveloperAnswer[0];
        }
    };

}
