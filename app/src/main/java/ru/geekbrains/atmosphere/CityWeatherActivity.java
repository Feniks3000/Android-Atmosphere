package ru.geekbrains.atmosphere;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class CityWeatherActivity extends AppCompatActivity {

    private static final String CLASS = CityWeatherActivity.class.getSimpleName();

    private Spinner spinnerCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.city_weather);
        initCityWeather();
        Log.i(CLASS, "onCreate");
        Toast.makeText(getApplicationContext(), "onCreate", Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(CLASS, "onStart");
        Toast.makeText(getApplicationContext(), "onStart", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i(CLASS, "onRestoreInstanceState");
        Toast.makeText(getApplicationContext(), "onRestoreInstanceState", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(CLASS, "onResume");
        Toast.makeText(getApplicationContext(), "onResume", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(CLASS, "onPause");
        Toast.makeText(getApplicationContext(), "onPause", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(CLASS, "onSaveInstanceState");
        Toast.makeText(getApplicationContext(), "onSaveInstanceState", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(CLASS, "onRestart");
        Toast.makeText(getApplicationContext(), "onRestart", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(CLASS, "onStop");
        Toast.makeText(getApplicationContext(), "onStop", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(CLASS, "onDestroy");
        Toast.makeText(getApplicationContext(), "onDestroy", Toast.LENGTH_SHORT).show();
    }
}