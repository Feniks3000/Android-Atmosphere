package ru.geekbrains.atmosphere.city_weather;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        DataRequestService.startDataRequestService(MyApp.getInstance().getApplicationContext(), cities);
        return this;
    }

    public CityWeatherSource rebuild(Cities cities) {
        data = new ArrayList<>();
        this.cities = cities.getCitiesArray();
        build();
        return this;
    }

    public void update(List<CityWeather> data) {
        Map<String, Integer> images = getImages();

        for (CityWeather cityWeather : data) {
            if (cityWeather.getPictureName() != null && images.containsKey(cityWeather.getPictureName())) {
                cityWeather.setPicture(images.get(cityWeather.getPictureName()));
            }
        }
        this.data = data;
    }

    public void addCity(String city) {
        CityWeather cityWeather = new CityWeather(city, 0, 0, null, null, null);
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

    @NotNull
    private Map<String, Integer> getImages() {
        Map<String, Integer> images = new HashMap<>();
        images.put("01d", R.drawable.icon_01d);
        images.put("01n", R.drawable.icon_01n);
        images.put("02d", R.drawable.icon_02d);
        images.put("02n", R.drawable.icon_02n);
        images.put("03d", R.drawable.icon_03d);
        images.put("03n", R.drawable.icon_03n);
        images.put("04d", R.drawable.icon_04d);
        images.put("04n", R.drawable.icon_04n);
        images.put("09d", R.drawable.icon_09d);
        images.put("09n", R.drawable.icon_09n);
        images.put("10d", R.drawable.icon_10d);
        images.put("10n", R.drawable.icon_10n);
        images.put("11d", R.drawable.icon_11d);
        images.put("11n", R.drawable.icon_11n);
        images.put("13d", R.drawable.icon_13d);
        images.put("13n", R.drawable.icon_13n);
        images.put("50d", R.drawable.icon_50d);
        images.put("50n", R.drawable.icon_50n);
        return images;
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
