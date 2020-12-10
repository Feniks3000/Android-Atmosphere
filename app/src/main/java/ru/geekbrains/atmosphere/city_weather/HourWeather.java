package ru.geekbrains.atmosphere.city_weather;

import android.os.Parcel;
import android.os.Parcelable;

public class HourWeather implements Parcelable {
    private String hour;
    private Integer temperature;

    public HourWeather(String hour, Integer temperature) {
        this.hour = hour;
        this.temperature = temperature;
    }

    protected HourWeather(Parcel in) {
        hour = in.readString();
        temperature = in.readInt();
    }

    public static final Creator<HourWeather> CREATOR = new Creator<HourWeather>() {
        @Override
        public HourWeather createFromParcel(Parcel in) {
            return new HourWeather(in);
        }

        @Override
        public HourWeather[] newArray(int size) {
            return new HourWeather[size];
        }
    };

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public Integer getTemperature() {
        return temperature;
    }

    public void setTemperature(Integer temperature) {
        this.temperature = temperature;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(hour);
        dest.writeInt(temperature);
    }
}
