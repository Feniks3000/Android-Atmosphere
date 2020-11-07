package ru.geekbrains.atmosphere.settings;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import ru.geekbrains.atmosphere.ExtraConstants;
import ru.geekbrains.atmosphere.R;

public class SettingsActivity extends AppCompatActivity implements ExtraConstants {

    private static final String CLASS = SettingsActivity.class.getSimpleName();
    private static final boolean LOGGING = false;

    private RadioGroup settingTheme;
    private Switch settingWeatherDetail;
    private Button saveSettings;

    private Settings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        settingTheme = findViewById(R.id.settingsTheme);
        settingWeatherDetail = findViewById(R.id.weatherDetail);

        settings = getIntent().getParcelableExtra(SETTINGS);

        if (LOGGING) {
            Log.i(CLASS, "onCreate received settings " + settings);
            Toast.makeText(getApplicationContext(), "onCreate received settings " + settings, Toast.LENGTH_SHORT).show();
        }

        ((RadioButton) settingTheme.getChildAt(settings.getTheme())).setChecked(true);
        settingWeatherDetail.setChecked(settings.isAllDetail());


        saveSettings = findViewById(R.id.saveSettings);
        saveSettings.setOnClickListener(view -> {
            settings.setTheme(settingTheme.indexOfChild(settingTheme.findViewById(settingTheme.getCheckedRadioButtonId())));
            settings.setAllDetail(settingWeatherDetail.isChecked());
            Intent intentResult = new Intent();
            intentResult.putExtra(SETTINGS, settings);
            setResult(RESULT_OK, intentResult);
            finish();
        });


    }
}