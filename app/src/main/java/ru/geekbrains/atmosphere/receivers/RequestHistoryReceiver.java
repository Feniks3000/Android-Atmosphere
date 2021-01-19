package ru.geekbrains.atmosphere.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;

import ru.geekbrains.atmosphere.ExtraConstants;
import ru.geekbrains.atmosphere.database.History;

public class RequestHistoryReceiver extends BroadcastReceiver implements ExtraConstants {
    private static final String CLASS = RequestHistoryReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        final ArrayList<History> result = intent.getParcelableArrayListExtra(HISTORY);
        Log.i(CLASS, "Data on old weather queries: " + result);
    }
}
