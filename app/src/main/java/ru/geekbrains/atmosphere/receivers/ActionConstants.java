package ru.geekbrains.atmosphere.receivers;

public interface ActionConstants {
    String BROADCAST_ACTION_WEATHER = "ru.geekbrains.atmosphere.data_request.weather";
    String BROADCAST_ACTION_HISTORY = "ru.geekbrains.atmosphere.database_request.history";
    String BROADCAST_ACTION_BATTERY_LOW = "android.intent.action.BATTERY_LOW";
    String BROADCAST_ACTION_CONNECTIVITY_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE";

}
