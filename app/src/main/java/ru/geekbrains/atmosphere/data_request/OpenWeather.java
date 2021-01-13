package ru.geekbrains.atmosphere.data_request;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.geekbrains.atmosphere.model.WeatherRequest;

public interface OpenWeather {
    @GET("data/2.5/weather")
    Call<WeatherRequest> loadWeather(@Query("q") String city, @Query("appid") String keyApi);
}
