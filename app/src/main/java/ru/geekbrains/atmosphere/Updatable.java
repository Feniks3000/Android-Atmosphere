package ru.geekbrains.atmosphere;

import ru.geekbrains.atmosphere.settings.Cities;
import ru.geekbrains.atmosphere.settings.Settings;

public interface Updatable {
    void updateSettingsAndCities(Settings settings, Cities cities);

    void updateActiveCity(String activeCity);
}
