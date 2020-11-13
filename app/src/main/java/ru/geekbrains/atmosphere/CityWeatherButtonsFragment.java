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
    private static final int SETTINGS_REQUEST_CODE = 1;
    private static final boolean LOGGING = false;

    private View view;
    private Button buttonSettings;
    private Button buttonAboutCity;

    private String city;
    private Settings settings;
    private Cities cities;
    private boolean landscapeOrientation;
    private static MainActivity mainActivity;

    public CityWeatherButtonsFragment() {
    }

    public static CityWeatherButtonsFragment create(String city, Settings settings, Cities cities, boolean landscapeOrientation, MainActivity activity) {
        CityWeatherButtonsFragment fragment = new CityWeatherButtonsFragment();
        Bundle args = new Bundle();
        args.putString(CITY, city);
        args.putParcelable(SETTINGS, settings);
        args.putParcelable(CITIES, cities);
        args.putBoolean(LANDSCAPE_ORIENTATION, landscapeOrientation);
        CityWeatherButtonsFragment.mainActivity = activity;
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_city_weather_buttons, container, false);

        buttonSettings = view.findViewById(R.id.settingsButton);
        buttonSettings.setOnClickListener(view -> {
            Toast.makeText(getActivity(), "buttonSettings", Toast.LENGTH_SHORT).show();
            if (landscapeOrientation) {
                Toast.makeText(getActivity(), "buttonSettings - land", Toast.LENGTH_SHORT).show();
                Log.i("buttonSettings", String.valueOf(landscapeOrientation));
                SettingsFragment settingsFragment = new SettingsFragment();
                mainActivity.getSupportFragmentManager().beginTransaction().replace(R.id.infoFragment, settingsFragment).commit();
            } else {
                Toast.makeText(getActivity(), "buttonSettings - no land", Toast.LENGTH_SHORT).show();
                Log.i("buttonSettings", String.valueOf(landscapeOrientation));
                Intent intent = new Intent(getContext(), SettingsActivity.class);
                intent.putExtra(SETTINGS, settings);
                intent.putExtra(CITIES, cities);
                startActivityForResult(intent, 1);
            }
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