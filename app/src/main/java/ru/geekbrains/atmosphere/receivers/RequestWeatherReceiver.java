package ru.geekbrains.atmosphere.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;

import ru.geekbrains.atmosphere.ExtraConstants;
import ru.geekbrains.atmosphere.city_weather.CityWeather;
import ru.geekbrains.atmosphere.database.DataBaseService;
import ru.geekbrains.atmosphere.database.History;
import ru.geekbrains.atmosphere.singletone.MyApp;

public class RequestWeatherReceiver extends BroadcastReceiver implements ExtraConstants {
    private static final String CLASS = RequestWeatherReceiver.class.getSimpleName();
    private static final boolean LOGGING = false;
    private OnUpdateWeatherListener onUpdateWeatherListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (context instanceof OnUpdateWeatherListener) {
            onUpdateWeatherListener = (OnUpdateWeatherListener) context;
        } else {
            return;
        }
        if (intent == null) {
            return;
        }

        final ArrayList<CityWeather> result = intent.getParcelableArrayListExtra(WEATHER_DATA);
        for (CityWeather cityWeather : result) {
            new Thread(() -> {
                if (LOGGING) {
                    Log.d(CLASS, "start Thread insert");
                }
                MyApp.getDAO().insert(new History(new Date().getTime(), cityWeather.getTemperature(), cityWeather.getCity()));
            }).start();
        }
        onUpdateWeatherListener.onUpdateWeather(result);
        DataBaseService.startDataBaseService(MyApp.getInstance().getApplicationContext());
    }

    public interface OnUpdateWeatherListener {
        void onUpdateWeather(ArrayList<CityWeather> data);
    }
}
