package ru.geekbrains.atmosphere;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ru.geekbrains.atmosphere.settings.Cities;
import ru.geekbrains.atmosphere.settings.Settings;
import ru.geekbrains.atmosphere.settings.SettingsActivity;

public class CityWeatherButtonsFragment extends Fragment implements View.OnClickListener, ExtraConstants {

    private static final String CLASS = CityWeatherButtonsFragment.class.getSimpleName();
    private static final boolean LOGGING = false;

    private String city;
    private Settings settings;
    private Cities cities;
    private boolean landscapeOrientation;
    private MainActivity mainActivity;

    public CityWeatherButtonsFragment() {
    }

    public static CityWeatherButtonsFragment create(String city, Settings settings, Cities cities, boolean landscapeOrientation) {
        CityWeatherButtonsFragment fragment = new CityWeatherButtonsFragment();
        Bundle args = new Bundle();
        args.putString(CITY, city);
        args.putParcelable(SETTINGS, settings);
        args.putParcelable(CITIES, cities);
        args.putBoolean(LANDSCAPE_ORIENTATION, landscapeOrientation);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            city = getArguments().getString(CITY);
            settings = getArguments().getParcelable(SETTINGS);
            cities = getArguments().getParcelable(CITIES);
            landscapeOrientation = getArguments().getBoolean(LANDSCAPE_ORIENTATION);
        }
        mainActivity = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_city_weather_buttons, container, false);

        Button buttonSettings = view.findViewById(R.id.settingsButton);
        buttonSettings.setOnClickListener(button -> {
            if (LOGGING) {
                Toast.makeText(getActivity(), String.format("landscape orientation %b", landscapeOrientation), Toast.LENGTH_SHORT).show();
                Log.i(CLASS, String.format("landscape orientation %b", landscapeOrientation));
            }
            if (landscapeOrientation) {
                mainActivity.changeFragment(R.id.infoFragment, SettingsFragment.create(settings, cities, landscapeOrientation));
            } else {
                Intent intent = new Intent(getContext(), SettingsActivity.class);
                intent.putExtra(SETTINGS, settings);
                intent.putExtra(CITIES, cities);
                startActivityForResult(intent, SETTINGS_REQUEST_CODE);
            }
        });

        Button buttonAboutCity = view.findViewById(R.id.webInfoButton);
        buttonAboutCity.setOnClickListener(button -> {
            Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
            intent.putExtra(SearchManager.QUERY, String.format(getResources().getString(R.string.weatherIn), city));
            startActivity(intent);
        });
        return view;
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode != SETTINGS_REQUEST_CODE) {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }
        settings = data.getParcelableExtra(SETTINGS);
        cities = data.getParcelableExtra(CITIES);
        mainActivity.updateSettingsAndCities(settings, cities);
        if (LOGGING) {
            Log.i(CLASS, "onActivityResult - received new settings " + settings);
            Toast.makeText(getContext(), "onActivityResult - received new settings " + settings, Toast.LENGTH_SHORT).show();
            Log.i(CLASS, "onActivityResult - received new cities " + cities);
            Toast.makeText(getContext(), "onActivityResult - received new cities " + cities, Toast.LENGTH_SHORT).show();
        }
    }
}