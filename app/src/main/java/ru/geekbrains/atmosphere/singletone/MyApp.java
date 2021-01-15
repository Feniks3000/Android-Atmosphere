package ru.geekbrains.atmosphere.singletone;

import android.app.Application;

import androidx.room.Room;

import ru.geekbrains.atmosphere.data_request.ApiHolder;
import ru.geekbrains.atmosphere.data_request.OpenWeather;
import ru.geekbrains.atmosphere.database.DAO;
import ru.geekbrains.atmosphere.database.DataBase;

public class MyApp extends Application {
    private static MyApp instance;
    private static ApiHolder apiHolder;
    private static DataBase dataBase;
    private final Storage storage = new Storage();

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        apiHolder = new ApiHolder();
        dataBase = Room.databaseBuilder(getApplicationContext(), DataBase.class, "database").build();
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

    public static DAO getDAO() {
        return dataBase.getDAO();
    }
}
