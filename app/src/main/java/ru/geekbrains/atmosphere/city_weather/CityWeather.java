package ru.geekbrains.atmosphere.city_weather;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class CityWeather implements Parcelable {
    private String city;
    private Integer temperature;
    private Integer picture;
    private String pictureName;
    private List<HourWeather> next4HoursWeather;
    private List<DayWeather> next5DaysWeather;

    public CityWeather(String city, Integer temperature, Integer picture, String pictureName, List<HourWeather> next4HoursWeather, List<DayWeather> next5DaysWeather) {
        this.city = city;
        this.temperature = temperature;
        this.picture = picture;
        this.pictureName = pictureName;
        this.next4HoursWeather = next4HoursWeather;
        this.next5DaysWeather = next5DaysWeather;
    }

    protected CityWeather(Parcel in) {
        city = in.readString();
        temperature = in.readInt();
        picture = in.readInt();
        pictureName = in.readString();
        next4HoursWeather = in.createTypedArrayList(HourWeather.CREATOR);
        next5DaysWeather = in.createTypedArrayList(DayWeather.CREATOR);
    }

    public static final Creator<CityWeather> CREATOR = new Creator<CityWeather>() {
        @Override
        public CityWeather createFromParcel(Parcel in) {
            return new CityWeather(in);
        }

        @Override
        public CityWeather[] newArray(int size) {
            return new CityWeather[size];
        }
    };

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getTemperature() {
        return temperature;
    }

    public void setTemperature(Integer temperature) {
        this.temperature = temperature;
    }

    public Integer getPicture() {
        return picture;
    }

    public void setPicture(Integer picture) {
        this.picture = picture;
    }

    public String getPictureName() {
        return pictureName;
    }

    public void setPictureName(String pictureName) {
        this.pictureName = pictureName;
    }

    public List<HourWeather> getNext4HoursWeather() {
        return next4HoursWeather;
    }

    public void setNext4HoursWeather(List<HourWeather> next4HoursWeather) {
        this.next4HoursWeather = next4HoursWeather;
    }

    public List<DayWeather> getNext5DaysWeather() {
        return next5DaysWeather;
    }

    public void setNext5DaysWeather(List<DayWeather> next5DaysWeather) {
        this.next5DaysWeather = next5DaysWeather;
    }

    @Override
    public String toString() {
        return "CityWeather{" +
                "city='" + city + '\'' +
                ", temperature=" + temperature +
                ", picture=" + picture +
                ", pictureName='" + pictureName + '\'' +
                ", next4HoursWeather=" + next4HoursWeather +
                ", next5DaysWeather=" + next5DaysWeather +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(city);
        dest.writeInt(temperature);
        dest.writeInt(picture);
        dest.writeString(pictureName);
        dest.writeTypedList(next4HoursWeather);
        dest.writeTypedList(next5DaysWeather);
    }
}
