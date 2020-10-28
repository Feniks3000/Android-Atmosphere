package ru.geekbrains.atmosphere;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableRow;

public class MainActivity extends AppCompatActivity {

//    private ListView listCity;
    private Spinner spinnerCity;
    private ImageView imagePrecipitationNow;
    private TableRow rowTempNext4Hour;
//    private RecyclerView recyclerCity;
//    private RecyclerView.Adapter adapter;
//    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        listCity = findViewById(R.id.listCity);
        spinnerCity = findViewById(R.id.spinnerCity);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new String[]{"Moscow", "Kemerovo", "Novosibirsk"});
//        listCity.setAdapter(arrayAdapter);
        spinnerCity.setAdapter(arrayAdapter);

        imagePrecipitationNow = findViewById(R.id.imagePrecipitationNow);
//        imagePrecipitationNow.setMinimumHeight(spinnerCity.getHeight());

        rowTempNext4Hour = findViewById(R.id.rowTempNext4Hour);
//        rowTempNext4Hour.addChildrenForAccessibility();

//        recyclerCity = findViewById(R.id.recyclerCity);
//        layoutManager = new LinearLayoutManager(this);
//        recyclerCity.setLayoutManager(layoutManager);
//        adapter = new CityAdapter();
//        recyclerCity.setAdapter(adapter);
    }
}