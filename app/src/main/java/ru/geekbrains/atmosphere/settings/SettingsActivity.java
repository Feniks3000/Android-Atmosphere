package ru.geekbrains.atmosphere.settings;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import ru.geekbrains.atmosphere.ExtraConstants;
import ru.geekbrains.atmosphere.R;

public class SettingsActivity extends AppCompatActivity implements ExtraConstants {

    private static final String CLASS = SettingsActivity.class.getSimpleName();
    private static final boolean LOGGING = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
    }
}