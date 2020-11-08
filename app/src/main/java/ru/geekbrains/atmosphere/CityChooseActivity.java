package ru.geekbrains.atmosphere;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import ru.geekbrains.atmosphere.settings.Cities;

public class CityChooseActivity extends AppCompatActivity implements ExtraConstants {

    private EditText newCity;
    private Button addCity;
    private Cities cities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.city_choose);

        cities = getIntent().getParcelableExtra(CITIES);

        newCity = findViewById(R.id.cityChooseText);

        addCity = findViewById(R.id.addCity);
        addCity.setOnClickListener(view -> {
            Intent intentResult = new Intent();
            cities.addCity(newCity.getText().toString());
            intentResult.putExtra(CITIES, cities);
            setResult(RESULT_OK, intentResult);
            finish();
        });
    }
}