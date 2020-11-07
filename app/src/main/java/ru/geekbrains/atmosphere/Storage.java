package ru.geekbrains.atmosphere;

import ru.geekbrains.atmosphere.settings.Settings;

public class Storage {
    private int counter = 0;

    public synchronized int getCounter() {
        return counter;
    }

    public synchronized int incCounter() {
        return ++counter;
    }
}
