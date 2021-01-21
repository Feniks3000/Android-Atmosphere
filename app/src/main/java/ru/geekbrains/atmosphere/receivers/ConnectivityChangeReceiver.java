package ru.geekbrains.atmosphere.receivers;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import ru.geekbrains.atmosphere.ExtraConstants;
import ru.geekbrains.atmosphere.R;

public class ConnectivityChangeReceiver extends BroadcastReceiver implements ExtraConstants {
    private static final String CLASS = ConnectivityChangeReceiver.class.getSimpleName();
    private static final int NOTIFY_ID = 101;
    private static String CHANNEL_ID = "Cat channel";

    @Override
    public void onReceive(Context context, Intent intent) {
        String title = context.getResources().getString(R.string.app_name);
        String text = context.getResources().getString(R.string.connectivity_change);

        Log.i(CLASS, String.format("Notification => %s - %s", title, text));

        // TODO: Notification в API 23 ругается на канал
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFY_ID, builder.build());

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.isActiveNetworkMetered()) {
            Toast.makeText(context, text, Toast.LENGTH_LONG).show();
        }
    }
}
