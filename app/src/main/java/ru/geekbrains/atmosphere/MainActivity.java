package ru.geekbrains.atmosphere;

import android.app.SearchManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import ru.geekbrains.atmosphere.settings.Cities;
import ru.geekbrains.atmosphere.settings.Settings;
import ru.geekbrains.atmosphere.settings.SettingsActivity;

public class MainActivity extends AppCompatActivity implements ExtraConstants {

    private static final String CLASS = CityWeatherActivity.class.getSimpleName();
    private static final int SETTINGS_REQUEST_CODE = 1;
    private static final boolean LOGGING = false;

    private boolean landscapeOrientation;

    private Button buttonSettings;
    private Button buttonAboutCity;
    private Spinner spinnerCity;

    private Settings settings;
    private Cities cities;
    private MainActivity mainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = this;
        setContentView(R.layout.activity_main);
        landscapeOrientation = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;

        Toast.makeText(getApplicationContext(), String.valueOf(MyApp.getInstance().getStorage().incCounter()), Toast.LENGTH_SHORT).show();

        settings = initSettings();
        cities = initCities();

        spinnerCity = findViewById(R.id.spinnerCity);
        updateSettingsAndCities(settings, cities);
        spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CityWeatherButtonsFragment buttonsFragment = CityWeatherButtonsFragment.create(spinnerCity.getSelectedItem().toString(), settings, cities, landscapeOrientation, mainActivity);
                if (landscapeOrientation) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.infoFragment, buttonsFragment).commit();
                } else {
                    getSupportFragmentManager().beginTransaction().replace(R.id.buttonsFragment, buttonsFragment).commit();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (LOGGING) {
            Log.i(CLASS, "onCreate");
            Toast.makeText(getApplicationContext(), "onCreate", Toast.LENGTH_SHORT).show();
        }
    }

    private void initButtons() {
        buttonSettings = findViewById(R.id.settingsButton);
        buttonSettings.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            intent.putExtra(SETTINGS, settings);
            intent.putExtra(CITIES, cities);
            startActivityForResult(intent, SETTINGS_REQUEST_CODE);
        });

        buttonAboutCity = findViewById(R.id.webInfoButton);
        buttonAboutCity.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
            intent.putExtra(SearchManager.QUERY, String.format(getResources().getString(R.string.weatherIn), spinnerCity.getSelectedItem().toString()));
            startActivity(intent);
        });
    }

    private Cities initCities() {
        Cities cities = new Cities(getResources().getStringArray(R.array.cities));
        if (LOGGING) {
            Log.i(CLASS, "initCities " + cities);
            Toast.makeText(getApplicationContext(), "initCities " + cities, Toast.LENGTH_SHORT).show();
        }
        return cities;
    }

    private Settings initSettings() {
        Settings settings = new Settings(Settings.DEFAULT_THEME, Settings.DEFAULT_ALL_DETAIL);
        if (LOGGING) {
            Log.i(CLASS, "initSettings " + settings);
            Toast.makeText(getApplicationContext(), "initSettings " + settings, Toast.LENGTH_SHORT).show();
        }
        return settings;
    }

    public void updateSettingsAndCities(Settings settings, Cities cities) {
        this.settings = settings;
        this.cities = cities;
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, cities.getCities().toArray(new String[0]));
        spinnerCity.setAdapter(arrayAdapter);
        spinnerCity.setSelection(spinnerCity.getFirstVisiblePosition());
    }

}