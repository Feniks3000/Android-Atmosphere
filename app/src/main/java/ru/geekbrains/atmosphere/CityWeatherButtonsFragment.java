package ru.geekbrains.atmosphere;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class CityWeatherButtonsFragment extends Fragment implements View.OnClickListener, ExtraConstants {

    private View view;
    private Button buttonSettings;
    private Button buttonAboutCity;

    private String city;

    public CityWeatherButtonsFragment() {
    }

    public static CityWeatherButtonsFragment create(String city) {
        CityWeatherButtonsFragment fragment = new CityWeatherButtonsFragment();
        Bundle args = new Bundle();
        args.putString(CITY, city);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            city = getArguments().getString(CITY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_city_weather_buttons, container, false);

        buttonSettings = view.findViewById(R.id.settingsButton);
        buttonSettings.setOnClickListener(view -> {
            Toast.makeText(getActivity(), "buttonSettings", Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
//            intent.putExtra(SETTINGS, settings);
//            intent.putExtra(CITIES, cities);
//            startActivityForResult(intent, SETTINGS_REQUEST_CODE);
        });

        buttonAboutCity = view.findViewById(R.id.webInfoButton);
        buttonAboutCity.setOnClickListener(view -> {
            Toast.makeText(getActivity(), "buttonAboutCity", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
            intent.putExtra(SearchManager.QUERY, String.format(getResources().getString(R.string.weatherIn), city));
            startActivity(intent);
        });
        return view;
    }

    @Override
    public void onClick(View v) {
    }
}