package ru.geekbrains.atmosphere;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import ru.geekbrains.atmosphere.settings.Settings;
import ru.geekbrains.atmosphere.settings.SettingsActivity;

public class CityWeatherActivity extends AppCompatActivity implements ExtraConstants {

    private static final String CLASS = CityWeatherActivity.class.getSimpleName();
    private static final int SETTINGS_REQUEST_CODE = 1;
    private static final boolean LOGGING = false;

    private Button buttonSettings;
    private Button buttonAboutCity;
    private Spinner spinnerCity;

    private Settings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.city_weather);

        Toast.makeText(getApplicationContext(), String.valueOf(MyApp.getInstance().getStorage().incCounter()), Toast.LENGTH_SHORT).show();

        spinnerCity = findViewById(R.id.spinnerCity);
        initCityWeather();

        settings = initSettings();

        buttonSettings = findViewById(R.id.settingsButton);
        buttonSettings.setOnClickListener(view -> {
            Intent intent = new Intent(CityWeatherActivity.this, SettingsActivity.class);
            intent.putExtra(SETTINGS, settings);
            startActivityForResult(intent, SETTINGS_REQUEST_CODE);
        });

        buttonAboutCity = findViewById(R.id.webInfoButton);
        buttonAboutCity.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
            intent.putExtra(SearchManager.QUERY, String.format(getResources().getString(R.string.weatherIn), spinnerCity.getSelectedItem().toString()));
            startActivity(intent);
        });

        if (LOGGING) {
            Log.i(CLASS, "onCreate");
            Toast.makeText(getApplicationContext(), "onCreate", Toast.LENGTH_SHORT).show();
        }
    }

    private Settings initSettings() {
        Settings settings = new Settings(Settings.DEFAULT_THEME, Settings.DEFAULT_ALL_DETAIL);
        if (LOGGING) {
            Log.i(CLASS, "initSettings " + settings);
            Toast.makeText(getApplicationContext(), "initSettings " + settings, Toast.LENGTH_SHORT).show();
        }
        return settings;
    }

    public void initCityWeather() {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.cities));
        spinnerCity.setAdapter(arrayAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (LOGGING) {
            Log.i(CLASS, "onStart");
            Toast.makeText(getApplicationContext(), "onStart", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != SETTINGS_REQUEST_CODE) {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }

        if (resultCode == RESULT_OK) {
            settings = data.getParcelableExtra(SETTINGS);
            if (LOGGING) {
                Log.i(CLASS, "onActivityResult - received new settings " + settings);
                Toast.makeText(getApplicationContext(), "onActivityResult - received new settings " + settings, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (LOGGING) {
            Log.i(CLASS, "onRestoreInstanceState");
            Toast.makeText(getApplicationContext(), "onRestoreInstanceState", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (LOGGING) {
            Log.i(CLASS, "onResume");
            Toast.makeText(getApplicationContext(), "onResume", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (LOGGING) {
            Log.i(CLASS, "onPause");
            Toast.makeText(getApplicationContext(), "onPause", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (LOGGING) {
            Log.i(CLASS, "onSaveInstanceState");
            Toast.makeText(getApplicationContext(), "onSaveInstanceState", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (LOGGING) {
            Log.i(CLASS, "onRestart");
            Toast.makeText(getApplicationContext(), "onRestart", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (LOGGING) {
            Log.i(CLASS, "onStop");
            Toast.makeText(getApplicationContext(), "onStop", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (LOGGING) {
            Log.i(CLASS, "onDestroy");
            Toast.makeText(getApplicationContext(), "onDestroy", Toast.LENGTH_SHORT).show();
        }
    }
}