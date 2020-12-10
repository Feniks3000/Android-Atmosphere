package ru.geekbrains.atmosphere.settings;

import android.os.Parcel;
import android.os.Parcelable;

public class Settings implements Parcelable {
    public static final int DEFAULT_THEME = 2;
    public static final int DEFAULT_ALL_DETAIL = 1;

    private int theme;
    private int allDetail;

    public int getTheme() {
        return theme;
    }

    public void setTheme(int theme) {
        this.theme = theme;
    }

    public boolean isAllDetail() {
        return allDetail == 1;
    }

    public void setAllDetail(boolean allDetail) {
        this.allDetail = (allDetail) ? 1 : 0;
    }

    @Override
    public String toString() {
        return "Settings{" +
                "theme=" + theme +
                ", allDetail=" + allDetail +
                '}';
    }

    public Settings(int theme, int allDetail) {
        this.theme = theme;
        this.allDetail = allDetail;
    }

    protected Settings(Parcel in) {
        theme = in.readInt();
        allDetail = in.readInt();
    }

    public static final Creator<Settings> CREATOR = new Creator<Settings>() {
        @Override
        public Settings createFromParcel(Parcel in) {
            return new Settings(in);
        }

        @Override
        public Settings[] newArray(int size) {
            return new Settings[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(theme);
        dest.writeInt(allDetail);
    }
}
