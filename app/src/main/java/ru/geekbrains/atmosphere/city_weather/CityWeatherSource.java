package ru.geekbrains.atmosphere.city_weather;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ru.geekbrains.atmosphere.R;
import ru.geekbrains.atmosphere.settings.Cities;

public class CityWeatherSource implements Parcelable {

    private List<CityWeather> dataSource;
    private Resources resources;
    private String[] cities;

    public CityWeatherSource(Resources resources, Cities cities) {
        dataSource = new ArrayList<>();
        this.resources = resources;
        this.cities = cities.getCitiesArray();
    }

    protected CityWeatherSource(Parcel in) {
        dataSource = in.createTypedArrayList(CityWeather.CREATOR);
        cities = in.createStringArray();
    }

    public static final Creator<CityWeatherSource> CREATOR = new Creator<CityWeatherSource>() {
        @Override
        public CityWeatherSource createFromParcel(Parcel in) {
            return new CityWeatherSource(in);
        }

        @Override
        public CityWeatherSource[] newArray(int size) {
            return new CityWeatherSource[size];
        }
    };

    public CityWeatherSource build() {
        int[] images = getImageArray();
        Random random = new Random();

        for (int i = 0; i < cities.length; i++) {
            Integer temperature = random.nextInt(30) - 10;
            List<HourWeather> next4Hours = new ArrayList<>();
            for (int j = 0; j < 4; j++) {
                next4Hours.add(new HourWeather(String.valueOf(10 + j), j * 10 + random.nextInt(10)));
            }
            List<DayWeather> next5Days = new ArrayList<>();
            for (int j = 0; j < 5; j++) {
                int morningTemperature = j * 10 + random.nextInt(10);
                int middayTemperature = morningTemperature + 5;
                int eveningTemperature = morningTemperature - 10;
                next5Days.add(new DayWeather(String.valueOf(10 + j), morningTemperature, middayTemperature, eveningTemperature));
            }
            CityWeather cityWeather = new CityWeather(cities[i], temperature, images[random.nextInt(3)], next4Hours, next5Days);
            dataSource.add(cityWeather);
        }
        return this;
    }

    public CityWeatherSource rebuild(Cities cities) {
        dataSource = new ArrayList<>();
        this.cities = cities.getCitiesArray();
        build();
        return this;
    }

    public void addCity(String city){
        CityWeather cityWeather = new CityWeather(city, 0, null, null, null);
        dataSource.add(cityWeather);
    }

    void removeCity(int position){
        dataSource.remove(position);
    }

    void clearCities(){
        dataSource.clear();
    }

    public CityWeather getCityWeather(int position) {
        return dataSource.get(position);
    }

    public int size() {
        return dataSource.size();
    }

    private int[] getImageArray() {
        TypedArray pictures = resources.obtainTypedArray(R.array.imagesWeather);
        int length = pictures.length();
        int[] answer = new int[length];
        for (int i = 0; i < length; i++) {
            answer[i] = pictures.getResourceId(i, 0);
        }
        return answer;
    }

    @Override
    public String toString() {
        return "CityWeatherSource{" +
                "dataSource=" + dataSource +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(dataSource);
        dest.writeStringArray(cities);
    }
}
