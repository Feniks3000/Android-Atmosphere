package ru.geekbrains.atmosphere.data_request;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Response;
import ru.geekbrains.atmosphere.BuildConfig;
import ru.geekbrains.atmosphere.ExtraConstants;
import ru.geekbrains.atmosphere.R;
import ru.geekbrains.atmosphere.city_weather.CityWeather;
import ru.geekbrains.atmosphere.city_weather.DayWeather;
import ru.geekbrains.atmosphere.city_weather.HourWeather;
import ru.geekbrains.atmosphere.model.WeatherRequest;
import ru.geekbrains.atmosphere.receivers.ActionConstants;
import ru.geekbrains.atmosphere.singletone.MyApp;

public class DataRequestService extends IntentService implements ExtraConstants, ActionConstants {
    private static final String CLASS = DataRequestService.class.getSimpleName();
    private static final boolean LOGGING = false;

    public DataRequestService() {
        super("DataRequestService");
    }

    public static void startDataRequestService(Context context, String[] cities) {
        Intent intent = new Intent(context, DataRequestService.class);
        intent.putExtra(CITIES, cities);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            String[] cities = intent.getStringArrayExtra(CITIES);
            String weatherUrl = intent.getStringExtra(WEATHER_URL);

            ArrayList<CityWeather> data = new ArrayList<>();
            //int[] images = getImageArray();
            Random random = new Random();
            new Thread(() -> {
                if (LOGGING) {
                    Log.d(CLASS, "start Thread");
                }
                for (int i = 0; i < cities.length; i++) {
                    try {

                        // TODO: рассмотреть возможность отказа от службы в пользу возможностей Retrofit
                        Response<WeatherRequest> response = MyApp.getOpenWeatherApi().loadWeather(cities[i], getString(R.string.weather_temp_type), BuildConfig.WEATHER_API_KEY).execute();
                        if (response.body() != null) {
                            WeatherRequest weatherRequest = (WeatherRequest) response.body();

                            // TODO: найти погодный сервис с бесплатным возвратом погоды по часам и дням
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

                            CityWeather cityWeather = new CityWeather(
                                    weatherRequest.getName(),
                                    (int) (weatherRequest.getMain().getTemp()),
                                    0 /*images[random.nextInt(3)]*/,
                                    next4Hours,
                                    next5Days
                            );
                            if (LOGGING) {
                                Log.d(CLASS, "get CityWeather " + cityWeather);
                            }
                            data.add(cityWeather);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();

                    }
                }
                if (LOGGING) {
                    Log.d(CLASS, "all data " + data);
                }
                sendBrodcast(data);
            }).start();
        }
    }

    private void sendBrodcast(ArrayList<CityWeather> data) {
        Intent broadcastIntent = new Intent(BROADCAST_ACTION_WEATHER);
        broadcastIntent.putParcelableArrayListExtra(WEATHER_DATA, data);
        sendBroadcast(broadcastIntent);
    }

}