package ru.geekbrains.atmosphere;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import ru.geekbrains.atmosphere.settings.Cities;
import ru.geekbrains.atmosphere.settings.Settings;

public class MainActivity extends AppCompatActivity implements ExtraConstants {

    private static final String CLASS = CityWeatherActivity.class.getSimpleName();
    private static final boolean LOGGING = false;

    private boolean landscapeOrientation;
    private Spinner spinnerCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        landscapeOrientation = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;

        Toast.makeText(getApplicationContext(), String.valueOf(MyApp.getInstance().getStorage().incCounter()), Toast.LENGTH_SHORT).show();

        spinnerCity = findViewById(R.id.spinnerCity);
        updateSettingsAndCities(getSettings(), getCities());
        spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CityWeatherButtonsFragment buttonsFragment = CityWeatherButtonsFragment.create(spinnerCity.getSelectedItem().toString(), getSettings(), getCities(), landscapeOrientation);
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
        setSettings(settings);
        setCities(cities);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, cities.getCities().toArray(new String[0]));
        spinnerCity.setAdapter(arrayAdapter);
        spinnerCity.setSelection(spinnerCity.getFirstVisiblePosition());
    }

    public void changeFragment(int containerViewId, androidx.fragment.app.Fragment fragment) {
        if (landscapeOrientation) {
            getSupportFragmentManager().beginTransaction().replace(containerViewId, fragment).commit();
        }
    }

    private Settings getSettings() {
        Settings settings = MyApp.getInstance().getStorage().getSettings();
        if (settings == null) {
            settings = initSettings();
            setSettings(initSettings());
        }
        return settings;
    }

    void setSettings(Settings settings) {
        MyApp.getInstance().getStorage().setSettings(settings);
    }

    private Cities getCities() {
        Cities cities = MyApp.getInstance().getStorage().getCities();
        if (cities == null) {
            cities = initCities();
            setCities(cities);
        }
        return cities;
    }

    void setCities(Cities cities) {
        MyApp.getInstance().getStorage().setCities(cities);
    }

}