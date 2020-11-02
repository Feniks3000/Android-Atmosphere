package ru.geekbrains.atmosphere;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {

    private Spinner spinnerCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.city_weather);
        initCityWeather();
    }

    public void openSettings(View view) {
        setContentView(R.layout.settings);
    }

    public void saveSettings(View view) {
        setContentView(R.layout.city_weather);
        initCityWeather();
    }

    public void initCityWeather() {
        spinnerCity = findViewById(R.id.spinnerCity);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new String[]{"Moscow", "Kemerovo", "Novosibirsk"});
        spinnerCity.setAdapter(arrayAdapter);
    }

    public void initCityChoose() {
        spinnerCity = findViewById(R.id.spinnerCityChoose);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new String[]{"Moscow", "Kemerovo", "Novosibirsk"});
        spinnerCity.setAdapter(arrayAdapter);
    }
}