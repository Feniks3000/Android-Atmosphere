package ru.geekbrains.atmosphere.city_weather;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.net.ssl.HttpsURLConnection;

import ru.geekbrains.atmosphere.BuildConfig;
import ru.geekbrains.atmosphere.R;
import ru.geekbrains.atmosphere.cities.Cities;
import ru.geekbrains.atmosphere.request.WeatherRequest;

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
        int[] images = getImageArray();
        Random random = new Random();

        new Thread(() -> {
            for (int i = 0; i < cities.length; i++) {
                try {
                    final URL url = new URL(String.format(resources.getString(R.string.WEATHER_URL), cities[i]) + "&appid=" + BuildConfig.WEATHER_API_KEY);

                    HttpsURLConnection httpsURLConnection = null;

                    try {
                        httpsURLConnection = (HttpsURLConnection) url.openConnection();
                        httpsURLConnection.setRequestMethod("GET");
                        httpsURLConnection.setReadTimeout(10000);

                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpsURLConnection.getInputStream()));
                        StringBuilder stringWithJson = new StringBuilder();
                        String line;
                        while ((line = bufferedReader.readLine()) != null) {
                            stringWithJson.append(line).append("\n");
                        }

                        // Начиная с API 24
                        //String stringWithJson = bufferedReader.lines().collect(Collectors.joining("\n"));

                        final WeatherRequest weatherRequest = (new Gson()).fromJson(stringWithJson.toString(), WeatherRequest.class);

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

                        CityWeather cityWeather = new CityWeather(weatherRequest.getName(), (int) (weatherRequest.getMain().getTemp() - 273.15), images[random.nextInt(3)], next4Hours, next5Days);
                        data.add(cityWeather);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (httpsURLConnection != null) {
                            httpsURLConnection.disconnect();
                        }
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        return this;
    }

    public CityWeatherSource rebuild(Cities cities) {
        data = new ArrayList<>();
        this.cities = cities.getCitiesArray();
        build();
        return this;
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
