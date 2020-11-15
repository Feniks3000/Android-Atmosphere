package ru.geekbrains.atmosphere;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;

import ru.geekbrains.atmosphere.settings.Cities;
import ru.geekbrains.atmosphere.settings.CitiesAdapter;
import ru.geekbrains.atmosphere.settings.Settings;
import ru.geekbrains.atmosphere.settings.SettingsActivity;

import static android.app.Activity.RESULT_OK;

public class SettingsFragment extends Fragment implements View.OnClickListener, ExtraConstants {

    private static final String CLASS = SettingsFragment.class.getSimpleName();
    private static final boolean LOGGING = false;

    private RadioGroup settingTheme;
    private Switch settingWeatherDetail;

    private RecyclerView citiesList;
    private CitiesAdapter citiesAdapter;

    private Settings settings;
    private Cities cities;
    private boolean landscapeOrientation;
    private MainActivity mainActivity;

    public SettingsFragment() {
    }

    public static SettingsFragment create(Settings settings, Cities cities, boolean landscapeOrientation) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
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
            settings = getArguments().getParcelable(SETTINGS);
            cities = getArguments().getParcelable(CITIES);
            landscapeOrientation = getArguments().getBoolean(LANDSCAPE_ORIENTATION);
        } else {
            settings = Objects.requireNonNull(getActivity()).getIntent().getParcelableExtra(SETTINGS);
            cities = Objects.requireNonNull(getActivity()).getIntent().getParcelableExtra(CITIES);
//            settings = MyApp.getInstance().getStorage().getSettings();
//            cities = MyApp.getInstance().getStorage().getCities();
            landscapeOrientation = false;
        }
        if (landscapeOrientation) {
            mainActivity = (MainActivity) getActivity();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        settingTheme = view.findViewById(R.id.settingsTheme);
        settingWeatherDetail = view.findViewById(R.id.weatherDetail);

        if (LOGGING) {
            Log.i(CLASS, "onCreateView received settings " + settings);
            Toast.makeText(getContext(), "onCreateView received settings " + settings, Toast.LENGTH_SHORT).show();

            Log.i(CLASS, "onCreateView received cities " + cities);
            Toast.makeText(getContext(), "onCreateView received cities " + cities, Toast.LENGTH_SHORT).show();
        }

        ((RadioButton) settingTheme.getChildAt(settings.getTheme())).setChecked(true);
        settingWeatherDetail.setChecked(settings.isAllDetail());

        Button saveSettings = view.findViewById(R.id.saveSettings);
        saveSettings.setOnClickListener(button -> {
            settings.setTheme(settingTheme.indexOfChild(settingTheme.findViewById(settingTheme.getCheckedRadioButtonId())));
            settings.setAllDetail(settingWeatherDetail.isChecked());
            if (landscapeOrientation) {
                mainActivity.changeFragment(R.id.infoFragment, CityWeatherButtonsFragment.create(cities.getCities().get(0), settings, cities, landscapeOrientation));
            } else {
                Intent intentResult = new Intent();
                intentResult.putExtra(SETTINGS, settings);
                intentResult.putExtra(CITIES, cities);
                getActivity().setResult(RESULT_OK, intentResult);
                getActivity().finish();
            }
        });

        TextView addCity = view.findViewById(R.id.settingsCitiesHeader);
        addCity.setOnClickListener(button -> {
            settings.setTheme(settingTheme.indexOfChild(settingTheme.findViewById(settingTheme.getCheckedRadioButtonId())));
            settings.setAllDetail(settingWeatherDetail.isChecked());
            if (landscapeOrientation) {
                mainActivity.changeFragment(R.id.infoFragment, CityChooseFragment.create(settings, cities, landscapeOrientation));
            } else {
                Intent intent = new Intent(getContext(), CityChooseActivity.class);
                intent.putExtra(SETTINGS, settings);
                intent.putExtra(CITIES, cities);
                intent.putExtra(LANDSCAPE_ORIENTATION, landscapeOrientation);
                startActivityForResult(intent, CITIES_REQUEST_CODE);
            }
        });

        // TODO: Need remove cities from the list
        citiesList = view.findViewById(R.id.cityList);
        citiesAdapter = new CitiesAdapter(cities.getCities());
        citiesList.setAdapter(citiesAdapter);

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != CITIES_REQUEST_CODE) {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }
        settings = data.getParcelableExtra(SETTINGS);
        cities = data.getParcelableExtra(CITIES);
        citiesAdapter = new CitiesAdapter(cities.getCities());
        citiesList.setAdapter(citiesAdapter);
        if (LOGGING) {
            Log.i(CLASS, "onActivityResult - received new settings " + settings);
            Toast.makeText(getContext(), "onActivityResult - received new settings " + settings, Toast.LENGTH_SHORT).show();
            Log.i(CLASS, "onActivityResult - received new cities " + cities);
            Toast.makeText(getContext(), "onActivityResult - received new cities " + cities, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
    }
}