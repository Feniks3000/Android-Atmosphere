package ru.geekbrains.atmosphere;

import android.content.Context;
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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import ru.geekbrains.atmosphere.settings.Cities;
import ru.geekbrains.atmosphere.settings.CitiesAdapter;
import ru.geekbrains.atmosphere.settings.Settings;

public class SettingsFragment extends Fragment implements View.OnClickListener, ExtraConstants {

    private static final String CLASS = SettingsFragment.class.getSimpleName();
    private static final boolean LOGGING = false;

    private OnUpdateSettingsAndCitiesListener onUpdateSettingsAndCitiesListener;
    private OnChangeFragmentListener onChangeFragmentListener;

    private RadioGroup settingTheme;
    private Switch settingWeatherDetail;

    private Settings settings;
    private Cities cities;

    public SettingsFragment() {
    }

    public static SettingsFragment create(Settings settings, Cities cities) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putParcelable(SETTINGS, settings);
        args.putParcelable(CITIES, cities);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            settings = getArguments().getParcelable(SETTINGS);
            cities = getArguments().getParcelable(CITIES);
        }

        if (LOGGING) {
            Log.d(CLASS, "OnCreate. Settings - " + settings);
            Log.d(CLASS, "OnCreate. Cities - " + cities);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        settingTheme = view.findViewById(R.id.settingsTheme);
        settingWeatherDetail = view.findViewById(R.id.weatherDetail);

        ((RadioButton) settingTheme.getChildAt(settings.getTheme())).setChecked(true);
        settingWeatherDetail.setChecked(settings.isAllDetail());

        Button saveSettings = view.findViewById(R.id.saveSettings);
        saveSettings.setOnClickListener(button -> {
            settings.setTheme(settingTheme.indexOfChild(settingTheme.findViewById(settingTheme.getCheckedRadioButtonId())));
            settings.setAllDetail(settingWeatherDetail.isChecked());

            onUpdateSettingsAndCitiesListener.onUpdateSettingsAndCities(settings, cities);
            onChangeFragmentListener.onChangeFragment(ButtonsFragment.create(settings, cities), true);
        });

        TextView addCity = view.findViewById(R.id.settingsCitiesHeader);
        addCity.setOnClickListener(button -> {
            settings.setTheme(settingTheme.indexOfChild(settingTheme.findViewById(settingTheme.getCheckedRadioButtonId())));
            settings.setAllDetail(settingWeatherDetail.isChecked());
            onChangeFragmentListener.onChangeFragment(CityChooseFragment.create(settings, cities), false);
        });

        // TODO: Need remove cities from the list
        RecyclerView citiesList = view.findViewById(R.id.cityList);
        CitiesAdapter citiesAdapter = new CitiesAdapter(cities.getCities());
        citiesList.setAdapter(citiesAdapter);

        return view;
    }

    @Override
    public void onClick(View v) {
    }

    public interface OnUpdateSettingsAndCitiesListener {
        void onUpdateSettingsAndCities(Settings settings, Cities cities);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnUpdateSettingsAndCitiesListener) {
            onUpdateSettingsAndCitiesListener = (OnUpdateSettingsAndCitiesListener) context;
        }
        if (context instanceof OnChangeFragmentListener) {
            onChangeFragmentListener = (OnChangeFragmentListener) context;
        }
    }
}