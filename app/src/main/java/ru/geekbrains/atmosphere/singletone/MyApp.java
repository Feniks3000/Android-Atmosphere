package ru.geekbrains.atmosphere.singletone;

import android.app.Application;

import ru.geekbrains.atmosphere.data_request.ApiHolder;
import ru.geekbrains.atmosphere.data_request.OpenWeather;

public class MyApp extends Application {
    private static MyApp instance;
    private static ApiHolder apiHolder;
    private final Storage storage = new Storage();

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        apiHolder = new ApiHolder();
    }

    public Storage getStorage() {
        return storage;
    }

    public static MyApp getInstance() {
        return instance;
    }

    public static OpenWeather getOpenWeatherApi() {
        return apiHolder.getOpenWeather();
    }
}
