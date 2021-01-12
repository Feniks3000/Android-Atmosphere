package ru.geekbrains.atmosphere.city_weather;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import ru.geekbrains.atmosphere.R;
import ru.geekbrains.atmosphere.cities.Cities;
import ru.geekbrains.atmosphere.data_request.DataRequestService;
import ru.geekbrains.atmosphere.singletone.MyApp;

public class CityWeatherSource implements Parcelable {

    private List<CityWeather> data;
    private Resources resources;
    private String[] cities;

    public CityWeatherSource(Resources resources, Cities cities) {
        data = new ArrayList<>();
        this.resources = resources;
        this.cities = cities.getCitiesArray();
    }

    protected CityWeatherSource(Parcel in) {
        data = in.createTypedArrayList(CityWeather.CREATOR);
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
        DataRequestService.startDataRequestService(MyApp.getInstance().getApplicationContext(), cities, resources.getString(R.string.WEATHER_URL));
        return this;
    }

    public CityWeatherSource rebuild(Cities cities) {
        data = new ArrayList<>();
        this.cities = cities.getCitiesArray();
        build();
        return this;
    }

    public void update(List<CityWeather> data) {
        this.data = data;
    }

    public void addCity(String city) {
        CityWeather cityWeather = new CityWeather(city, 0, null, null, null);
        data.add(cityWeather);
    }

    void removeCity(int position) {
        data.remove(position);
    }

    void clearCities() {
        data.clear();
    }

    public CityWeather getCityWeather(int position) {
        return data.get(position);
    }

    public int size() {
        return data.size();
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
                "dataSource=" + data +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(data);
        dest.writeStringArray(cities);
    }

}
