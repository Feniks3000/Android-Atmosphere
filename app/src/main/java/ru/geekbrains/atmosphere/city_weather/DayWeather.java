package ru.geekbrains.atmosphere.city_weather;

import android.os.Parcel;
import android.os.Parcelable;

public class DayWeather implements Parcelable {
    private String date;
    private Integer morningTemperature;
    private Integer middayTemperature;
    private Integer eveningTemperature;

    public DayWeather(String date, Integer morningTemperature, Integer middayTemperature, Integer eveningTemperature) {
        this.date = date;
        this.morningTemperature = morningTemperature;
        this.middayTemperature = middayTemperature;
        this.eveningTemperature = eveningTemperature;
    }

    protected DayWeather(Parcel in) {
        date = in.readString();
        morningTemperature = in.readInt();
        middayTemperature = in.readInt();
        eveningTemperature = in.readInt();
    }

    public static final Creator<DayWeather> CREATOR = new Creator<DayWeather>() {
        @Override
        public DayWeather createFromParcel(Parcel in) {
            return new DayWeather(in);
        }

        @Override
        public DayWeather[] newArray(int size) {
            return new DayWeather[size];
        }
    };

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getMorningTemperature() {
        return morningTemperature;
    }

    public void setMorningTemperature(Integer morningTemperature) {
        this.morningTemperature = morningTemperature;
    }

    public Integer getMiddayTemperature() {
        return middayTemperature;
    }

    public void setMiddayTemperature(Integer middayTemperature) {
        this.middayTemperature = middayTemperature;
    }

    public Integer getEveningTemperature() {
        return eveningTemperature;
    }

    public void setEveningTemperature(Integer eveningTemperature) {
        this.eveningTemperature = eveningTemperature;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(date);
        dest.writeInt(morningTemperature);
        dest.writeInt(middayTemperature);
        dest.writeInt(eveningTemperature);
    }
}
