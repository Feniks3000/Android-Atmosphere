package ru.geekbrains.atmosphere;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableRow;

public class MainActivity extends AppCompatActivity {

    private Spinner spinnerCity;
    private ImageView imagePrecipitationNow;
    private TableRow rowTempNext4Hour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinnerCity = findViewById(R.id.spinnerCity);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new String[]{"Moscow", "Kemerovo", "Novosibirsk"});
        spinnerCity.setAdapter(arrayAdapter);

        imagePrecipitationNow = findViewById(R.id.imagePrecipitationNow);
        rowTempNext4Hour = findViewById(R.id.rowTempNext4Hour);
    }
}