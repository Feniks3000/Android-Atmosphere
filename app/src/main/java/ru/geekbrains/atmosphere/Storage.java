package ru.geekbrains.atmosphere;

import ru.geekbrains.atmosphere.settings.Cities;
import ru.geekbrains.atmosphere.settings.Settings;

public class Storage {
    private int counter = 0;
    private Settings settings;
    private Cities cities;

    public synchronized int getCounter() {
        return counter;
    }

    public synchronized int incCounter() {
        return ++counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public Cities getCities() {
        return cities;
    }

    public void setCities(Cities cities) {
        this.cities = cities;
    }
}
