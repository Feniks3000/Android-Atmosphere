package ru.geekbrains.atmosphere.database;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

import ru.geekbrains.atmosphere.ExtraConstants;
import ru.geekbrains.atmosphere.receivers.ActionConstants;
import ru.geekbrains.atmosphere.singletone.MyApp;

public class DataBaseService extends IntentService implements ExtraConstants, ActionConstants {
    private static final String CLASS = DataBaseService.class.getSimpleName();
    private static final boolean LOGGING = false;
    private final DAO dao = MyApp.getDAO();

    public DataBaseService() {
        super("DataBaseService");
    }

    public static void startDataBaseService(Context context) {
        Intent intent = new Intent(context, DataBaseService.class);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        new Thread(() -> {
            if (LOGGING) {
                Log.d(CLASS, "start Thread");
            }
            ArrayList<History> histories = new ArrayList<>(Arrays.asList(dao.selectAll()));
            if (LOGGING) {
                Log.d(CLASS, "ArrayList with history -> " + histories);
            }
            sendBroadcast(histories);
        }).start();
    }

    private void sendBroadcast(ArrayList<History> histories) {
        Intent broadcastIntent = new Intent(BROADCAST_ACTION_HISTORY);
        broadcastIntent.putParcelableArrayListExtra(HISTORY, histories);
        sendBroadcast(broadcastIntent);
    }

}