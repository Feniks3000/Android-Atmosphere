package ru.geekbrains.atmosphere.data_request;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

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
import ru.geekbrains.atmosphere.ExtraConstants;
import ru.geekbrains.atmosphere.MainActivity;
import ru.geekbrains.atmosphere.city_weather.CityWeather;
import ru.geekbrains.atmosphere.city_weather.DayWeather;
import ru.geekbrains.atmosphere.city_weather.HourWeather;
import ru.geekbrains.atmosphere.request.WeatherParser;
import ru.geekbrains.atmosphere.request.WeatherRequest;

public class DataRequestService extends IntentService implements ExtraConstants {

    public DataRequestService() {
        super("DataRequestService");
    }

    public static void startDataRequestService(Context context, String[] cities, String weatherUrl) {
        Intent intent = new Intent(context, DataRequestService.class);
        intent.putExtra(CITIES, cities);
        intent.putExtra(WEATHER_URL, weatherUrl);
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
                Log.i("DataRequestService", "start Thread");
                for (int i = 0; i < cities.length; i++) {
                    try {
                        final URL url = new URL(String.format(weatherUrl, cities[i]) + "&appid=" + BuildConfig.WEATHER_API_KEY);

                        HttpsURLConnection httpsURLConnection = null;

                        try {
                            httpsURLConnection = (HttpsURLConnection) url.openConnection();
                            httpsURLConnection.setRequestMethod("GET");
                            httpsURLConnection.setReadTimeout(10000);

                            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpsURLConnection.getInputStream()));
                            final WeatherRequest weatherRequest = WeatherParser.parser(bufferedReader);

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

                            CityWeather cityWeather = new CityWeather(weatherRequest.getName(), (int) (weatherRequest.getMain().getTemp() - 273.15), 0 /*images[random.nextInt(3)]*/, next4Hours, next5Days);
                            Log.i("DataRequestService", "get CityWeather " + cityWeather);
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
                Log.i("DataRequestService", "all data " + data);
                sendBrodcast(data);
            }).start();
        }
    }

    private void sendBrodcast(ArrayList<CityWeather> data) {
        Intent broadcastIntent = new Intent(MainActivity.BROADCAST_ACTION_FINISHED);
        broadcastIntent.putParcelableArrayListExtra(WEATHER_DATA, data);
        sendBroadcast(broadcastIntent);
    }

}