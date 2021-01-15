package ru.geekbrains.atmosphere.database;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

import ru.geekbrains.atmosphere.ExtraConstants;
import ru.geekbrains.atmosphere.MainActivity;
import ru.geekbrains.atmosphere.singletone.MyApp;

public class DataBaseService extends IntentService implements ExtraConstants {

    public DataBaseService() {
        super("DataBaseService");
    }

    private final DAO dao = MyApp.getDAO();

    public static void startDataBaseService(Context context) {
        Intent intent = new Intent(context, DataBaseService.class);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        new Thread(() -> {
            Log.i("DataBaseService", "start Thread");
            Log.i("DataBaseService", "Array -> " + Arrays.toString(dao.selectAll()));
            ArrayList<History> histories = new ArrayList<>(Arrays.asList(dao.selectAll()));
            Log.i("DataBaseService", "ArrayList -> " + histories);
            sendBroadcast(histories);
        }).start();
    }

    private void sendBroadcast(ArrayList<History> histories) {
        Intent broadcastIntent = new Intent(MainActivity.BROADCAST_ACTION_HISTORY_FINISHED);
        broadcastIntent.putParcelableArrayListExtra(HISTORY, histories);
        sendBroadcast(broadcastIntent);
    }

}