package ru.geekbrains.atmosphere.settings;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import ru.geekbrains.atmosphere.CityChooseActivity;
import ru.geekbrains.atmosphere.ExtraConstants;
import ru.geekbrains.atmosphere.R;

public class SettingsActivity extends AppCompatActivity implements ExtraConstants {

    private static final String CLASS = SettingsActivity.class.getSimpleName();
    private static final int CITIES_REQUEST_CODE = 1;
    private static final boolean LOGGING = false;

    private RadioGroup settingTheme;
    private Switch settingWeatherDetail;
    private TextView addCity;
    private Button saveSettings;
    private RecyclerView citiesList;

    private CitiesAdapter citiesAdapter;

    private Settings settings;
    private Cities cities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        settingTheme = findViewById(R.id.settingsTheme);
        settingWeatherDetail = findViewById(R.id.weatherDetail);

        settings = getIntent().getParcelableExtra(SETTINGS);
        cities = getIntent().getParcelableExtra(CITIES);

        if (LOGGING) {
            Log.i(CLASS, "onCreate received settings " + settings);
            Toast.makeText(getApplicationContext(), "onCreate received settings " + settings, Toast.LENGTH_SHORT).show();

            Log.i(CLASS, "onCreate received cities " + cities);
            Toast.makeText(getApplicationContext(), "onCreate received cities " + cities, Toast.LENGTH_SHORT).show();
        }

        ((RadioButton) settingTheme.getChildAt(settings.getTheme())).setChecked(true);
        settingWeatherDetail.setChecked(settings.isAllDetail());

        saveSettings = findViewById(R.id.saveSettings);
        saveSettings.setOnClickListener(view -> {
            settings.setTheme(settingTheme.indexOfChild(settingTheme.findViewById(settingTheme.getCheckedRadioButtonId())));
            settings.setAllDetail(settingWeatherDetail.isChecked());
            Intent intentResult = new Intent();
            intentResult.putExtra(SETTINGS, settings);
            intentResult.putExtra(CITIES, cities);
            setResult(RESULT_OK, intentResult);
            finish();
        });

        addCity = findViewById(R.id.settingsCitiesHeader);
        addCity.setOnClickListener(view -> {
            Intent intent = new Intent(SettingsActivity.this, CityChooseActivity.class);
            intent.putExtra(CITIES, cities);
            startActivityForResult(intent, CITIES_REQUEST_CODE);
        });

        // TODO: Need remove cities from the list
        citiesList = findViewById(R.id.cityList);
        citiesAdapter = new CitiesAdapter(cities.getCities());
        citiesList.setAdapter(citiesAdapter);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != CITIES_REQUEST_CODE) {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }

        if (resultCode == RESULT_OK) {
            cities = data.getParcelableExtra(CITIES);
            citiesAdapter = new CitiesAdapter(cities.getCities());
            citiesList.setAdapter(citiesAdapter);

            if (LOGGING) {
                Log.i(CLASS, "onActivityResult - received new cities " + cities);
                Toast.makeText(getApplicationContext(), "onActivityResult - received new cities " + cities, Toast.LENGTH_SHORT).show();
            }
        }
    }

}