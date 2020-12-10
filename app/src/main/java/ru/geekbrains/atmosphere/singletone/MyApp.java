package ru.geekbrains.atmosphere.singletone;

import android.app.Application;

public class MyApp extends Application {
    private static MyApp instance;
    private final Storage storage = new Storage();

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public Storage getStorage() {
        return storage;
    }

    public static MyApp getInstance() {
        return instance;
    }
}
