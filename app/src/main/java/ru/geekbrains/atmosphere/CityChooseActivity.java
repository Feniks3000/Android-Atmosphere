package ru.geekbrains.atmosphere;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class CityChooseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.city_choose);
        initCityChoose();
    }

    public void initCityChoose() {
        Spinner spinnerCity = findViewById(R.id.spinnerCityChoose);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.cities));
        spinnerCity.setAdapter(arrayAdapter);
    }
}