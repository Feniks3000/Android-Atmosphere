package ru.geekbrains.atmosphere.city_weather;

import android.content.res.Resources;

import ru.geekbrains.atmosphere.cities.Cities;

public class CityWeatherSourceBuilder {
    private Resources resources;
    private Cities cities;

    public CityWeatherSourceBuilder setResources(Resources resources, Cities cities) {
        this.resources = resources;
        this.cities = cities;
        return this;
    }

    public CityWeatherSource build() {
        CityWeatherSource cityWeatherSource = new CityWeatherSource(resources, cities);
        cityWeatherSource.build();
        return cityWeatherSource;
    }
}